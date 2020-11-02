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