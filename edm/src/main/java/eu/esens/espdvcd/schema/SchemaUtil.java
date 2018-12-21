/**
 * Copyright 2016-2018 University of Piraeus Research Center
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.schema;

import eu.esens.espdvcd.schema.enums.EDMSubVersion;
import eu.esens.espdvcd.schema.enums.EDMVersion;
import eu.espd.schema.v1.espdrequest_1.ESPDRequestType;
import eu.espd.schema.v1.espdresponse_1.ESPDResponseType;

import javax.xml.bind.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ESPD/VCD Schema Utility class. Provides factory methods for getting marshallers and unmarshallers for the ESPD
 * and VCD Objects.
 */
public class SchemaUtil {

    private static final Logger LOGGER = Logger.getLogger(SchemaUtil.class.getName());

    private static final JAXBContext JCV102; // 1.0.2
    private static final JAXBContext JCV202; // 2.0.2
    private static final JAXBContext JCV210; // 2.1.0

    static {
        try {

            JCV102 = JAXBContext.newInstance(ESPDRequestType.class.getPackage().getName()
                    + ":" + ESPDResponseType.class.getPackage().getName());

            JCV202 = JAXBContext.newInstance(eu.espd.schema.v2.v202.pre_award.qualificationapplicationrequest
                    .QualificationApplicationRequestType.class.getPackage().getName()
                    + ":" + eu.espd.schema.v2.v202.pre_award.qualificationapplicationresponse
                    .QualificationApplicationResponseType.class.getPackage().getName());

            JCV210 = JAXBContext.newInstance(eu.espd.schema.v2.v210.qualificationapplicationrequest
                    .QualificationApplicationRequestType.class.getPackage().getName()
                    + ":" + eu.espd.schema.v2.v210.qualificationapplicationresponse
                    .QualificationApplicationResponseType.class.getPackage().getName());

        } catch (JAXBException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Factory Method that gets a proper marshaller for the ESPD/VCD Artifacts
     *
     * @return an ESPD/VCD Marshaller
     * @throws JAXBException when the marshaller cannot be initialized
     */
    public static Marshaller getMarshaller(EDMVersion version) throws JAXBException {

        switch (version) {

            case V1:

                return getMarshaller(EDMSubVersion.V102);

            case V2:

                return getMarshaller(EDMSubVersion.V210);

            default:
                throw new IllegalStateException("Error... Unknown edm version.");
        }
    }

    /**
     * Factory Method that gets a proper marshaller for the ESPD/VCD Artifacts
     *
     * @return an ESPD/VCD Marshaller
     * @throws JAXBException when the marshaller cannot be initialized
     */
    public static Marshaller getMarshaller(EDMSubVersion version) throws JAXBException {

        Marshaller marshaller = null;

        try {

            switch (version) {

                case V102:

                    marshaller = JCV102.createMarshaller();
                    marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new ESPDPrefixMapper());
                    break;

                case V202:

                    marshaller = JCV202.createMarshaller();
                    marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new ESPDPrefixMapper());
                    break;

                case V210:

                    marshaller = JCV210.createMarshaller();
                    marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new ESPDPrefixMapper());
                    break;

                default:
                    throw new IllegalStateException("Error... Unknown edm version.");
            }

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        } catch (PropertyException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return marshaller;
    }

    /**
     * Factory Method that gets a proper unmarshaller for the ESPD/VCD Artifacts
     *
     * @return an ESPD/VCD Marshaller
     * @throws JAXBException when the marshaller cannot be initialized
     */
    public static Unmarshaller getUnmarshaller(EDMVersion version) throws JAXBException {

        switch (version) {

            case V1:

                return getUnmarshaller(EDMSubVersion.V102);

            case V2:

                return getUnmarshaller(EDMSubVersion.V210);

            default:
                throw new IllegalStateException("Error... Unknown edm version.");
        }

    }

    /**
     * Factory Method that gets a proper unmarshaller for the ESPD/VCD Artifacts
     *
     * @return an ESPD/VCD Marshaller
     * @throws JAXBException when the marshaller cannot be initialized
     */
    public static Unmarshaller getUnmarshaller(EDMSubVersion version) throws JAXBException {

        switch (version) {

            case V102:

                return JCV102.createUnmarshaller();

            case V202:

                return JCV202.createUnmarshaller();

            case V210:

                return JCV210.createUnmarshaller();

            default:
                throw new IllegalStateException("Error... Unknown edm version.");
        }

    }

}
