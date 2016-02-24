package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.model.CADetails;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.ESPDRequestImpl;
import grow.names.specification.ubl.schema.xsd.espdrequest_1.ESPDRequestType;
import java.io.InputStream;
import java.io.StringWriter;
import javax.xml.bind.JAXB;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.AddressType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.AttachmentType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.ContractingPartyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.CountryType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.DocumentReferenceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.ExternalReferenceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyNameType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.ContractFolderIDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.DescriptionType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.DocumentTypeCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.FileNameType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IDType;
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
        
        //TODO: Need to add null checks here
        cd.setProcurementProcedureFileReferenceNo(reqType.getContractFolderID().getValue());
        cd.setProcurementProcedureTitle(reqType.getAdditionalDocumentReference().get(0).getAttachment().getExternalReference().getFileName().getValue());
        cd.setProcurementProcedureDesc(reqType.getAdditionalDocumentReference().get(0).getAttachment().getExternalReference().getDescription().get(0).getValue());                
        return req;
         
    }
    
    public ESPDRequestType createXML(ESPDRequest req) {
        
        // CA Details First
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

        if (req.getCADetails().getProcurementProcedureFileReferenceNo() != null) {
           reqType.setContractFolderID(new ContractFolderIDType());
           reqType.getContractFolderID().setValue(req.getCADetails().getProcurementProcedureFileReferenceNo());
        }
        
                
        //Adding the additional document reference
        if (req.getCADetails().getProcurementProcedureTitle() != null) {
            DocumentReferenceType dr = new DocumentReferenceType();
            dr.setID(new IDType());
            // Adding some default values. Will be elaborated later through either the model or the builder
            dr.setDocumentTypeCode(new DocumentTypeCodeType());
            dr.getDocumentTypeCode().setValue("TED_CN");
            dr.getDocumentTypeCode().setListID("ReferencesTypeCodes");
            dr.getDocumentTypeCode().setListAgencyID("EU-COM-GROW");
            dr.getDocumentTypeCode().setListVersionID("1.0");
            dr.setAttachment(new AttachmentType());
            dr.getAttachment().setExternalReference(new ExternalReferenceType());
            dr.getAttachment().getExternalReference().setFileName(new FileNameType());
            dr.getAttachment().getExternalReference().getFileName().setValue(req.getCADetails().getProcurementProcedureTitle());

            DescriptionType dt = new DescriptionType();
            dt.setValue(req.getCADetails().getProcurementProcedureDesc());
            dr.getAttachment().getExternalReference().getDescription().add(dt);

           reqType.getAdditionalDocumentReference().add(dr);
          
        }
        // TODO: Criterias, Requirement Groups, Requirements
        
        
        // TODO: Evidences
        
        //Return the Object
        return reqType;
    }
    public String createXMLasString(ESPDRequest req) {
        StringWriter result = new StringWriter();
        JAXB.marshal(createXML(req), result);
        return result.toString();
    }
    
    private ESPDRequestType read(InputStream is) {
        // Start with the convience methods provided by JAXB. If there are
        // perfomance issues we will swicth back to the JAXB API Usage
        ESPDRequestType er = JAXB.unmarshal(is, ESPDRequestType.class);
        return er;
    }    
}
