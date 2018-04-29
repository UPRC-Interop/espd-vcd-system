package eu.esens.espdvcd.designer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import eu.esens.espdvcd.builder.BuilderFactory;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.ESPDResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class RegulatedESPDResponseV1ServiceTest {

    File espdRequestFile;
    File espdResponseFile;
    ESPDRequest request;
    ESPDResponse response;
    ESPDService service;
    ObjectWriter writer;

    @Before
    public void setUp() throws Exception {
        espdRequestFile = new File(RegulatedESPDResponseV1ServiceTest.class.getResource("/espd-request.xml").toURI());
        Assert.assertNotNull(espdRequestFile);

        espdResponseFile = new File(RegulatedESPDResponseV1ServiceTest.class.getResource("/espd-response.xml").toURI());
        Assert.assertNotNull(espdResponseFile);

        request = BuilderFactory.V1.getModelBuilder().importFrom
                (RegulatedESPDResponseV1ServiceTest.class.getResourceAsStream("/espd-request.xml")).createRegulatedESPDRequest();
        Assert.assertNotNull(request);

        response = BuilderFactory.V1.getModelBuilder().importFrom
                (RegulatedESPDResponseV1ServiceTest.class.getResourceAsStream("/espd-response.xml")).createRegulatedESPDResponse();
        Assert.assertNotNull(response);

        service = new RegulatedESPDResponseV1Service();
        writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    @Test
    public void responseJSONFromRequestTest() throws Exception {
        ESPDResponse response = (ESPDResponse) service.XMLFileToObjectTransformer(espdRequestFile);
        Assert.assertNotNull(response);
//        System.out.print(writer.writeValueAsString(response));
    }

    @Test
    public void responseJSONFromResponseTest() throws Exception{
        ESPDResponse response = (ESPDResponse) service.XMLFileToObjectTransformer(espdResponseFile);
        Assert.assertNotNull(response);
//        System.out.print(writer.writeValueAsString(response));
    }

    @Test
    public void XMLStreamFromResponse() throws Exception{
        InputStream is = service.ObjectToXMLStreamTransformer(response);
        Assert.assertNotNull(is);
//        System.out.print(new BufferedReader( new InputStreamReader(is) ).lines().parallel().collect(Collectors.joining("\n")));
    }

    @Test
    public void XMLStringFromResponse() throws Exception{
        String is = service.ObjectToXMLStringTransformer(response);
        Assert.assertNotNull(is);
//        System.out.print(is);
    }

}