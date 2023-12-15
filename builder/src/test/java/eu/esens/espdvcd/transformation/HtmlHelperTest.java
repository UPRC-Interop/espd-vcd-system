package eu.esens.espdvcd.transformation;

import eu.esens.espdvcd.codelist.enums.EULanguageCodeEnum;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.ESPDResponse;
import freemarker.template.TemplateException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class HtmlHelperTest {
    private final TransformationService transformationService = new TransformationService();

    /**
     * Generates an ESPD Request to HTML document
     * @param target is the target path.
     * @param espdRequest is the ESPD Request Model.
     * @param lang is the selected language for HTML document.
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws TemplateException
     */
    private void generateHtml(Path target, ESPDRequest espdRequest, EULanguageCodeEnum lang) throws IOException,
            ParserConfigurationException,SAXException,TemplateException  {
        try (OutputStream os = new BufferedOutputStream(Files.newOutputStream(target))) {
            transformationService.createHtml(espdRequest, os, lang);
        }
    }

    /**
     * Generates an ESPD Response to HTML document
     * @param target is the target path.
     * @param espdResponse is the ESPD Response Model.
     * @param lang is the selected language for HTML document.
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws TemplateException
     */
    private void generateHtml(Path target, ESPDResponse espdResponse, EULanguageCodeEnum lang) throws IOException,
            ParserConfigurationException,SAXException,TemplateException  {
        try (OutputStream os = new BufferedOutputStream(Files.newOutputStream(target))) {
            transformationService.createHtml(espdResponse, os, lang);
        }
    }

    /**
     * Generates the template of HTML ESPD Request document.
     * @param requestContent is the ESPD Request model.
     * @param lang is the selected language for HTML document.
     * @throws IOException
     * @throws TemplateException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @return java.nio.file.Path
     */
    public Path generateTempHtml(ESPDRequest requestContent, EULanguageCodeEnum lang) throws IOException,
            ParserConfigurationException,SAXException,TemplateException {
        final Path target = Files.createTempFile("testfile", ".html");
        generateHtml(target, requestContent, lang);
        return target;
    }

    /**
     * Generates the template of HTML ESPD Response document.
     * @param responseContent is the ESPD Response model.
     * @param lang is the selected language for HTML document.
     * @throws IOException
     * @throws TemplateException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @return java.nio.file.Path
     */
    public Path generateTempHtml(ESPDResponse responseContent, EULanguageCodeEnum lang) throws IOException,
            ParserConfigurationException,SAXException,TemplateException {
        final Path target = Files.createTempFile("testfile", ".html");
        generateHtml(target, responseContent, lang);
        return target;
    }


}