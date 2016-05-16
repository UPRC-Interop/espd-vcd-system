package eu.esens.espdvcd.builder;

import com.sun.corba.se.impl.io.IIOPInputStream;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.ESPDResponse;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ESPDXMLDocumentBuilder {
    
    public ESPDXMLDocumentBuilder() {};
    
    public ESPDXMLDocumentBuilder createXMLFrom(ESPDRequest req) {
        return this;
    }
    
    public ESPDXMLDocumentBuilder createXMLFrom(ESPDResponse res) {
        return this;
        
    }
    
    
        
}
