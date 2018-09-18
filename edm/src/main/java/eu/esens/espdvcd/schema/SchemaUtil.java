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
package eu.esens.espdvcd.schema;

//import grow.names.specification.ubl.edm.xsd.espdrequest_1.ESPDRequestType;
//import grow.names.specification.ubl.edm.xsd.espdresponse_1.ESPDResponseType;
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
    public static Marshaller getMarshaller(EDMVersion version) throws JAXBException {

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
                    throw new SchemaException("Error... Unknown edm version.");
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
    public static Unmarshaller getUnmarshaller(EDMVersion version) throws JAXBException {

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
                    throw new SchemaException("Error... Unknown edm version.");
            }

        } catch (SchemaException e) {
            Logger.getLogger(SchemaUtil.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }

        return unmarshaller;
    }

}
