package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.designer.exception.ValidationException;
import eu.esens.espdvcd.designer.util.DocumentDetails;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.List;

public interface ImportESPDService<T extends ESPDRequest> {
    T importESPDFile(File XML) throws RetrieverException, BuilderException, JAXBException, SAXException, ValidationException, IOException;
    DocumentDetails getDocumentDetails(File XML);

    default void generateUUIDs(T espdDocument){
        int counter = 0;
        for (SelectableCriterion cr : espdDocument.getFullCriterionList()) {
            cr.setUUID(cr.getID());
            generateUUIDSForAllRequirementGroups(cr.getRequirementGroups(), counter);
        }
    }

    default void generateUUIDSForAllRequirementGroups(List<RequirementGroup> reqGroups, int counter) {
        for (RequirementGroup reqGroup : reqGroups) {
            counter++;
            reqGroup.setUUID(String.format("%s-%d", reqGroup.getID(), counter));
            generateUUIDSForAllRequirementGroups(reqGroup.getRequirementGroups(), counter);
            List<Requirement> reqs = reqGroup.getRequirements();
            for (Requirement req : reqs) {
                counter++;
                req.setUUID(String.format("%s-%d", req.getID(), counter));
            }
        }
    }
}