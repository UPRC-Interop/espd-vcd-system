package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.builder.BuilderFactory;
import eu.esens.espdvcd.designer.exception.ValidationException;
import eu.esens.espdvcd.designer.typeEnum.ArtefactType;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.retriever.exception.RetrieverException;

import java.io.InputStream;

//public class RegulatedModelToESPDV1Service<E> implements ModeltoESPDService {
//
//    @Override
//    public InputStream CreateXMLStreamFromModel(E document) throws ValidationException {
//        //Do validation
//
//        return BuilderFactory.withEDMVersion1().getDocumentBuilderFor(document).getAsInputStream();
//    }
//
//    @Override
//    public String CreateXMLStringFromModel(E document) throws ValidationException {
//        //Do validation
//
//        return BuilderFactory.withEDMVersion1().getDocumentBuilderFor(document).getAsString();
//    }
//}
