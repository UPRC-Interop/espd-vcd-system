/**
 * Created by ixuz on 2/9/16.
 */

package eu.esens.espdvcd.designer;

import eu.esens.espdvcd.codelist.CountryCodeGC;
import eu.esens.espdvcd.designer.components.CountryComboBox;
import eu.esens.espdvcd.model.uifacade.ElementContainer;
import eu.esens.espdvcd.model.uifacade.ElementContainerImpl;
import eu.esens.espdvcd.model.uifacade.ElementType;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.OptionGroup;
import com.vaadin.server.ThemeResource;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;
import java.util.Collection;

public class FormFactory {
    /**
     * Constructs a Vaadin7 form based upon the provided ElementContainer.
     * The generated form is automatically added to the layout.
     *
     * @param layout the form will automatically be added to this Vaadin7-layout
     * @param element describes the form that will be generated
     */
    public static AbstractOrderedLayout CreateForm(AbstractOrderedLayout layout, ElementContainer<ElementContainer> element) {
        return CreateFormRecursive(layout, element, 0);
    }

    /**
     * Constructs a Vaadin7 form based upon the provided ElementContainer.
     * The generated form is automatically added to the layout.
     * When calling this method, usually the level should be set to 0 which indicates the root of the structure.
     *
     * @param layout the form will automatically be added to this Vaadin7-layout
     * @param element describes the form that will be generated
     * @param level keeps track of the depth of each ElementContainer, this is used for the css style identifier names
     * @see eu.esens.espdvcd.model.uifacade.ElementContainer
     */
    @SuppressWarnings("unchecked")
    public static AbstractOrderedLayout CreateFormRecursive(AbstractOrderedLayout layout, ElementContainer<ElementContainer> element, int level) {

        // A composite element type that will may contain children elements.
        if (element.getElementType() == ElementType.COMPOSITE) {
            VerticalLayout content = new VerticalLayout();
            content.setMargin(true);

            // Create a panel to store all the sub-content in this element
            Panel panel = new Panel("");
            panel.setStyleName("form-depth-" + level + "-panel");
            layout.addComponent(panel);
            panel.setContent(content);

            // Construct a label from the Name
            if (element.getName() != null) {
                panel.setCaption(element.getID() + " " + element.getName());
            }

            // Construct a label from the Short description
            if (element.getShortDescription() != null) {
                if (!element.getShortDescription().isEmpty()) {
                    content.addComponent(new Label(element.getShortDescription()));
                }
            }

            // Construct a label from the Detailed description
            if (element.getDetailedDescription() != null) {
                content.addComponent(new Label(element.getDetailedDescription()));
            }

            // Construct a label listing the number of children elements
            if (element.getDefaultContent() != null) {
                // Loop each child element
                for (ElementContainer<ElementContainer> el : element.getDefaultContent()) {
                    CreateFormRecursive(content, el, new Integer(level)+1);
                }
            }
        }

        // A static text element type, generate a basic Vaadin7 Label
        if (element.getElementType() == ElementType.STATICTEXT) {
            Label label = new Label(element.getDetailedDescription());
            label.setStyleName("form-depth-" + level + "-static-text");
            layout.addComponent(label);
        }

        // A textfield element type, generate a basic Vaadin7 input field
        if (element.getElementType() == ElementType.TEXTFIELD) {
            TextField textField = new TextField(element.getID() + " " + element.getName());
            textField.setStyleName("form-depth-" + level + "-text-field");
            layout.addComponent(textField);
        }

        // A selection list element type, generate a basic Vaadin7 drop-down menu
        if (element.getElementType() == ElementType.SELECTIONLIST) {
            CountryComboBox comboBox = new CountryComboBox(element.getID() + " " + element.getName(), "Sweden");
            layout.addComponent(comboBox);

            IndexedContainer ic = new IndexedContainer(element.getDefaultContent());
            for (int i=0; i<ic.size(); i++) {
                String country = (String)ic.getIdByIndex(i);
                String isoCode = "";
                try {
                    isoCode = CountryCodeGC.getISOCode(country.toUpperCase());
                } catch (IllegalArgumentException e) {
                    isoCode = "null";
                }
                comboBox.addIconItem(country, "img/flags_iso/24/" + isoCode.toLowerCase() + ".png");
            }

        }

        // A radio list element type, generate a basic Vaadin7 radio list menu
        if (element.getElementType() == ElementType.RADIOLIST) {
            OptionGroup optionGroup = new OptionGroup(element.getID() + " " + element.getName());
            optionGroup.setStyleName("form-depth-" + level + "-radio-list");
            IndexedContainer ic = new IndexedContainer(element.getDefaultContent());
            optionGroup.setContainerDataSource(ic);
            layout.addComponent(optionGroup);
        }

        return layout;
    }

    /**
     * Sample construct of an ESPDVCD template.
     * This method is temporary and will be replaced by the backend ESPDVCD builder.
     *
     * @return ElementContainer
     */
    public static ElementContainer<ElementContainer> SampleEspdTemplate() {
        // prepare data structure for ESPD Template
        List<ElementContainer> espdTemplateElements = new LinkedList<>();

        ElementContainer<ElementContainer> espdTemplate = new ElementContainerImpl<ElementContainer>(
                ElementType.COMPOSITE,
                "",
                "ESPD Template",
                "Service to fill out and reuse the ESPD Template",
                null,
                null,
                true,
                true,
                espdTemplateElements, null
        );

        // create the 4 input masks (based on DG GROW ESPD system)
        espdTemplateElements.add(createDetails());
        espdTemplateElements.add(createProcedureMask());
        espdTemplateElements.add(createExclusionCriteriaMask());
        espdTemplateElements.add(createSelectionCriteriaMask());
        espdTemplateElements.add(createFinishMask());

        return espdTemplate;
    }

    /**
     * Sample construct of an ESPDVCD details model.
     * This method is temporary and will be replaced by the backend ESPDVCD builder.
     *
     * @return ElementContainer
     */
    private static ElementContainer createDetails() {
        List<ElementContainer> topLevelElements = new LinkedList<>();

        // create top-level frame
        ElementContainer<ElementContainer> topLevelElement = new ElementContainerImpl<ElementContainer>(
                ElementType.COMPOSITE,
                "",
                "Welcome to the ESPD service",
                "",
                null,
                null,
                true,
                true,
                topLevelElements, null
        );

        topLevelElements.add(new ElementContainerImpl<String>(
                ElementType.STATICTEXT,
                null,
                null,
                null,
                "European Single Procurement Document (ESPD) is a self-declaration of the businesses' financial status, abilities and suitability for a public procurement procedure. It is available in all EU languages and used as a preliminary evidence of fulfilment of the conditions required in public procurement procedures across the EU. Thanks to the ESPD, the tenderers no longer have to provide full documentary evidence and different forms previously used in the EU procurement, which means a significant simplification of access to cross-border tendering opportunities. From October 2018 onwards the ESPD shall be provided exclusively in an electronic form. The European Commission provides a free web service for the buyers, bidders and other parties interested in filling in the ESPD electronically. The online form can be filled in, printed and then sent to the buyer together with the rest of the bid. If the procedure is run electronically, the ESPD can be exported, stored and submitted electronically. The ESPD provided in a previous public procurement procedure can be reused as long as the information remains correct. Bidders may be excluded from the procedure or be subject to prosecution if the information in the ESPD is seriously misrepresented, withheld or cannot be complemented with supporting documents.",
                null,
                true,
                true,
                null, null
        ));

        topLevelElements.add(new ElementContainerImpl<String>(
                ElementType.RADIOLIST,
                "",
                "Who are you?",
                null,
                null,
                null,
                true,
                true,
                Arrays.asList(new String[]{"I am a contracting authority", "I am an economic operator"}), null
        ));

        topLevelElements.add(new ElementContainerImpl<String>(
                ElementType.RADIOLIST,
                "",
                "What would you like to do?",
                null,
                null,
                null,
                true,
                true,
                Arrays.asList(new String[]{"Create a new ESPD", "Reuse an existing ESPD", "Review ESPD"}), null
        ));

        topLevelElements.add(new ElementContainerImpl<String>(
                ElementType.SELECTIONLIST,
                "",
                "Where are you from?",
                null,
                null,
                null,
                true,
                true,
                Arrays.asList(new String[]{"Germany", "Greece", "Sweden"}), null
        ));

        return topLevelElement;
    }

    /**
     * Sample construct of an ESPDVCD procedure model.
     * This method is temporary and will be replaced by the backend ESPDVCD builder.
     *
     * @return ElementContainer
     */
    private static ElementContainer createProcedureMask() {
        List<ElementContainer> topLevelElements = new LinkedList<>();

        // create top-level frame
        ElementContainer<ElementContainer> topLevelElement = new ElementContainerImpl<ElementContainer>(
                ElementType.COMPOSITE,
                "1",
                "Procedure",
                "Information concerning the contracting authority and the procurement procedure",
                null,
                null,
                true,
                true,
                topLevelElements, null
        );

        // create element "Information about publication"
        topLevelElements.add(new ElementContainerImpl<String>(
                ElementType.TEXTFIELD,
                "1.1",
                "Information about publication",
                "For procurement procedures in which a call for competition has been published in the Official Journal of the European Union, the information required under Part I will be automatically retrieved, provided that the electronic ESPD-service is used to generate and fill in the ESPD. Reference of the relevant notice published in the Official Journal of the European Union:",
                null, // omitted "In case publication of a notice in the Official Journal of the European Union is not required, please give other information allowing the procurement procedure to be unequivocally identified (e. g. reference of a publication at national level):"
                null,
                true,
                true,
                Arrays.asList(new String[]{"____/S ___-_______"}), "[][][][]/S [][][]-[][][][][][][]"
        ));

        // create element "Identify the procurer"
        List<ElementContainer> procurerElements = new LinkedList<>();
        topLevelElements.add(new ElementContainerImpl<ElementContainer>(
                ElementType.COMPOSITE,
                "1.2",
                "Identify the procurer",
                null,
                null,
                null,
                true,
                true,
                procurerElements, null
        ));
        procurerElements.add(new ElementContainerImpl<String>(
                ElementType.TEXTFIELD,
                "1.2.1",
                "Official Name:",
                null,
                null,
                null,
                true,
                true,
                null, null
        ));
        procurerElements.add(new ElementContainerImpl<String>(
                ElementType.SELECTIONLIST,
                "1.2.2",
                "Country:",
                null,
                null,
                null,
                true,
                true,
                Arrays.asList(new String[]{"Germany", "Greece", "Sweden"}), null
        ));

        // create element "Information about the procurement procedure"
        List<ElementContainer> procurmentProcedureElements = new LinkedList<>();
        topLevelElements.add(new ElementContainerImpl<ElementContainer>(
                ElementType.COMPOSITE,
                "1.3",
                "Information about the procurement procedure",
                null,
                null,
                null,
                true,
                true,
                procurmentProcedureElements, null
        ));

        procurmentProcedureElements.add(new ElementContainerImpl<String>(
                ElementType.TEXTFIELD,
                "1.3.1",
                "Title:",
                null,
                null,
                null,
                true,
                true,
                null, null
        ));
        procurmentProcedureElements.add(new ElementContainerImpl<String>(
                ElementType.TEXTFIELD,
                "1.3.2",
                "Short description:",
                null,
                null,
                null,
                true,
                true,
                null, null
        ));
        procurmentProcedureElements.add(new ElementContainerImpl<String>(
                ElementType.TEXTFIELD,
                "1.3.3",
                "File reference number attributed by the contracting authority or contracting entity (if applicable):",
                null,
                null,
                null,
                true,
                true,
                null, null
        ));

        return topLevelElement;
    }

    /**
     * Sample construct of an ESPDVCD exclusion criteria model.
     * This method is temporary and will be replaced by the backend ESPDVCD builder.
     *
     * @return ElementContainer
     */
    private static ElementContainer createExclusionCriteriaMask() {
        List<ElementContainer> topLevelElements = new LinkedList<>();

        // create top-level frame
        ElementContainer<ElementContainer> topLevelElement = new ElementContainerImpl<ElementContainer>(
                ElementType.COMPOSITE,
                "2",
                "Exclusion grounds",
                "...to fill with content...",
                null,
                null,
                true,
                true,
                topLevelElements, null
        );

        return topLevelElement;
    }

    /**
     * Sample construct of an ESPDVCD selection criteria model.
     * This method is temporary and will be replaced by the backend ESPDVCD builder.
     *
     * @return ElementContainer
     */
    private static ElementContainer createSelectionCriteriaMask() {
        List<ElementContainer> topLevelElements = new LinkedList<>();

        // create top-level frame
        ElementContainer<ElementContainer> topLevelElement = new ElementContainerImpl<ElementContainer>(
                ElementType.COMPOSITE,
                "3",
                "Selection criteria",
                "...to fill with content...",
                null,
                null,
                true,
                true,
                topLevelElements, null
        );

        return topLevelElement;
    }

    /**
     * Sample construct of an ESPDVCD additional information model.
     * This method is temporary and will be replaced by the backend ESPDVCD builder.
     *
     * @return ElementContainer
     */
    private static ElementContainer createFinishMask() {
        List<ElementContainer> topLevelElements = new LinkedList<>();

        // create top-level frame
        ElementContainer<ElementContainer> topLevelElement = new ElementContainerImpl<ElementContainer>(
                ElementType.COMPOSITE,
                "4",
                "Finish",
                "...to fill with content...",
                null,
                null,
                true,
                true,
                topLevelElements, null
        );

        return topLevelElement;
    }
}
