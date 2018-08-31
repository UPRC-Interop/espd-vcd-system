package eu.esens.espdvcd.validator;

import java.util.List;

/**
 * Generic edm validator interface.
 *
 * Created by Ulf Lotzmann on 11/05/2016.
 */
public interface ArtefactValidator {

    /**
     * Provides validation result.
     * @return true, if validation was successful
     */
    boolean isValid();

    /**
     * Provides list of validation events.
     * @return list of events where validation was not successful
     */
    List<ValidationResult> getValidationMessages();

    /**
     * Provides filtered list of validation events.
     * @param keyWord, for which the list entries are filtered
     * @return filtered list of validation events
     */
    List<ValidationResult> getValidationMessagesFiltered(String keyWord);

}
