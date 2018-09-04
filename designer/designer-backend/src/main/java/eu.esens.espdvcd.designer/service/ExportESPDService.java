package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.designer.exception.ValidationException;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.ESPDResponse;
import eu.esens.espdvcd.model.SelectableCriterion;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;

public interface ExportESPDService {
    InputStream exportESPDRequestAsInputStream(ESPDRequest model) throws ValidationException;

    String exportESPDRequestAsString(ESPDRequest model) throws ValidationException;

    InputStream exportESPDResponseAsInputStream(ESPDResponse model) throws ValidationException;

    String exportESPDResponseAsString(ESPDResponse model) throws ValidationException;

    default boolean hasNullCriterion(final List<SelectableCriterion> criteria) {
        return criteria.stream()
                .anyMatch(Objects::isNull);
    }
}
