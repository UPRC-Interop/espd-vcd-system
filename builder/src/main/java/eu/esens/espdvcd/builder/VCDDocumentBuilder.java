package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.model.ESPDRequest;

import no.difi.asic.AsicWriterFactory;
import no.difi.asic.AsicWriter;
import no.difi.asic.MimeType;
import no.difi.asic.SignatureHelper;
import no.difi.xsd.asic.model._1.AsicManifest;
import org.etsi.uri._02918.v1_2.DataObjectReferenceType;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URL;

/**
 * Created by Ulf Lotzmann on 08/06/2016.
 */
public class VCDDocumentBuilder extends DocumentBuilder {

    private SignatureHelper signatureHelper;

    public VCDDocumentBuilder(ESPDRequest req, SignatureHelper signatureHelper) {
        super(req);
        this.signatureHelper = signatureHelper;
    }

    /**
     * Bundles the XML Representation of the ESPD with attached documents to an ASiC, returned as an input stream.
     *
     * @return the VCD ASiC as an input stream
     */
    @Override
    public InputStream getAsInputStream() {
        return createVCD(super.getAsInputStream());
    }

    private InputStream createVCD(InputStream espdStream) {
        // Creates an ASiC archive

        // Name of the file to hold the the ASiC archive
        File archiveOutputFile = new File(System.getProperty("java.io.tmpdir"), "vcd.asice");

        // Creates an AsicWriterFactory with default signature method
        AsicWriterFactory asicWriterFactory = AsicWriterFactory.newFactory();

        try {
            // Creates the actual container with all the data objects (files) and signs it.
            AsicWriter asicWriter = asicWriterFactory.newContainer(archiveOutputFile)
                 .add(espdStream, "espd.xml", MimeType.forString("application/xml"))
            // Adds an ordinary file, using the file name as the entry name
            //.add(biiEnvelopeFile)
            // Adds another file, explicitly naming the entry and specifying the MIME type
            //.add(biiMessageFile, BII_MESSAGE_XML, MimeType.forString("application/xml"))
            // Signing the contents of the archive, closes it for further changes.
            //.sign(keystoreFile, keyStorePassword(), privateKeyPassword());
            .sign(signatureHelper);
        }
        catch (IOException ex) {
            Logger.getLogger(VCDDocumentBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
        InputStream asic = null;
        try {
            asic = new FileInputStream(archiveOutputFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(VCDDocumentBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }

        return asic;
    }

    /**
     * Writes InputStream to file to be processed by ASiC library.
     * @param in
     * @param file
     */
  /*  private void copyInputStreamToFile( InputStream in, File file ) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

}
