package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.designer.model.CodelistItem;

import java.util.List;

public interface CodelistsService {
    List<CodelistItem> getCodelist(String codelist) throws IllegalArgumentException;

    List<CodelistItem> getTranslatedCodelist(String codelist, String language) throws IllegalArgumentException;
}