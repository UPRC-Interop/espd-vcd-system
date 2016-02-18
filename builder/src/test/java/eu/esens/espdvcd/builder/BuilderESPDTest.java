
package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.model.ESPDRequest;
import grow.names.specification.ubl.schema.xsd.espdrequest_1.ESPDRequestType;
import java.io.InputStream;
import javax.xml.bind.JAXB;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Jerry Dimitriou <jerouris@unipi.gr>
 */
public class BuilderESPDTest {
    
    InputStream is;
    public BuilderESPDTest() {
    }
    
    @Before
    public void setUp() {
        is = BuilderESPDTest.class.getResourceAsStream("/espd-request.xml");
        Assert.assertNotNull(is);
    }

    @Test
    public void createESPDRequestFromFile() throws Exception {
        
        ESPDBuilder espdBuiler = new ESPDBuilder();
        ESPDRequest espd = espdBuiler.createFromXML(is);
        System.out.println(espdBuiler.createXML(espd));
    }
    
}
