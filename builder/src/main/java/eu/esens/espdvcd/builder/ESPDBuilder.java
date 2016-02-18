package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.model.CADetails;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.ESPDRequestImpl;
import grow.names.specification.ubl.schema.xsd.espdrequest_1.ESPDRequestType;
import java.io.InputStream;
import java.io.StringWriter;
import javax.xml.bind.JAXB;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.AddressType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.ContractingPartyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.CountryType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyNameType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IdentificationCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.NameType;

/**
 *
 * @author Jerry Dimitriou <jerouris@unipi.gr>
 */
public class ESPDBuilder {
    
    public ESPDRequest createFromXML(InputStream xmlESPD) throws Exception {
         
        // Check and read the file in the JAXB Object
        ESPDRequestType reqType = read(xmlESPD);
         
        // Create the Model Object
        ESPDRequest req = new ESPDRequestImpl();
        CADetails cd = new CADetails();
        req.setCADetails(cd);
        
        if (!reqType.getContractingParty()
                .getParty().getPartyName().isEmpty()) {
                    cd.setCAOfficialName(reqType.getContractingParty()
                .getParty().getPartyName()
                .get(0).getName().getValue());           
        } else throw new Exception("Cannot find party name");

       
        cd.setCACountry(reqType.getContractingParty()
                .getParty().getPostalAddress().getCountry().getIdentificationCode().getValue());
                
        return req;
         
    }
    
    public String createXML(ESPDRequest req) {
        
        ESPDRequestType reqType = new ESPDRequestType();
        reqType.setContractingParty(new ContractingPartyType());
        reqType.getContractingParty().setParty(new PartyType());
        PartyNameType pt = new PartyNameType();
        pt.setName(new NameType());
        pt.getName().setValue(req.getCADetails().getCAOfficialName());
        
        reqType.getContractingParty().getParty().setPostalAddress(new AddressType());
        reqType.getContractingParty().getParty().getPostalAddress().setCountry(new CountryType());
        reqType.getContractingParty().getParty().getPostalAddress().getCountry().setIdentificationCode(new IdentificationCodeType());
        reqType.getContractingParty().getParty().getPostalAddress().getCountry().getIdentificationCode().setValue(req.getCADetails().getCACountry());

        reqType.getContractingParty().getParty().getPartyName().add(pt);

        
        StringWriter result = new StringWriter();
        JAXB.marshal(reqType, result);
        return result.toString();
    }
    
    private ESPDRequestType read(InputStream is) {
        // Start with the convience methods provided by JAXB. If there are
        // perfomance issues we will swicth back to the JAXB API Usage
        ESPDRequestType er = JAXB.unmarshal(is, ESPDRequestType.class);
        return er;
    }
    
}
