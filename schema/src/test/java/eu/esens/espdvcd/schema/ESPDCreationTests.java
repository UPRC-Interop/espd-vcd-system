package eu.esens.espdvcd.schema;

import grow.names.specification.ubl.schema.xsd.espdrequest_1.ESPDRequestType;
import grow.names.specification.ubl.schema.xsd.espdrequest_1.ObjectFactory;
import grow.names.specification.ubl.schema.xsd.espdresponse_1.ESPDResponseType;
import java.io.ByteArrayOutputStream;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.AddressType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.ContractingPartyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.BuildingNameType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IDType;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class ESPDCreationTests {
    
    public ESPDCreationTests() {
    }
    
    @Before
    public void setUp() {
    }
    
    @Test
    public void createESPDRequest() {
        
        ObjectFactory of = new ObjectFactory();
        ESPDRequestType req = new ESPDRequestType();

        //Add an ID
        req.setID(new IDType());
        req.getID().setSchemeID("Lala");
        req.getID().setValue("Value");
        
        req.setContractingParty(new ContractingPartyType());
        req.getContractingParty().setParty(new PartyType());
        req.getContractingParty().getParty().setPostalAddress(new AddressType());
        req.getContractingParty().getParty().getPostalAddress().setBuildingName(new BuildingNameType());
        req.getContractingParty().getParty().getPostalAddress().getBuildingName().setValue("Address Building 123");

        //This must be validated using code list for examle
        req.getContractingParty().getParty().getPostalAddress().getBuildingName().setLanguageID("EN");
        
        System.out.println(toXml(of.createESPDRequest(req)));
        
    }
    
    @Test
    public void createESPDResponse() {

        grow.names.specification.ubl.schema.xsd.espdresponse_1.ObjectFactory of = new grow.names.specification.ubl.schema.xsd.espdresponse_1.ObjectFactory();
        ESPDResponseType res = new ESPDResponseType();
        res.setID(new IDType());
        res.getID().setSchemeID("Lala");
        res.getID().setValue("Value");
        
        System.out.println(toXml(of.createESPDResponse(res)));
        
    }
    
    private String toXml(JAXBElement element) {
    try {

        Marshaller marshaller = SchemaUtil.getMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);  
        

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        marshaller.marshal(element, baos);
        return baos.toString();
    } catch (Exception e) {
        e.printStackTrace();
    }      
    return "";
}
    
}
