package eu.esens.espdvcd.schema;

//import grow.names.specification.ubl.schema.xsd.espdrequest_1.ESPDRequestType;
//import grow.names.specification.ubl.schema.xsd.espdresponse_1.ESPDResponseType;
//
//import test.x.ubl.pre_award.qualificationapplicationrequest.QualificationApplicationRequestType;
//import test.x.ubl.pre_award.qualificationapplicationresponse.QualificationApplicationResponseType;

import eu.esens.espdvcd.schema.exception.SchemaException;
import eu.espd.schema.v1.espdrequest_1.ESPDRequestType;
import eu.espd.schema.v1.espdresponse_1.ESPDResponseType;
import eu.espd.schema.v2.pre_award.qualificationapplicationrequest.QualificationApplicationRequestType;
import eu.espd.schema.v2.pre_award.qualificationapplicationresponse.QualificationApplicationResponseType;

import javax.xml.bind.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ESPD/VCD Schema Utility class. Provides factory methods for getting marshallers and unmarshallers for the ESPD
 * and VCD Objects.
 */
public class SchemaUtil {

    private static final JAXBContext JCV1;
    private static final JAXBContext JCV2;

    static {
        try {
            JCV1 = JAXBContext.newInstance(ESPDRequestType.class.getPackage().getName()
                    + ":" + ESPDResponseType.class.getPackage().getName());

            JCV2 = JAXBContext.newInstance(QualificationApplicationRequestType.class.getPackage().getName()
                    + ":" + QualificationApplicationResponseType.class.getPackage().getName());
        } catch (JAXBException ex) {
            Logger.getLogger(SchemaUtil.class.getName()).log(Level.SEVERE, null, ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Factory Method that gets a proper marshaller for the ESPD/VCD Artifacts
     *
     * @return an ESPD/VCD Marshaller
     * @throws JAXBException when the marshaller cannot be initialized
     */
    public static Marshaller getMarshaller(SchemaVersion version) throws JAXBException {

        Marshaller marshaller = null;

        try {

            switch (version) {
                case V1:
                    marshaller = JCV1.createMarshaller();
                    break;
                case V2:
                    marshaller = JCV2.createMarshaller();
                    break;
                default:
                    throw new SchemaException("Error... Unknown schema version.");
            }

            marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new ESPDPrefixMapper());
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        } catch (PropertyException | SchemaException e) {
            Logger.getLogger(SchemaUtil.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
        return marshaller;
    }

    /**
     * Factory Method that gets a proper unmarshaller for the ESPD/VCD Artifacts
     *
     * @return an ESPD/VCD Marshaller
     * @throws JAXBException when the marshaller cannot be initialized
     */
    public static Unmarshaller getUnmarshaller(SchemaVersion version) throws JAXBException {

        Unmarshaller unmarshaller = null;

        try {

            switch (version) {
                case V1:
                    unmarshaller = JCV1.createUnmarshaller();
                    break;
                case V2:
                    unmarshaller = JCV2.createUnmarshaller();
                    break;
                default:
                    throw new SchemaException("Error... Unknown schema version.");
            }

        } catch (SchemaException e) {
            Logger.getLogger(SchemaUtil.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }

        return unmarshaller;
    }

}
