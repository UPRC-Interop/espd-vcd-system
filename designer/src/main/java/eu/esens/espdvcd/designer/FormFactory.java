/**
 * Created by ixuz on 2/9/16.
 */

package eu.esens.espdvcd.designer;

import eu.esens.espdvcd.codelist.CountryCodeGC;
import eu.esens.espdvcd.designer.components.CountryComboBox;
import eu.esens.espdvcd.model.uifacade.ElementContainer;
import eu.esens.espdvcd.model.uifacade.ElementType;
import eu.esens.espdvcd.model.uifacade.ElementBuilder;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.OptionGroup;
import java.util.*;

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
                element.getDefaultContent().stream().forEach((el) -> {
                    CreateFormRecursive(content, el, level+1);
                });
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
    @SuppressWarnings("unchecked")
    public static ElementContainer<ElementContainer> SampleEspdTemplate() {
        // prepare data structure for ESPD Template
        List<ElementContainer> espdTemplateElements = new LinkedList<>();

        ElementContainer espdTemplate = new ElementBuilder()
                .id("1.0")
                .name("Name")
                .defaultContent(espdTemplateElements)
                .build();

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
    @SuppressWarnings("unchecked")
    private static ElementContainer createDetails() {
        List<ElementContainer> topLevelElements = new LinkedList<>();

        ElementContainer<ElementContainer> topLevelElement = new ElementBuilder()
                .elementType(ElementType.COMPOSITE)
                .name("Welcome to the ESPD service")
                .defaultContent(topLevelElements)
                .build();
        topLevelElements.add(new ElementBuilder()
                .elementType(ElementType.STATICTEXT)
                .detailedDescription("European Single Procurement Document (ESPD) is a self-declaration of the businesses' financial status, abilities and suitability for a public procurement procedure. It is available in all EU languages and used as a preliminary evidence of fulfilment of the conditions required in public procurement procedures across the EU. Thanks to the ESPD, the tenderers no longer have to provide full documentary evidence and different forms previously used in the EU procurement, which means a significant simplification of access to cross-border tendering opportunities. From October 2018 onwards the ESPD shall be provided exclusively in an electronic form. The European Commission provides a free web service for the buyers, bidders and other parties interested in filling in the ESPD electronically. The online form can be filled in, printed and then sent to the buyer together with the rest of the bid. If the procedure is run electronically, the ESPD can be exported, stored and submitted electronically. The ESPD provided in a previous public procurement procedure can be reused as long as the information remains correct. Bidders may be excluded from the procedure or be subject to prosecution if the information in the ESPD is seriously misrepresented, withheld or cannot be complemented with supporting documents.")
                .build());
        topLevelElements.add(new ElementBuilder()
                .elementType(ElementType.RADIOLIST)
                .name("Who are you?")
                .defaultContent(Arrays.asList("I am a contracting authority", "I am an economic operator"))
                .build());
        topLevelElements.add(new ElementBuilder()
                .elementType(ElementType.RADIOLIST)
                .name("What would you like to do?")
                .defaultContent(Arrays.asList("Create a new ESPD", "Reuse an existing ESPD", "Review ESPD"))
                .build());
        topLevelElements.add(new ElementBuilder()
                .elementType(ElementType.SELECTIONLIST)
                .name("Where are you from?")
                .defaultContent(Arrays.asList("Germany", "Greece", "Sweden"))
                .build());

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
        ElementContainer topLevelElement = new ElementBuilder()
                .elementType(ElementType.COMPOSITE)
                .id("1")
                .name("Procedure")
                .shortDescription("Information concerning the contracting authority and the procurement procedure")
                .defaultContent(topLevelElements)
                .build();

        // create element "Information about publication"
        topLevelElements.add(new ElementBuilder()
                .elementType(ElementType.TEXTFIELD)
                .id("1.1")
                .name("Information about publication")
                .shortDescription("For procurement procedures in which a call for competition has been published in the Official Journal of the European Union, the information required under Part I will be automatically retrieved, provided that the electronic ESPD-service is used to generate and fill in the ESPD. Reference of the relevant notice published in the Official Journal of the European Union:")
                .defaultContent(Arrays.asList(new String[]{"____/S ___-_______"}))
                .content("[][][][]/S [][][]-[][][][][][][]")
                .build());

        // create element "Identify the procurer"
        List<ElementContainer> procurerElements = new LinkedList<>();
        procurerElements.add(new ElementBuilder()
                .elementType(ElementType.COMPOSITE)
                .id("1.2")
                .name("Identify the procurer")
                .defaultContent(procurerElements)
                .build());
        procurerElements.add(new ElementBuilder()
                .elementType(ElementType.TEXTFIELD)
                .id("1.2.1")
                .name("Official Name:")
                .build());
        procurerElements.add(new ElementBuilder()
                .elementType(ElementType.SELECTIONLIST)
                .id("1.2.2")
                .name("Country:")
                .defaultContent(Arrays.asList(new String[]{"Germany", "Greece", "Sweden"}))
                .build());

        // create element "Information about the procurement procedure"
        List<ElementContainer> procurmentProcedureElements = new LinkedList<>();
        topLevelElements.add(new ElementBuilder()
                .elementType(ElementType.COMPOSITE)
                .id("1.3")
                .name("Information about the procurement procedure")
                .defaultContent(procurmentProcedureElements)
                .build());

        procurmentProcedureElements.add(new ElementBuilder()
                .elementType(ElementType.TEXTFIELD)
                .id("1.3.1")
                .name("Title:")
                .build());
        procurmentProcedureElements.add(new ElementBuilder()
                .elementType(ElementType.TEXTFIELD)
                .id("1.3.2")
                .name("Short description:")
                .build());
        procurmentProcedureElements.add(new ElementBuilder()
                .elementType(ElementType.TEXTFIELD)
                .id("1.3.3")
                .name("File reference number attributed by the contracting authority or contracting entity (if applicable):")
                .build());

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
        ElementContainer<ElementContainer> topLevelElement = new ElementBuilder()
                .elementType(ElementType.COMPOSITE)
                .id("2")
                .name("Exclusion grounds")
                .shortDescription("...to fill with content...")
                .defaultContent(topLevelElements)
                .build();

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
        ElementContainer<ElementContainer> topLevelElement = new ElementBuilder()
                .elementType(ElementType.COMPOSITE)
                .id("3")
                .name("Selection criteria")
                .shortDescription("...to fill with content...")
                .defaultContent(topLevelElements)
                .build();

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
        ElementContainer<ElementContainer> topLevelElement = new ElementBuilder()
                .elementType(ElementType.COMPOSITE)
                .id("4")
                .name("Finish")
                .shortDescription("...to fill with content...")
                .defaultContent(topLevelElements)
                .build();

        return topLevelElement;
    }
}
