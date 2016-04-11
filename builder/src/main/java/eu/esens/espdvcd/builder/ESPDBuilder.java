package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.builder.model.ModelFactory;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.ESPDResponse;
import eu.esens.espdvcd.model.SelectableCriterion;
import grow.names.specification.ubl.schema.xsd.espdrequest_1.ESPDRequestType;
import grow.names.specification.ubl.schema.xsd.espdrequest_1.ObjectFactory;
import grow.names.specification.ubl.schema.xsd.espdresponse_1.ESPDResponseType;
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
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.CopyIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IssueDateType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IssueTimeType;
import eu.esens.espdvcd.builder.schema.SchemaExtractor;
import eu.esens.espdvcd.builder.schema.SchemaFactory;

/**
 *
 */
public class ESPDBuilder {

    private boolean insertAllCriteria = true;
    
    public ESPDRequest createESPDRequestFromXML(InputStream xmlESPD) throws Exception {

        // Check and read the file in the JAXB Object
        ESPDRequestType reqType = read(xmlESPD);

        // Create the Model Object      
        ESPDRequest req = ModelFactory.ESPD_REQUEST.extractESPDRequest(reqType); 
        
        if (insertAllCriteria) {
          CriteriaExtractor cr = new PredefinedESPDCriteriaExtractor();
            req.setCriterionList(cr.getFullList(req.getFullCriterionList()));
        }
        return req;

    }
    
    public ESPDRequestType createXML(ESPDRequest req) {

        ESPDRequestType reqType = finalize(SchemaFactory.ESPD_REQUEST.extractESPDRequestType(req));
        return reqType;
        
    }
    
    public ESPDResponseType createXML(ESPDResponse res) {
       // ESPDRequestType reqType = finalize(SchemaExtractor.extractESPDRequestType(req));
        return new ESPDResponseType();
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

    public String createXMLasString(ESPDRequest req) {
        StringWriter result = new StringWriter();
        ObjectFactory of = new ObjectFactory();

        //Return the Object
        JAXB.marshal(of.createESPDRequest(createXML(req)), result);
        return result.toString();
    }

    public List<SelectableCriterion> getCriteriaList() {
        CriteriaExtractor cr = new PredefinedESPDCriteriaExtractor();
        return cr.getFullList();
    }

    private ESPDRequestType read(InputStream is) {
        // Start with the convience methods provided by JAXB. If there are
        // perfomance issues we will swicth back to the JAXB API Usage
        ESPDRequestType er = JAXB.unmarshal(is, ESPDRequestType.class);
        return er;
    }
    
    public void setInsertAllCriteria(boolean insertAll) {
        insertAllCriteria = insertAll;
    }
    
    public boolean isInsertAllCriteria() {
        return insertAllCriteria;
    } 
}
