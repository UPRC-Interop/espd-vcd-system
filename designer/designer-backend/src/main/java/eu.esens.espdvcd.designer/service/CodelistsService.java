package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.codelist.Codelists;
import eu.esens.espdvcd.codelist.CodelistsV1;
import eu.esens.espdvcd.designer.util.CodelistItem;

import java.util.List;

public interface CodelistsService {
    List<CodelistItem> getCodelist(String codelist) throws IllegalArgumentException;

    List<CodelistItem> getTranslatedCodelist(String codelist, String language) throws IllegalArgumentException;

    Codelists[] getAvailableCodelists();
}