/**
 * Copyright 2016-2018 University of Piraeus Research Center
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
        //System.err.println("----------- tmp dir: " + System.getProperty("java.io.tmpdir"));
        //System.err.println("----------- local evidence URI: " + EvidenceHelper.transformEvidenceURIFromLocalResourceToASiCResource("file:/C:/Users/ULFLOT~1/AppData/Local/Temp/N073_Cover%20Final%20deliverable%20for%20CWA%203456%20eTendering.doc.pdf"));

        InputStream isRes = BuilderESPDTest.class.getResourceAsStream("/artefacts/regulated/v1/espd-response_withEvidenceDocumentReferences.xml");
        Assert.assertNotNull(isRes);

        // create an ESPD request as content for the ASiC
        espd = new VCDModelBuilder().importFrom(isRes).createESPDResponse();
        Assert.assertNotNull(espd);

        // create the SignatureHelper for signing the ASiC
        signature = new SignatureHelper(getKeyStoreFile(), keyStorePassword(), privateKeyPassword());
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
     * temporary method for signing the ASiC - copied from no.difi.asic.TestUtil
     * @return
     */
    private String keyStorePassword() {
        return "changeit";
    }

    /**
     * temporary method for signing the ASiC - copied from no.difi.asic.TestUtil
     * @return
     */
    private String privateKeyPassword() {
        return "changeit";
    }

    /**
     * temporary method for signing the ASiC - copied from no.difi.asic.TestUtil
     * @return
     */
    private String keyPairAlias() {
        return "client_alias";
    }
}
