package eu.esens.espdvcd.designer.components;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.*;
import eu.esens.espdvcd.builder.VCDDocumentBuilder;
import eu.esens.espdvcd.builder.XMLDocumentBuilder;
import eu.esens.espdvcd.codelist.CodeListV1;
import eu.esens.espdvcd.designer.views.Master;
import eu.esens.espdvcd.model.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import no.difi.asic.SignatureHelper;

public class ESPDResponseForm extends ESPDForm {

    private static final long serialVersionUID = -5560648566867967260L;

    private ESPDResponse espdResponse = null;
    private LinkedHashMap<String,List<CriterionForm>> exclusionCriterionHash = new LinkedHashMap<>();
    private LinkedHashMap<String,List<CriterionForm>> selectionCriterionHash = new LinkedHashMap<>();
    private LinkedHashMap<String,List<CriterionForm>> economicOperatorCriterionHash = new LinkedHashMap<>();
    private LinkedHashMap<String,List<CriterionForm>> reductionCriterionHash = new LinkedHashMap<>();
    private List<CriterionGroupForm> exclusionCriterionGroupForms = new ArrayList<>();
    private List<CriterionGroupForm> selectionCriterionGroupForms = new ArrayList<>();
    private List<CriterionGroupForm> economicOperatorCriterionGroupForms = new ArrayList<>();
    private List<CriterionGroupForm> reductionCriterionGroupForms = new ArrayList<>();

    int displayEvidences;

    public ESPDResponseForm(Master view, ESPDResponse espdResponse, int displayEvidences, boolean readOnly) {
        super(view, espdResponse, "espd.xml");
        this.espdResponse = espdResponse;
        this.displayEvidences = displayEvidences;

        // Page 1 - Procedure
        VerticalLayout page1 = newPage("Information concerning the procurement procedure", "Procedure");
        page1.addComponent(new CADetailsForm(espdResponse, readOnly));
        page1.addComponent(new EODetailsForm(espdResponse.getEODetails(), readOnly));

        for (SelectableCriterion criterion : espdResponse.getEORelatedCriteriaList()) {

            if (!economicOperatorCriterionHash.containsKey(criterion.getCriterionGroup())) {
                economicOperatorCriterionHash.put(criterion.getCriterionGroup(), new ArrayList<>());
            }

            CriterionForm criterionForm = new CriterionForm(view, criterion, true, displayEvidences, readOnly, espdResponse.getEODetails());
            economicOperatorCriterionHash.get(criterion.getCriterionGroup()).add(criterionForm);
        }

        for (Map.Entry<String, List<CriterionForm>> entry : economicOperatorCriterionHash.entrySet()) {
            String key = entry.getKey();
            List<CriterionForm> criterionForms = entry.getValue();

            String fullTypeCodeName = CodeListV1.CriteriaType.getValueForId(key);

            if (fullTypeCodeName == null) {
                // Fallback to display the type code instead of real name
                fullTypeCodeName = key;
            }

            CriterionGroupForm criterionGroupForm = new CriterionGroupForm(espdResponse, view, fullTypeCodeName, criterionForms);
            economicOperatorCriterionGroupForms.add(criterionGroupForm);
            page1.addComponent(criterionGroupForm);
        }

        // Page 2 - Exclusion
        VerticalLayout page2 = newPage("Exclusion grounds criteria", "Exclusion");

        VerticalLayout exclusionActionLayout = new VerticalLayout();
        exclusionActionLayout.setMargin(true);
        page2.addComponent(exclusionActionLayout);

        for (SelectableCriterion criterion : espdResponse.getExclusionCriteriaList()) {

            if (!exclusionCriterionHash.containsKey(criterion.getCriterionGroup())) {
                exclusionCriterionHash.put(criterion.getCriterionGroup(), new ArrayList<CriterionForm>());
            }

            CriterionForm criterionForm = new CriterionForm(view, criterion, true, displayEvidences, readOnly, espdResponse.getEODetails());
            exclusionCriterionHash.get(criterion.getCriterionGroup()).add(criterionForm);
        }

        for (Map.Entry<String, List<CriterionForm>> entry : exclusionCriterionHash.entrySet()) {
            String key = entry.getKey();
            List<CriterionForm> criterionForms = entry.getValue();

            String fullTypeCodeName = CodeListV1.CriteriaType.getValueForId(key);

            if (fullTypeCodeName == null) {
                // Fallback to display the type code instead of real name
                fullTypeCodeName = key;
            }

            CriterionGroupForm criterionGroupForm = new CriterionGroupForm(espdResponse, view, fullTypeCodeName, criterionForms);
            exclusionCriterionGroupForms.add(criterionGroupForm);
            page2.addComponent(criterionGroupForm);
        }


        // Page 3 - Selection
        VerticalLayout page3 = newPage("Selection criteria", "Selection");

        VerticalLayout selectionActionLayout = new VerticalLayout();
        selectionActionLayout.setMargin(true);
        page3.addComponent(selectionActionLayout);

        for (SelectableCriterion criterion : espdResponse.getSelectionCriteriaList()) {

            //if (!selectionCriterionHash.containsKey(criterion.getTypeCode())) {
            // bugfix UL_2016-12-22: only one criterion per criterion group was displayed - wrong map key was used
            if (!selectionCriterionHash.containsKey(criterion.getCriterionGroup())) {
                selectionCriterionHash.put(criterion.getCriterionGroup(), new ArrayList<CriterionForm>());
            }

            CriterionForm criterionForm = new CriterionForm(view, criterion, true, displayEvidences, readOnly, espdResponse.getEODetails());
            selectionCriterionHash.get(criterion.getCriterionGroup()).add(criterionForm);

        }

        for (Map.Entry<String, List<CriterionForm>> entry : selectionCriterionHash.entrySet()) {
            String key = entry.getKey();
            List<CriterionForm> criterionForms = entry.getValue();

            String fullTypeCodeName = CodeListV1.CriteriaType.getValueForId(key);

            if (fullTypeCodeName == null) {
                // Fallback to display the type code instead of real name
                fullTypeCodeName = key;
            }

            CriterionGroupForm criterionGroupForm = new CriterionGroupForm(espdResponse, view, fullTypeCodeName, criterionForms);
            selectionCriterionGroupForms.add(criterionGroupForm);
            page3.addComponent(new CriterionGroupForm(espdResponse, view, fullTypeCodeName, criterionForms));
        }


        // Page 4 - Reduction of candidates
        VerticalLayout page4 = newPage("Reduction of candidates", "Reduction of candidates");

        VerticalLayout reductionActionLayout = new VerticalLayout();
        reductionActionLayout.setMargin(true);
        page4.addComponent(reductionActionLayout);

        for (SelectableCriterion criterion : espdResponse.getReductionOfCandidatesCriteriaList()) {

            if (!reductionCriterionHash.containsKey(criterion.getCriterionGroup())) {
                reductionCriterionHash.put(criterion.getCriterionGroup(), new ArrayList<CriterionForm>());
            }

            CriterionForm criterionForm = new CriterionForm(view, criterion, true, displayEvidences, readOnly, espdResponse.getEODetails());
            reductionCriterionHash.get(criterion.getCriterionGroup()).add(criterionForm);
        }

        for (Map.Entry<String, List<CriterionForm>> entry : reductionCriterionHash.entrySet()) {
            String key = entry.getKey();
            List<CriterionForm> criterionForms = entry.getValue();

            String fullTypeCodeName = CodeListV1.CriteriaType.getValueForId(key);

            if (fullTypeCodeName == null) {
                // Fallback to display the type code instead of real name
                fullTypeCodeName = key;
            }

            CriterionGroupForm criterionGroupForm = new CriterionGroupForm(espdResponse, view, fullTypeCodeName, criterionForms);
            reductionCriterionGroupForms.add(criterionGroupForm);
            page4.addComponent(new CriterionGroupForm(espdResponse, view, fullTypeCodeName, criterionForms));
        }

        // Page 5 - Finish
        VerticalLayout page5 = newPage("Finish", "Finish");

        if (displayEvidences == 1 || displayEvidences == 2) {
            setExportAsicResource();
        } else {
            setExportXmlResource();
        }
    }

    /**
     * When the user have clicked the Export button, this method is invoked.
     * Exports the espd request xml to the system console
     *
     * @param event Vaadin7 Button click event
     * @see com.vaadin.ui.Button.ClickEvent
     */
    @Override
    protected void onExportConsole(Button.ClickEvent event) {
        // Display espd request xml button
        System.out.println("espdResponse: " + espdResponse);
        String xml = new XMLDocumentBuilder(espdResponse).getAsString();
        System.out.println("Xml: " + xml);
    }


    private static final String KEY_STORE_RESOURCE_NAME = "kontaktinfo-client-test.jks";

    /**
     * temporary method for signing the ASiC - copied from no.difi.asic.TestUtil
     * @return key store file
     */
    private File getKeyStoreFile() throws IllegalStateException {

        URL keyStoreResourceURL = this.getClass().getClassLoader().getResource(KEY_STORE_RESOURCE_NAME);
        try {
            URI uri = keyStoreResourceURL.toURI();

            File file = new File(uri);
            if (!file.canRead()) {
                throw new IllegalStateException("Unable to locate " + KEY_STORE_RESOURCE_NAME + " in class path");
            }
            return file;

        } catch (URISyntaxException e) {
            throw new IllegalStateException("Unable to convert URL of keystore " + KEY_STORE_RESOURCE_NAME + " into a URI");
        }
    }

    /**
     * temporary method for signing the ASiC - copied from no.difi.asic.TestUtil
     * @return
     */
    private String keyStorePassword() {
        return "changeit";
    }

    /**
     * temporary method for signing the ASiC - copied from no.difi.asic.TestUtil
     * @return
     */
    private String privateKeyPassword() {
        return "changeit";
    }

    /**
     * temporary method for signing the ASiC - copied from no.difi.asic.TestUtil
     * @return
     */
    private String keyPairAlias() {
        return "client_alias";
    }
}
