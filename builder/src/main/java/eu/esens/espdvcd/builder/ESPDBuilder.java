package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.builder.schema.SchemaFactory;
import eu.esens.espdvcd.builder.model.ESPDRequestModelFactory;
import eu.esens.espdvcd.builder.model.ModelFactory;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.SelectableCriterion;
import grow.names.specification.ubl.schema.xsd.espdrequest_1.ESPDRequestType;
import grow.names.specification.ubl.schema.xsd.espdrequest_1.ObjectFactory;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;
import javax.xml.bind.JAXB;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.CopyIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IssueDateType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IssueTimeType;

/**
 *
 */
public class ESPDBuilder {

    public ESPDRequest createFromXML(InputStream xmlESPD) throws Exception {

        // Check and read the file in the JAXB Object
        ESPDRequestType reqType = read(xmlESPD);

        // Create the Model Object
       
        ESPDRequest req = ModelFactory.ESPD_REQUEST.extractESPDRequest(reqType); 
        
        return req;

    }

    public ESPDRequestType createXML(ESPDRequest req) {

        ESPDRequestType reqType = finalize(SchemaFactory.extractESPDRequestType(req));
        return reqType;
        
    }
    
    public ESPDRequestType finalize(ESPDRequestType reqType) {
        
        // Finalizes the ESPDRequest Type, adding the Date and Time of Issue etc
        reqType.setUBLVersionID(SchemaFactory.createUBL21VersionIdType());
        
        reqType.setCustomizationID(SchemaFactory.createBIICustomizationIdType("urn:www.cenbii.eu:transaction:biitrns070:ver3.0"));
        reqType.setVersionID(SchemaFactory.createVersionIDType("1"));

        reqType.setCopyIndicator(new CopyIndicatorType());
        reqType.getCopyIndicator().setValue(false);
        

        Date now = new Date();
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(now);
       
        try {
            XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar();
            reqType.setIssueDate(new IssueDateType());
            reqType.getIssueDate().setValue(date2.normalize());
            reqType.setIssueTime(new IssueTimeType());
            reqType.getIssueTime().setValue(date2.normalize());
        } catch (DatatypeConfigurationException e) {
            
        }
        
        reqType.setID(SchemaFactory.createISOIECIDType(UUID.randomUUID().toString()));
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
    
}
