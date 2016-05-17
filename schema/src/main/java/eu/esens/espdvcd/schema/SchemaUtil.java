package eu.esens.espdvcd.schema;

import grow.names.specification.ubl.schema.xsd.espdrequest_1.ESPDRequestType;
import grow.names.specification.ubl.schema.xsd.espdresponse_1.ESPDResponseType;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

/**
 *ESPD/VCD Schema Utility class. Provides factory methods for getting 
 * marshallers and unmarshallers for the ESPD and VCD Objects.
 */
public class SchemaUtil {

    private static final JAXBContext JC;
    
    static {
        try {
            JC = JAXBContext.newInstance(ESPDRequestType.class.getPackage().getName()+":"+ESPDResponseType.class.getPackage().getName());
        } catch (JAXBException ex) {
            Logger.getLogger(SchemaUtil.class.getName()).log(Level.SEVERE, null, ex);
            throw new ExceptionInInitializerError(ex);
        }
    }   
        
    /**
     * Factory Method that gets a proper marshaller for the ESPD/VCD Artifacts
     * @return an ESPD/VCD Marshaller
     * @throws JAXBException when the marshaller cannot be initialized
     */
    public static Marshaller getMarshaller() throws JAXBException {

        Marshaller marshaller = JC.createMarshaller();
         try {
             marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper",new ESPDPrefixMapper());
             marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        } catch( PropertyException e ) {
        }
        return marshaller;
    }
    
    /**
     * Factory Method that gets a proper unmarshaller for the ESPD/VCD Artifacts
     * @return an ESPD/VCD Marshaller
     * @throws JAXBException when the marshaller cannot be initialized
     */
    public static Unmarshaller getUnmarshaller() throws JAXBException {

        Unmarshaller unmarshaller = JC.createUnmarshaller();
        return unmarshaller;
        
    }
    
}
