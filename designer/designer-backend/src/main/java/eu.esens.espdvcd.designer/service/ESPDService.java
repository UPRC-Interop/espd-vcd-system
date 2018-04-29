package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.designer.exception.ValidationException;
import eu.esens.espdvcd.designer.typeEnum.ArtefactType;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public interface ESPDService {
    Object XMLFileToObjectTransformer(File XML) throws RetrieverException, BuilderException, FileNotFoundException, JAXBException, SAXException, ValidationException;

    InputStream ObjectToXMLStreamTransformer(Object document);

    String ObjectToXMLStringTransformer(Object document);

    ArtefactType getArtefactType();

    default boolean isESPDRequest(File Artefact) {

        Scanner scanner = null;
        try {
            scanner = new Scanner(Artefact);
        } catch (FileNotFoundException e) {
            Logger.getLogger(ESPDService.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
        String line = "";
        int linesToSkip = 2;
        for (int i = 0; i < linesToSkip; i++) {
            if (scanner.hasNextLine())
                line = scanner.nextLine();
        }
        return line.trim().split("\\s+")[0].contains("ESPDRequest");
    }
}
