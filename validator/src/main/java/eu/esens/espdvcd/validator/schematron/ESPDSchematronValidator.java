package eu.esens.espdvcd.validator.schematron;

import com.helger.schematron.ISchematronResource;
import com.helger.schematron.pure.SchematronResourcePure;
import eu.esens.espdvcd.validator.ArtifactValidator;
import java.util.List;

/**
 *
 * @author konstantinos
 */
public class ESPDSchematronValidator implements ArtifactValidator {

    private static final String ERROR_INVALID_SCHEMATRON = "Invalid Schematron";
    private String schPath;

    public ESPDSchematronValidator(String schPath) {
        this.schPath = schPath;

        final ISchematronResource schResource = SchematronResourcePure.fromFile(schPath);
        if (!schResource.isValidSchematron()) {
            throw new IllegalArgumentException(ERROR_INVALID_SCHEMATRON);
        }

    }

    private void validateXML() {

    }

    @Override
    public boolean isValid() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> getValidationMessages() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> getValidationMessagesFiltered(String keyWord) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
