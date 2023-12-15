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
package eu.esens.espdvcd.designer.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AppConfigTest {

    private AppConfig theConfig;

    @Before
    public void setUp(){
        theConfig = AppConfig.getInstance();
    }

    @Test
    public void isDebugEnabled() {
        assertTrue(theConfig.isDebugEnabled());
    }

    @Test
    public void getServerPort() {
        assertEquals(8080, theConfig.getServerPort());
    }

    @Test
    public void isValidatorEnabled() {
        assertFalse(theConfig.isValidatorEnabled());
    }

    @Test
    public void isArtefactDumpingEnabled() {
        assertTrue(theConfig.isArtefactDumpingEnabled());
    }

    @Test
    public void dumpIncomingArtefactsLocation() {
        assertEquals("artefacts", theConfig.dumpIncomingArtefactsLocation());
    }

    @Test
    public void isCORSEnabled() {
        assertTrue(theConfig.isCORSEnabled());
    }

    @Test
    public void isEnchancedSecurityEnabled() {
        assertFalse(theConfig.isEnchancedSecurityEnabled());
    }

    @Test
    public void isFramingAllowed() {
        assertTrue(theConfig.isFramingAllowed());
    }

    @Test
    public void isSelectionCriteriaPreselected() {
        assertTrue(theConfig.isSelectionCriteriaPreselected());
    }

    @Test
    public void getMaxFileSize() {
        assertEquals(4, theConfig.getMaxFileSize());
    }
}
