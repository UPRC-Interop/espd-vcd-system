package eu.esens.espdvcd.validator.schematron;

import com.helger.schematron.pure.SchematronResourcePure;
import com.helger.schematron.xslt.SchematronResourceSCH;
import eu.esens.espdvcd.codelist.GenericCode;
import eu.esens.espdvcd.validator.ArtifactValidator;
import eu.esens.espdvcd.validator.ValidationResult;
import org.oclc.purl.dsdl.svrl.FailedAssert;
import org.oclc.purl.dsdl.svrl.SchematronOutputType;

import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author konstantinos
 */
public class ESPDSchematronValidator implements ArtifactValidator {

    private static final String ERROR_INVALID_SCHEMATRON = "Invalid Schematron";
    private List<ValidationResult> validationMessages = new LinkedList<>();

    public ESPDSchematronValidator(InputStream is, String schPath) {
        validateXMLViaXSLTSchematronFull(is, schPath);
    }

    public static boolean validateXMLViaXSLTSchematron(InputStream is, String schPath) {
        final SchematronResourceSCH schematron = SchematronResourceSCH.fromClassPath(schPath);
        boolean isValid = false;

        if (!schematron.isValidSchematron()) {
            throw new IllegalArgumentException(ERROR_INVALID_SCHEMATRON);
        }

        try {
            isValid = schematron.getSchematronValidity(new StreamSource(is)).isValid();
        } catch (Exception e) {
            Logger.getLogger(GenericCode.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }

        return isValid;
    }

    private void validateXMLViaXSLTSchematronFull(InputStream is, String schPath) {
        final SchematronResourceSCH schematron = SchematronResourceSCH.fromClassPath(schPath);

        if (!schematron.isValidSchematron()) {
            throw new IllegalArgumentException(ERROR_INVALID_SCHEMATRON);
        }

        try {
            SchematronOutputType svrl = schematron.applySchematronValidationToSVRL(new StreamSource(is));

            svrl.getActivePatternAndFiredRuleAndFailedAssert()
                    .stream()
                    .filter(value -> value instanceof FailedAssert)
                    .map(failedAssertObject -> (FailedAssert) failedAssertObject)
                    .forEach(fa -> validationMessages.add(new ValidationResult.Builder(fa.getId(), fa.getLocation(), fa.getText())
                            .flag(fa.getFlag())
                            .test(fa.getTest())
                            .role(fa.getRole())
                            .build()));
            // Print SVRL
            // new SVRLMarshaller().write(svrl, System.out);
        } catch (Exception e) {
            e.printStackTrace();
            validationMessages.add(new ValidationResult.Builder(String.valueOf(validationMessages.size()),
                    "(line 0, column 0)", e.getMessage()).build());
        }
    }

    @Override
    public boolean isValid() {
        return validationMessages.isEmpty();
    }

    @Override
    public List<ValidationResult> getValidationMessages() {
        return validationMessages;
    }

    @Override
    public List<ValidationResult> getValidationMessagesFiltered(String flag) {
        return validationMessages
                .stream()
                .filter(validationResult -> validationResult.getFlag().contains(flag))
                .collect(Collectors.toList());
    }

}
