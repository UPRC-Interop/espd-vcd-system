package eu.esens.espdvcd.builder;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;
import javax.xml.bind.JAXB;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import eu.esens.espdvcd.builder.model.ModelFactory;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.ESPDResponse;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.builder.schema.SchemaFactory;

import grow.names.specification.ubl.schema.xsd.espdrequest_1.ESPDRequestType;
import grow.names.specification.ubl.schema.xsd.espdresponse_1.ESPDResponseType;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.CopyIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IssueDateType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IssueTimeType;

public class ESPDBuilder {

    private boolean insertAllCriteria = true;
    
    public ESPDRequest createESPDRequestFromXML(InputStream xmlESPD) throws Exception {

        // Check and read the file in the JAXB Object
        ESPDRequestType reqType = readESPDRequestFromStream(xmlESPD);

        // Create the Model Object      
        ESPDRequest req = ModelFactory.ESPD_REQUEST.extractESPDRequest(reqType); 
        
        if (insertAllCriteria) {
          CriteriaExtractor cr = new PredefinedESPDCriteriaExtractor();
            req.setCriterionList(cr.getFullList(req.getFullCriterionList()));
        }
        return req;

    }
    
    public ESPDResponse createESPDResponseFromXML(InputStream xmlESPDRes) throws Exception {

        // Check and read the file in the JAXB Object
        ESPDResponseType resType = readESPDResponseFromStream(xmlESPDRes);

        // Create the Model Object      
        ESPDResponse res = ModelFactory.ESPD_RESPONSE.extractESPDResponse(resType);
        return res;
    }
    
    
    public ESPDRequestType createXML(ESPDRequest req) {
        ESPDRequestType reqType = finalize(SchemaFactory.ESPD_REQUEST.extractESPDRequestType(req));
        return reqType;
        
    }
    
    public ESPDResponseType createXML(ESPDResponse res) {
        ESPDResponseType resType = finalize(SchemaFactory.ESPD_RESPONSE.extractESPDResponseType(res));
        return resType;
    }
    
    public ESPDRequestType finalize(ESPDRequestType reqType) {
        
        // Finalizes the ESPDRequest Type, adding the Date and Time of Issue etc
        reqType.setUBLVersionID(SchemaFactory.ESPD_REQUEST.createUBL21VersionIdType());
        
        reqType.setCustomizationID(SchemaFactory.ESPD_REQUEST.createBIICustomizationIdType("urn:www.cenbii.eu:transaction:biitrns070:ver3.0"));
        reqType.setVersionID(SchemaFactory.ESPD_REQUEST.createVersionIDType("1"));

        reqType.setCopyIndicator(new CopyIndicatorType());
        reqType.getCopyIndicator().setValue(false);
        

        Date now = new Date();
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(now);
       
        try {
            // Creates the format according to the EC Application Requirement
            XMLGregorianCalendar xmlDate = DatatypeFactory.newInstance()
                    .newXMLGregorianCalendar(
                            c.get(Calendar.YEAR),
                            c.get(Calendar.MONTH)+1, 
                            c.get(Calendar.DAY_OF_MONTH),
                            c.get(Calendar.HOUR),
                            c.get(Calendar.MINUTE),
                            c.get(Calendar.SECOND),
                            DatatypeConstants.FIELD_UNDEFINED,
                            DatatypeConstants.FIELD_UNDEFINED                            
                    );
           
            reqType.setIssueDate(new IssueDateType());
            reqType.getIssueDate().setValue(xmlDate);
            reqType.setIssueTime(new IssueTimeType());
            reqType.getIssueTime().setValue(xmlDate);
        } catch (DatatypeConfigurationException e) {
            System.out.println("ERROR in DATES!");
        }
        
        reqType.setID(SchemaFactory.ESPD_REQUEST.createISOIECIDType(UUID.randomUUID().toString()));
        return reqType;
    }
    
        public ESPDResponseType finalize(ESPDResponseType resType) {
        
        // Finalizes the ESPDResponse Type, adding the Date and Time of Issue etc
        resType.setUBLVersionID(SchemaFactory.ESPD_RESPONSE.createUBL21VersionIdType());
        
        resType.setCustomizationID(SchemaFactory.ESPD_RESPONSE.createBIICustomizationIdType("urn:www.cenbii.eu:transaction:biitrns092:ver3.0"));
        resType.setVersionID(SchemaFactory.ESPD_RESPONSE.createVersionIDType("1"));

        resType.setCopyIndicator(new CopyIndicatorType());
        resType.getCopyIndicator().setValue(false);
        

        Date now = new Date();
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(now);
       
        try {
            // Creates the format according to the EC Application Requirement
            XMLGregorianCalendar xmlDate = DatatypeFactory.newInstance()
                    .newXMLGregorianCalendar(
                            c.get(Calendar.YEAR),
                            c.get(Calendar.MONTH)+1, 
                            c.get(Calendar.DAY_OF_MONTH),
                            c.get(Calendar.HOUR),
                            c.get(Calendar.MINUTE),
                            c.get(Calendar.SECOND),
                            DatatypeConstants.FIELD_UNDEFINED,
                            DatatypeConstants.FIELD_UNDEFINED                            
                    );
           
            resType.setIssueDate(new IssueDateType());
            resType.getIssueDate().setValue(xmlDate);
            resType.setIssueTime(new IssueTimeType());
            resType.getIssueTime().setValue(xmlDate);
        } catch (DatatypeConfigurationException e) {
            System.out.println("ERROR in DATES!");
        }
        
        resType.setID(SchemaFactory.ESPD_RESPONSE.createISOIECIDType(UUID.randomUUID().toString()));
        return resType;
    }

    public String createXMLasString(ESPDRequest doc) {
        StringWriter result = new StringWriter();
   
        //Return the Object
        if (doc instanceof ESPDResponse) {
            grow.names.specification.ubl.schema.xsd.espdresponse_1.ObjectFactory of = new grow.names.specification.ubl.schema.xsd.espdresponse_1.ObjectFactory();
            JAXB.marshal(of.createESPDResponse(createXML((ESPDResponse)doc)), result);
        } else {
            grow.names.specification.ubl.schema.xsd.espdrequest_1.ObjectFactory of = new grow.names.specification.ubl.schema.xsd.espdrequest_1.ObjectFactory();
            JAXB.marshal(of.createESPDRequest(createXML(doc)), result);
        }
        
        return result.toString();
    }

    public List<SelectableCriterion> getCriteriaList() {
        CriteriaExtractor cr = new PredefinedESPDCriteriaExtractor();
        return cr.getFullList();
    }

    private ESPDRequestType readESPDRequestFromStream(InputStream is) {
        // Start with the convience methods provided by JAXB. If there are
        // perfomance issues we will swicth back to the JAXB API Usage
        ESPDRequestType er = JAXB.unmarshal(is, ESPDRequestType.class);
        return er;
    }
    
    private ESPDResponseType readESPDResponseFromStream(InputStream is) {
        // Start with the convience methods provided by JAXB. If there are
        // perfomance issues we will swicth back to the JAXB API Usage
        ESPDResponseType er = JAXB.unmarshal(is, ESPDResponseType.class);
        return er;
    }
    
    public void setInsertAllCriteria(boolean insertAll) {
        insertAllCriteria = insertAll;
    }
    
    public boolean isInsertAllCriteria() {
        return insertAllCriteria;
    } 
}
