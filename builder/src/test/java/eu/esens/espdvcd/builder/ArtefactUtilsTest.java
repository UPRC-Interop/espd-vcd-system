/**
 * Copyright 2016-2020 University of Piraeus Research Center
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *     http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.builder.util.ArtefactUtils;
import eu.esens.espdvcd.codelist.enums.ProfileExecutionIDEnum;
import eu.esens.espdvcd.codelist.enums.QualificationApplicationTypeEnum;
import eu.esens.espdvcd.codelist.enums.internal.ArtefactType;
import eu.esens.espdvcd.schema.enums.EDMSubVersion;
import eu.esens.espdvcd.schema.enums.EDMVersion;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static eu.esens.espdvcd.builder.util.ArtefactUtils.*;

public class ArtefactUtilsTest {

    private InputStream espdRequestRegulatedV1_0_2;
    private InputStream espdRequestRegulatedV2_0_1;
    private InputStream espdResponseRegulatedV1_0_2;
    private InputStream espdResponseRegulatedV2_0_2;

//    private InputStream selfContainedRequestUPRCDA210IS;

    private File regulatedRequestV2;

    private InputStream selfContainedRequestV2IS;
    private File selfContainedRequestV2File;

    @Before
    public void setUp() throws Exception{
        espdRequestRegulatedV1_0_2 = ArtefactUtilsTest.class.getResourceAsStream("/artefacts/regulated/v1/espd-request.xml");
        Assert.assertNotNull(espdRequestRegulatedV1_0_2);
        espdRequestRegulatedV2_0_1 = ArtefactUtilsTest.class.getResourceAsStream("/espd-request-v2_2018-05-30a.xml");
        Assert.assertNotNull(espdRequestRegulatedV2_0_1);
        espdResponseRegulatedV1_0_2 = ArtefactUtilsTest.class.getResourceAsStream("/artefacts/regulated/v1/espd-response.xml");
        Assert.assertNotNull(espdResponseRegulatedV1_0_2);
        espdResponseRegulatedV2_0_2 = ArtefactUtilsTest.class.getResourceAsStream("/artefacts/regulated/v2/2.1.0/REGULATED-ESPD-Response_2.0.2.xml");
        Assert.assertNotNull(espdResponseRegulatedV2_0_2);

        regulatedRequestV2 = new File(getClass().getClassLoader().getResource("artefacts/regulated/v2/2.1.0/REGULATED-ESPD-Request_2.0.2.xml").toURI());
        Assert.assertNotNull(regulatedRequestV2);

        selfContainedRequestV2IS = getClass().getClassLoader().getResourceAsStream("artefacts/selfcontained/da/2.0.2/UPRC-ESPD-Self-Contained-Request-2.0.2-DA-Artefact.xml");
        Assert.assertNotNull(selfContainedRequestV2IS);

        selfContainedRequestV2File = new File(getClass().getClassLoader().getResource("artefacts/selfcontained/da/2.0.2/UPRC-ESPD-Self-Contained-Response-2.0.2-DA-Artefact.xml").toURI());
        Assert.assertNotNull(selfContainedRequestV2File);
    }

    @Test
    public void testFindEDMVersion() {
        Assert.assertEquals(EDMVersion.V2, findEDMVersion(espdRequestRegulatedV2_0_1));
        Assert.assertEquals(EDMVersion.V1, findEDMVersion(espdRequestRegulatedV1_0_2));
    }

    @Test
    public void testFindProfileExecutionID() {
        Assert.assertEquals(ProfileExecutionIDEnum.ESPD_EDM_V201_REGULATED, findProfileExecutionID(espdRequestRegulatedV2_0_1));
        Assert.assertNotEquals(ProfileExecutionIDEnum.ESPD_EDM_V202_REGULATED, findProfileExecutionID(espdRequestRegulatedV1_0_2));
        Assert.assertNotEquals(ProfileExecutionIDEnum.ESPD_EDM_V202_REGULATED, findProfileExecutionID(espdRequestRegulatedV1_0_2));
        Assert.assertEquals(ProfileExecutionIDEnum.ESPD_EDM_V102, findProfileExecutionID(espdRequestRegulatedV1_0_2));
        Assert.assertEquals(ProfileExecutionIDEnum.ESPD_EDM_V102, findProfileExecutionID(espdResponseRegulatedV1_0_2));
        Assert.assertEquals(ProfileExecutionIDEnum.ESPD_EDM_V202_REGULATED, findProfileExecutionID(espdResponseRegulatedV2_0_2));
    }

    @Test
    public void testFindSchemaVersion() {
        Assert.assertEquals(EDMVersion.V2, findEDMVersion(espdRequestRegulatedV2_0_1));
        Assert.assertNotEquals(EDMVersion.V1, findEDMVersion(espdRequestRegulatedV2_0_1));
        Assert.assertEquals(EDMVersion.V1, findEDMVersion(espdRequestRegulatedV1_0_2));
        Assert.assertNotEquals(EDMVersion.V2, findEDMVersion(espdRequestRegulatedV1_0_2));
        Assert.assertNotEquals(EDMVersion.V2, findEDMVersion(espdResponseRegulatedV1_0_2));
        Assert.assertEquals(EDMVersion.V1, findEDMVersion(espdResponseRegulatedV1_0_2));
        Assert.assertEquals(EDMVersion.V2, findEDMVersion(espdResponseRegulatedV2_0_2));
        Assert.assertNotEquals(EDMVersion.V1, findEDMVersion(espdResponseRegulatedV2_0_2));
    }

    @Test
    public void testFindArtefactType() {
        Assert.assertEquals(ArtefactType.ESPD_REQUEST, findArtefactType(espdRequestRegulatedV1_0_2));
        Assert.assertEquals(ArtefactType.ESPD_REQUEST, findArtefactType(espdRequestRegulatedV2_0_1));
        Assert.assertEquals(ArtefactType.ESPD_RESPONSE, findArtefactType(espdResponseRegulatedV1_0_2));
        Assert.assertEquals(ArtefactType.ESPD_RESPONSE, findArtefactType(espdResponseRegulatedV2_0_2));
    }

    @Test
    public void testUseTwoArtefactUtilsMethodsForTheSameStreamRepeatedly() {
        Assert.assertEquals(ProfileExecutionIDEnum.ESPD_EDM_V102, findProfileExecutionID(espdRequestRegulatedV1_0_2));
        Assert.assertEquals(EDMVersion.V1, findEDMVersion(espdRequestRegulatedV1_0_2));
        Assert.assertEquals(ArtefactType.ESPD_REQUEST, findArtefactType(espdRequestRegulatedV1_0_2));
    }

    @Test
    public void testUseTwoArtefactUtilsMethodsForTheSameFileInputStreamRepeatedly() throws Exception {
        Assert.assertEquals(ProfileExecutionIDEnum.ESPD_EDM_V202_REGULATED, findProfileExecutionID(new FileInputStream(regulatedRequestV2)));
        Assert.assertEquals(EDMVersion.V2, findEDMVersion(new FileInputStream(regulatedRequestV2)));
        Assert.assertEquals(ArtefactType.ESPD_REQUEST, findArtefactType(new FileInputStream(regulatedRequestV2)));
    }

    @Test
    public void testFindQualificationApplicationType() {
        Assert.assertEquals(QualificationApplicationTypeEnum.SELFCONTAINED, findQualificationApplicationType(selfContainedRequestV2File));
        Assert.assertEquals(QualificationApplicationTypeEnum.SELFCONTAINED, findQualificationApplicationType(selfContainedRequestV2IS));

        Assert.assertEquals(QualificationApplicationTypeEnum.SELFCONTAINED, findQualificationApplicationType(getClass().getClassLoader()
                .getResourceAsStream("artefacts/selfcontained/da/2.1.0/UPRC-ESPD-Self-Contained-Request-2.1.0-DA-Artefact.xml")));

        Assert.assertEquals(QualificationApplicationTypeEnum.SELFCONTAINED, findQualificationApplicationType(getClass().getClassLoader()
                .getResourceAsStream("artefacts/selfcontained/da/2.1.0/UPRC-ESPD-Self-Contained-Response-2.1.0-DA-Artefact.xml")));

        Assert.assertEquals(QualificationApplicationTypeEnum.SELFCONTAINED, findQualificationApplicationType(getClass().getClassLoader()
                .getResourceAsStream("artefacts/selfcontained/da/2.1.0/UPRC-ESPD-Self-Contained-Request-2.1.0-DA-Artefact-5-12-2018.xml")));

        Assert.assertEquals(QualificationApplicationTypeEnum.SELFCONTAINED, findQualificationApplicationType(getClass().getClassLoader()
                .getResourceAsStream("artefacts/selfcontained/da/2.1.0/UPRC-ESPD-Self-Contained-Response-2.1.0-DA-Artefact-5-12-2018.xml")));

        Assert.assertEquals(QualificationApplicationTypeEnum.REGULATED, findQualificationApplicationType(getClass().getClassLoader()
                .getResourceAsStream("artefacts/regulated/v2/2.1.0/ESPDResponse_DA_Test-2.1.0-v0.5.xml")));
    }

    @Test
    public void testClearAllWhitespaces() {
        String originalString = "Reference\n" +
                "                                                  description";
        Assert.assertEquals("Referencedescription", ArtefactUtils.clearAllWhitespaces(originalString));
    }

    @Test
    public void testFindEDMSubVersionNew() {
        Assert.assertEquals(EDMSubVersion.V201, findEDMSubVersion(espdRequestRegulatedV2_0_1));
        Assert.assertEquals(EDMSubVersion.V102, findEDMSubVersion(espdRequestRegulatedV1_0_2));
        Assert.assertEquals(EDMSubVersion.V202, findEDMSubVersion(espdResponseRegulatedV2_0_2));

        Assert.assertEquals(EDMSubVersion.V210, findEDMSubVersion(getClass().getClassLoader()
                .getResourceAsStream("artefacts/selfcontained/da/2.1.0/UPRC-ESPD-Self-Contained-Response-2.1.0-DA-Artefact-5-12-2018.xml")));
    }

}
