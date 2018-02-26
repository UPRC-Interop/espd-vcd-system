package eu.esens.espdvcd.validator.schematron;

import com.helger.schematron.xslt.SchematronResourceSCH;
import eu.esens.espdvcd.validator.ArtifactValidator;
import eu.esens.espdvcd.validator.ValidationResult;
import org.oclc.purl.dsdl.svrl.FailedAssert;
import org.oclc.purl.dsdl.svrl.SchematronOutputType;

import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author konstantinos
 */
public class ESPDSchematronValidator implements ArtifactValidator {

    private static final String ERROR_INVALID_SCHEMATRON = "Error... Invalid Schematron";
    private List<ValidationResult> validationMessages = new LinkedList<>();

    public ESPDSchematronValidator(InputStream is, String schPath) {
        validateXMLViaXSLTSchematronFull(is, schPath);
    }

    public ESPDSchematronValidator(File artifact, String... schPath) {
        Arrays.asList(schPath).forEach(path -> validateXMLViaXSLTSchematronFull(artifact, path));
    }

    static boolean validateXMLViaXSLTSchematron(InputStream is, String schPath) {
        final SchematronResourceSCH schematron = SchematronResourceSCH.fromClassPath(schPath);
        final Map <String, Object> aParams = new HashMap<>();
        aParams.put ("allow-foreign", "true");
        schematron.setParameters (aParams);
        boolean isValid = false;

        if (!schematron.isValidSchematron()) {
            throw new IllegalArgumentException(ERROR_INVALID_SCHEMATRON);
        }

        try {
            isValid = schematron.getSchematronValidity(new StreamSource(is)).isValid();
        } catch (Exception e) {
            Logger.getLogger(ESPDSchematronValidator.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }

        return isValid;
    }

    /**
     * Validating given file against the specified schematron
     *
     * @param artifact the espd request/response artifact provided by the specified file
     * @param schPath the schematron file path
     */
    private void validateXMLViaXSLTSchematronFull(File artifact, String schPath) {
        try {
            validateXMLViaXSLTSchematronFull(new FileInputStream(artifact), schPath);
        } catch (FileNotFoundException e) {
            Logger.getLogger(ESPDSchematronValidator.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * Validating given input stream against the specified schematron
     *
     * @param is the espd request/response artifact provided by the specified input stream
     * @param schPath the schematron file path
     */
    private void validateXMLViaXSLTSchematronFull(InputStream is, String schPath) {
        final SchematronResourceSCH schematron = SchematronResourceSCH.fromClassPath(schPath);

        final Map <String, Object> aParams = new HashMap<>();
        aParams.put ("allow-foreign", "true");
        schematron.setParameters (aParams);

        if (!schematron.isValidSchematron()) {
            throw new IllegalArgumentException(ERROR_INVALID_SCHEMATRON);
        }

        try {
            SchematronOutputType svrl = schematron.applySchematronValidationToSVRL(new StreamSource(is));

            svrl.getActivePatternAndFiredRuleAndFailedAssert()
                    .stream()
                    .filter(value -> value instanceof FailedAssert) // discard all others and keep FailedAssert objects
                    .map(failedAssertObject -> (FailedAssert) failedAssertObject) // convert the object stream to FailedAssert stream
                    // loop through all and create for each one a new validation result
                    .forEach(fa -> validationMessages.add(new ValidationResult.Builder(fa.getId(), fa.getLocation(), fa.getText())
                            .flag(fa.getFlag())
                            .test(fa.getTest())
                            .role(fa.getRole())
                            .build()));
            // Print SVRL
            // new SVRLMarshaller().write(svrl, System.out);
        } catch (Exception e) {
            e.printStackTrace();
            /* @TODO should flag value be fatal here */
            validationMessages.add(new ValidationResult.Builder(String.valueOf(validationMessages.size()),
                    "(line 0, column 0)", e.getMessage())
                    .flag("fatal")
                    .build());
        }
    }



    /**
     * Provides validation result.
     *
     * @return true, if validation was successful
     */
    @Override
    public boolean isValid() {
        return validationMessages.isEmpty();
    }

    /**
     * Provides list of validation events.
     *
     * @return list of events where validation was not successful; empty, if validation was successful
     */
    @Override
    public List<ValidationResult> getValidationMessages() {
        return validationMessages;
    }

    /**
     * Provides filtered list of validation events.
     *
     * @param flag, for which the list entries are filtered
     * @return filtered list of validation events
     */
    @Override
    public List<ValidationResult> getValidationMessagesFiltered(String flag) {
        return validationMessages
                .stream()
                .filter(validationResult -> Optional.ofNullable(validationResult.getFlag()).equals(flag))
                .collect(Collectors.toList());
    }

}
