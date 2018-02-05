package eu.esens.espdvcd.validator.schematron;

import com.helger.commons.io.resource.FileSystemResource;
import com.helger.schematron.ISchematronResource;
import com.helger.schematron.pure.SchematronResourcePure;
import com.helger.schematron.pure.errorhandler.CollectingPSErrorHandler;
import com.helger.schematron.pure.errorhandler.DoNothingPSErrorHandler;
import com.helger.schematron.pure.errorhandler.IPSErrorHandler;
import com.helger.schematron.pure.errorhandler.LoggingPSErrorHandler;
import com.helger.schematron.pure.exchange.PSReader;
import com.helger.schematron.pure.exchange.SchematronReadException;
import com.helger.schematron.pure.model.PSSchema;
import eu.esens.espdvcd.validator.ArtifactValidator;

import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author konstantinos
 */
public class ESPDSchematronValidator implements ArtifactValidator {

    private static final String ERROR_INVALID_SCHEMATRON = "Invalid Schematron";
    private List<String> validationMessages = new LinkedList<>();

    public ESPDSchematronValidator(InputStream is, String schPath) {
        validateXML(is, schPath);
    }

    public ESPDSchematronValidator(InputStream is, String... schPath) {
        validateXML(is, schPath);
    }

    // FIXME: this method does not work properly
    private void validateXML(InputStream is, String... schPath) {
        // Schematron Pure
        final List<SchematronResourcePure> schematronList = Arrays.asList(schPath)
                .stream()
                .map(path -> SchematronResourcePure.fromFile(path))
                .collect(Collectors.toList());
        // error handler
        schematronList
                .stream()
                .forEach(schematron -> schematron.setErrorHandler(new LoggingPSErrorHandler()));

        schematronList.stream().filter(schematron -> !schematron.isValidSchematron()).forEach(schematron -> {
            throw new IllegalArgumentException(ERROR_INVALID_SCHEMATRON);
        });

//        schematronList.stream().forEach(schematron -> {
//            try {
//                schematron.getSchematronValidity(new StreamSource(is)).isValid();
//            } catch (Exception e) {
//                validationMessages.add(e.getMessage());
//            }
//        });
    }

    private void validateXML(InputStream is, String schPath) {
        // Schematron Pure
        final SchematronResourcePure schematron = SchematronResourcePure.fromFile(schPath);
        schematron.setErrorHandler(new LoggingPSErrorHandler());
        // final ISchematronResource schematron = SchematronResourcePure.fromFile(schPath);

        if (!schematron.isValidSchematron()) {
            throw new IllegalArgumentException(ERROR_INVALID_SCHEMATRON);
        }

        try {
            schematron.getSchematronValidity(new StreamSource(is)).isValid();
        } catch (Exception e) {
            validationMessages.add(e.getMessage());
        }
    }

    @Override
    public boolean isValid() {
        return validationMessages.isEmpty();
    }

    @Override
    public List<String> getValidationMessages() {
        return validationMessages;
    }

    @Override
    public List<String> getValidationMessagesFiltered(String keyWord) {
        return validationMessages.stream().filter(s -> s.contains(keyWord)).collect(Collectors.toList());
    }

}
