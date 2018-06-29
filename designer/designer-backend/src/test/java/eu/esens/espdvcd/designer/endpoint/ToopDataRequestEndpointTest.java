package eu.esens.espdvcd.designer.endpoint;

import eu.esens.espdvcd.designer.model.ToopDataRequest;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ToopDataRequestEndpointTest {

    @Test
    public void testToopDataRequest() throws IOException {
        ToopDataRequest x = new ToopDataRequest("DE/GB/45452312Z","SE");
        ToopDataRequestEndpoint.createRequestAndSendToToopConnector(x);
    }

}