package eu.esens.espdvcd.designer.services;

import eu.esens.espdvcd.designer.models.CodelistItem;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CodelistsService {
    List<CodelistItem> getCodelist(String codelist) throws IllegalArgumentException;
    List<CodelistItem> getTranslatedCodelist(String codelist, String language) throws IllegalArgumentException;
}