package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.SelectableCriterion;
import grow.names.specification.ubl.schema.xsd.espdrequest_1.ESPDRequestType;
import grow.names.specification.ubl.schema.xsd.espdrequest_1.ObjectFactory;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;
import javax.xml.bind.JAXB;

/**
 *
 * @author Jerry Dimitriou <jerouris@unipi.gr>
 */
public class ESPDBuilder {

    public ESPDRequest createFromXML(InputStream xmlESPD) throws Exception {

        // Check and read the file in the JAXB Object
        ESPDRequestType reqType = read(xmlESPD);

        // Create the Model Object
        ESPDRequest req = ModelFactory.extractESPDRequest(reqType); 
        
        return req;

    }

    public ESPDRequestType createXML(ESPDRequest req) {

        ESPDRequestType reqType = SchemaFactory.extractESPDRequestType(req);
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
        CriteriaExtractor cr = new ESPDCriteriaExtractor();
        return cr.getFullList();
    }

    private ESPDRequestType read(InputStream is) {
        // Start with the convience methods provided by JAXB. If there are
        // perfomance issues we will swicth back to the JAXB API Usage
        ESPDRequestType er = JAXB.unmarshal(is, ESPDRequestType.class);
        return er;
    }
    
}
