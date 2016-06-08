package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.model.ESPDResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import no.difi.asic.SignatureHelper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by Ulf Lotzmann on 08/06/2016.
 */
public class VCDDocumentBuilderTest {

    ESPDResponse espd;
    SignatureHelper signature;

    public VCDDocumentBuilderTest()  {
    }

    @Before
    public void setUp() throws BuilderException, IOException {
        System.err.println("----------- tmp dir: " + System.getProperty("java.io.tmpdir"));

        InputStream isRes = BuilderESPDTest.class.getResourceAsStream("/espd-response.xml");
        Assert.assertNotNull(isRes);

        // create an ESPD request as content for the ASiC
        espd = new ModelBuilder().importFrom(isRes).createESPDResponse();
        Assert.assertNotNull(espd);

        // create the SignatureHelper for signing the ASiC
        File keystoreFile = getKeyStoreFile();
        signature = new SignatureHelper(keystoreFile, keyStorePassword(), privateKeyPassword());
    }

    @Test
    public void testVCDBuilderWithESPDResponse() {
        VCDDocumentBuilder db = new VCDDocumentBuilder(espd, signature);
        InputStream asic = db.getAsInputStream();
        Assert.assertNotNull(asic);
    }



    private static final String KEY_STORE_RESOURCE_NAME = "kontaktinfo-client-test.jks";

    /**
     * temporary method for signing the ASiC - copied from no.difi.asic.TestUtil
     * @return key store file
     */
   private File getKeyStoreFile() throws IllegalStateException {

        URL keyStoreResourceURL = this.getClass().getClassLoader().getResource(KEY_STORE_RESOURCE_NAME);
        try {
            URI uri = keyStoreResourceURL.toURI();

            File file = new File(uri);
            if (!file.canRead()) {
                throw new IllegalStateException("Unable to locate " + KEY_STORE_RESOURCE_NAME + " in class path");
            }
            return file;

        } catch (URISyntaxException e) {
            throw new IllegalStateException("Unable to convert URL of keystore " + KEY_STORE_RESOURCE_NAME + " into a URI");
        }
    }

    /**
     * FIXME: temporary method for signing the ASiC - copied from no.difi.asic.TestUtil
     * @return
     */
    private String keyStorePassword() {
        return "changeit";
    }

    /**
     * FIXME: temporary method for signing the ASiC - copied from no.difi.asic.TestUtil
     * @return
     */
    private String privateKeyPassword() {
        return "changeit";
    }

    /**
     * FIXME: temporary method for signing the ASiC - copied from no.difi.asic.TestUtil
     * @return
     */
    private String keyPairAlias() {
        return "client_alias";
    }
}
