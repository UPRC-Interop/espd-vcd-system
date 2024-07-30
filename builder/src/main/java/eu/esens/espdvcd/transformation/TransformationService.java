/**
 * Copyright 2016-2020 University of Piraeus Research Center
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *     http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.transformation;

import com.openhtmltopdf.pdfboxout.PdfBoxRenderer;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import eu.esens.espdvcd.codelist.enums.EULanguageCodeEnum;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.ESPDResponse;
import freemarker.ext.dom.NodeModel;
import freemarker.template.*;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


public class TransformationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransformationService.class);
    private static final String FTL_PATH = "/eu/esens/espdvcd/transformation/";
//    private static final String FREE_SANS_PATH = "eu/esens/espdvcd/transformation/";
//    public static final String FREE_SANS = "Free Sans";
    private static final String ESPD_PROP_DOC = "properties/ESPD_";
    private static final String ESPD_EN_PROP_DOC = "properties/ESPD_en";
    private static final String XML = ".xml";
    private static final String LOGO_PATH = "images/PromitheusESPDint_logo.png";

    private static final String FONT_AWESOME_CSS_PATH = Paths.get("src/main/resources/eu/esens/espdvcd/transformation/css/font-awesome.css").toAbsolutePath().toUri().toString();

    private final String FREE_SANS_FONT_PATH = "/eu/esens/espdvcd/transformation/FreeSans.ttf";
    private final File FREE_SANS_FONT_FILE;

    private final String FONT_AWESOME_FONT_PATH = "/eu/esens/espdvcd/transformation/fonts/fontawesome-webfont.ttf";
    private final File FONT_AWESOME_FONT_FILE;

    public TransformationService() {
        try (InputStream isFreeSansFont = TransformationService.class.getResourceAsStream(FREE_SANS_FONT_PATH);
             InputStream isFontAwesomeFont = TransformationService.class.getResourceAsStream(FONT_AWESOME_FONT_PATH)) {
            if (isFreeSansFont == null) {
                throw new IllegalStateException("Failed to properly load the free sans font resource");
            }
            Path tempFreeSansFile = Files.createTempFile("free-sans-font-file", "ttf");
            byte[] bFreeSansFont = isFreeSansFont.readAllBytes();
            Files.write(tempFreeSansFile, bFreeSansFont, StandardOpenOption.CREATE);
            FREE_SANS_FONT_FILE = tempFreeSansFile.toFile();

            if (isFontAwesomeFont == null) {
                throw new IllegalStateException("Failed to properly load the font awesome font resource");
            }
            Path tempFontAwesomeFile = Files.createTempFile("font-awesome-font-file", "ttf");
            byte[] bFontAwesomeFont = isFontAwesomeFont.readAllBytes();
            Files.write(tempFontAwesomeFile, bFontAwesomeFont, StandardOpenOption.CREATE);
            FONT_AWESOME_FONT_FILE = tempFontAwesomeFile.toFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Transforms an ESPD Request model to W3C document.
     * @param espdRequestModel is the ESPD Request Model.
     * @param lang is the selected language for HTML or PDF document.
     * @throws IOException
     * @throws TemplateException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @return  org.w3c.dom.Document
     * */
    public Document transformToW3CDoc(ESPDRequest espdRequestModel,EULanguageCodeEnum lang) throws IOException,
            TemplateException, ParserConfigurationException, SAXException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_30);
        cfg.setClassForTemplateLoading(TransformationService.class,FTL_PATH);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        Template temp = cfg.getTemplate("espd_document.ftlh");

        /* ftlModel contains ESPD Request model, ESPD Properties model and type of model(ESPD Response or ESPD Request) */
        Map ftlModel = new HashMap();
        ftlModel.put("espdModel", espdRequestModel);
        ftlModel.put("espdProperties",NodeModel.parse(findPropFile(ESPD_PROP_DOC+lang.name().toLowerCase()+XML)));
        ftlModel.put("espdEnProperties",NodeModel.parse(findPropFile(ESPD_EN_PROP_DOC+XML)));
        ftlModel.put("logoPath", getImageAsBase64(LOGO_PATH));
        ftlModel.put("fontAwesomeCssPath", FONT_AWESOME_CSS_PATH);

        /*--------------------------------------------------------------------------------------------------------------*/

        StringWriter sw = new StringWriter();
        temp.process(ftlModel,sw);
        sw.flush();

        return new W3CDom().fromJsoup(Jsoup.parse(sw.toString()));
    }

    /**
     * Transforms an ESPD Response model to W3C document.
     * <p>Use {@link private File findPropFile(String path)} to select the right path of properties file</p>
     * @param espdResponseModel is the ESPD Response Model.
     * @param lang is the selected language for HTML or PDF document.
     * @throws IOException
     * @throws TemplateException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @return org.w3c.dom.Document
     * */
    public Document transformToW3CDoc(ESPDResponse espdResponseModel,EULanguageCodeEnum lang) throws IOException,
            TemplateException, ParserConfigurationException, SAXException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_30);
        cfg.setClassForTemplateLoading(TransformationService.class,FTL_PATH);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        Template temp = cfg.getTemplate("espd_document.ftlh");

        /* ftlModel contains ESPD Response model, ESPD Properties model and type of model(ESPD Response or ESPD Request) */
        Map ftlModel = new HashMap();
        ftlModel.put("espdModel", espdResponseModel);
        ftlModel.put("espdProperties",NodeModel.parse(findPropFile(ESPD_PROP_DOC+lang.name().toLowerCase()+XML)));
        ftlModel.put("espdEnProperties",NodeModel.parse(findPropFile(ESPD_EN_PROP_DOC+XML)));
        ftlModel.put("logoPath", getImageAsBase64(LOGO_PATH));
        ftlModel.put("fontAwesomeCssPath", FONT_AWESOME_CSS_PATH);
        /*--------------------------------------------------------------------------------------------------------------*/

        StringWriter sw = new StringWriter();
        temp.process(ftlModel,sw);
        sw.flush();

        return new W3CDom().fromJsoup(Jsoup.parse(sw.toString()));
    }

    /**
     * Selects the right path of properties file.
     * @param path is the path of properties file.
     * @return java.io.File
     * */
    private InputSource findPropFile(String path){
        String pathForBuildingProj = "eu/esens/espdvcd/transformation/";
        InputStream isResource = this.getClass().getClassLoader().getResourceAsStream(pathForBuildingProj+path);
        return new InputSource(isResource);
    }

    public static String getImageAsBase64(String path) {
        try (InputStream is = TransformationService.class.getResourceAsStream(path)) {
            if (is == null) {
                throw new IllegalArgumentException("File not found: " + path);
            }
            BufferedImage image = ImageIO.read(is);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            byte[] bytes = baos.toByteArray();
            return Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read the image file", e);
        }
    }

    /**
     * Creates the PDF builder with W3C document.
     * <p>Use {@link private void createPdf(OutputStream outputStream, PdfRendererBuilder builder)} to create the PDF in stream form</p>
     * @param espdModelRequest is the ESPD Request Model.
     * @param outputStream is the PDF document in stream form.
     * @param lang is the selected language for PDF document.
     * @throws IOException
     * @throws TemplateException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public void createPdf(ESPDRequest espdModelRequest, OutputStream outputStream, EULanguageCodeEnum lang) throws IOException,
            TemplateException, ParserConfigurationException, SAXException {
        Document doc = transformToW3CDoc(espdModelRequest,lang);
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.withW3cDocument(doc, null);
        createPdf(outputStream, builder);
    }

    /**
     * Creates the PDF builder with W3C document.
     * <p>Use {@link private void createPdf(OutputStream outputStream, PdfRendererBuilder builder)} to create the PDF in stream form</p>
     * @param espdModelResponse is the ESPD Response Model.
     * @param outputStream is the PDF document in stream form.
     * @param lang is the selected language for PDF document.
     * @throws IOException
     * @throws TemplateException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public void createPdf(ESPDResponse espdModelResponse, OutputStream outputStream, EULanguageCodeEnum lang) throws IOException,
            TemplateException, ParserConfigurationException, SAXException {
        Document doc = transformToW3CDoc(espdModelResponse,lang);
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.withW3cDocument(doc, null);
        createPdf(outputStream, builder);
    }

    /**
     * Creates the PDF in stream form.
     * @param outputStream is the PDF document in stream form.
     * @param builder is the PDF builder.
     * @throws IOException
     * @throws TemplateException
     * @throws ParserConfigurationException
     * @throws SAXException
     * */
    private void createPdf(OutputStream outputStream, PdfRendererBuilder builder) throws IOException {
        builder
//                .useFont(
//                () -> TransformationService.class.getClassLoader().getResourceAsStream(FREE_SANS_PATH+"FreeSans.ttf"),
//                FREE_SANS, 400, BaseRendererBuilder.FontStyle.NORMAL, true);
                .toStream(outputStream)
                .useFont(FREE_SANS_FONT_FILE, "Free-Sans")
                .useFont(FONT_AWESOME_FONT_FILE, "FontAwesome");

        PdfBoxRenderer renderer = builder.buildPdfRenderer();
        renderer.layout();
        renderer.createPDF();
    }

    /**
     * Creates the HTML in stream form.
     * @param espdModelRequest is the ESPD Request Model.
     * @param outputStream is the PDF document in stream form.
     * @param lang is the selected language for HTML document.
     * @throws IOException
     * @throws TemplateException
     * @throws ParserConfigurationException
     * @throws SAXException
     * */
    public void createHtml(ESPDRequest espdModelRequest, OutputStream outputStream, EULanguageCodeEnum lang) throws
            TemplateException, ParserConfigurationException, SAXException, IOException {
        Document doc = transformToW3CDoc(espdModelRequest,lang);
        DOMSource transformed = new DOMSource(doc);

        Result outputTarget = new StreamResult(outputStream);
        try {
            TransformerFactory.newInstance().newTransformer().transform(transformed, outputTarget);
        } catch (TransformerException e) {
            LOGGER.error("Error while transforming document", e);
            displayStackTraceElements(e);
            e.printStackTrace();
        }
    }

    /**
     * Creates the HTML in stream form.
     * @param espdModelResponse is the ESPD Response Model.
     * @param outputStream is the PDF document in stream form.
     * @param lang is the selected language for HTML document.
     * @throws IOException
     * @throws TemplateException
     * @throws ParserConfigurationException
     * @throws SAXException
     * */
    public void createHtml(ESPDResponse espdModelResponse, OutputStream outputStream, EULanguageCodeEnum lang) throws TemplateException,
            IOException, ParserConfigurationException, SAXException {
        Document doc = transformToW3CDoc(espdModelResponse,lang);
        DOMSource transformed = new DOMSource(doc);

        Result outputTarget = new StreamResult(outputStream);
        try {
            TransformerFactory.newInstance().newTransformer().transform(transformed, outputTarget);
        } catch (TransformerException e) {
            LOGGER.error("Error while transforming document", e);
            displayStackTraceElements(e);
            e.printStackTrace();
        }
    }

    /**
     * <p>Use
     * {@link public void createHtml(ESPDRequest espdModelRequest, OutputStream outputStream, EULanguageCodeEnum lang, String ESPDVersion)}
     * to create HTML in stream form
     * </p>
     * @param espdModelRequest is the ESPD Request Model.
     * @param languageCodeEnum is the selected language for HTML document.
     * @throws IOException
     * @throws TemplateException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @return java.io.Inputstream
     * */
    public InputStream createHtmlStream(ESPDRequest espdModelRequest, EULanguageCodeEnum languageCodeEnum) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (OutputStream os = new BufferedOutputStream(outputStream)) {
            createHtml(espdModelRequest, os, languageCodeEnum);
        } catch (IOException | TemplateException | ParserConfigurationException | SAXException e) {
            LOGGER.error("Error while transforming document", e);
            displayStackTraceElements(e);
            e.printStackTrace();
        }
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    /**
     * <p>Use
     * {@link public void createHtml(ESPDResponse espdModelResponse, OutputStream outputStream, EULanguageCodeEnum lang, String ESPDVersion)}
     * to create HTML in stream form
     * </p>
     * @param espdModelResponse is the ESPD Response Model.
     * @param languageCodeEnum is the selected language for HTML document.
     * @throws IOException
     * @throws TemplateException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @return java.io.Inputstream
     * */
    public InputStream createHtmlStream(ESPDResponse espdModelResponse, EULanguageCodeEnum languageCodeEnum) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (OutputStream os = new BufferedOutputStream(outputStream)) {
            createHtml(espdModelResponse, os, languageCodeEnum);
        } catch (IOException | TemplateException | SAXException | ParserConfigurationException e) {
            LOGGER.error("Error while transforming document", e);
            displayStackTraceElements(e);
            e.printStackTrace();
        }
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    /**
     * <p>Use
     * {@link public void createPDF(ESPDRequest espdModelRequest, OutputStream outputStream, EULanguageCodeEnum lang, String ESPDVersion)}
     * to create PDF in stream form
     * </p>
     * @param espdRequestModel is the ESPD Request Model.
     * @param languageCodeEnum is the selected language for PDF document.
     * @throws IOException
     * @throws TemplateException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @return java.io.Inputstream
     * */
    public InputStream createPdfStream(ESPDRequest espdRequestModel, EULanguageCodeEnum languageCodeEnum) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (OutputStream os = new BufferedOutputStream(outputStream)) {
            createPdf(espdRequestModel, os, languageCodeEnum);
        } catch (IOException | TemplateException | ParserConfigurationException | SAXException e) {
            LOGGER.error("Error while transforming document", e);
            displayStackTraceElements(e);
            e.printStackTrace();
        }
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    /**
     * <p>Use
     * {@link public void createPDF(ESPDResponse espdModelResponse, OutputStream outputStream, EULanguageCodeEnum lang, String ESPDVersion)}
     * to create PDF in stream form
     * </p>
     * @param espdResponseModel is the ESPD Response Model.
     * @param languageCodeEnum is the selected language PDF document.
     * @throws IOException
     * @throws TemplateException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @return java.io.Inputstream
     * */
    public InputStream createPdfStream(ESPDResponse espdResponseModel, EULanguageCodeEnum languageCodeEnum) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (OutputStream os = new BufferedOutputStream(outputStream)) {
            createPdf(espdResponseModel, os, languageCodeEnum);
        } catch (IOException | TemplateException | SAXException | ParserConfigurationException e) {
            LOGGER.error("Error while transforming document", e);
            displayStackTraceElements(e);
            e.printStackTrace();
        }
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    /**
     * Display info for an exception
     * @param ex is an exception
     * */
    public void displayStackTraceElements(Exception ex) {
        StackTraceElement[] stackTraces = ex.getStackTrace();
        for(StackTraceElement stackTrace :stackTraces){
            System.out.printf("Info from stack trace: %s\n",stackTrace.getClassName(),
                    stackTrace.getFileName(),
                    stackTrace.getLineNumber(),
                    stackTrace.getMethodName());
        }
    }

}
