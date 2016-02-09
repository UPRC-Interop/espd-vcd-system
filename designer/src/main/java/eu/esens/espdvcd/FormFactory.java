package eu.esens.espdvcd;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import eu.esens.espdvcd.model.uifacade.TestElementContainer;
import eu.esens.espdvcd.model.uifacade.ElementContainer;
import eu.esens.espdvcd.model.uifacade.ElementContainerImpl;
import eu.esens.espdvcd.model.uifacade.ElementType;

import java.util.*;

/**
 * Created by ixuz on 2/9/16.
 */
public class FormFactory {
    @SuppressWarnings("unchecked")
    public static AbstractOrderedLayout CreateForm(AbstractOrderedLayout layout, ElementContainer<ElementContainer> element) {

        if (element.getElementType() == ElementType.COMPOSITE) {
            VerticalLayout content = new VerticalLayout();
            content.setMargin(true);

            Panel panel = new Panel("");
            layout.addComponent(panel);
            panel.setContent(content);

            // Construct a label from the Name
            if (element.getName() != null) {
                panel.setCaption(element.getID() + " " + element.getName());
            }

            // Construct a label from the Short description
            if (element.getShortDescription() != null) {
                content.addComponent(new Label(element.getShortDescription()));
            }

            // Construct a label from the Detailed description
            if (element.getDetailedDescription() != null) {
                content.addComponent(new Label(element.getDetailedDescription()));
            }

            // Construct a label listing the number of children elements
            if (element.getDefaultContent() != null) {
                // Loop each child element
                for (ElementContainer<ElementContainer> el : element.getDefaultContent()) {
                    CreateForm(content, el);
                }
            }
        }

        if (element.getElementType() == ElementType.TEXTFIELD) {
            TextField textField = new TextField(element.getID() + " " + element.getName());
            layout.addComponent(textField);
        }

        if (element.getElementType() == ElementType.SELECTIONLIST) {
            ComboBox comboBox = new ComboBox(element.getID() + " " + element.getName());
            IndexedContainer ic = new IndexedContainer(element.getDefaultContent());
            comboBox.setContainerDataSource(ic);
            layout.addComponent(comboBox);
        }

        return layout;
    }

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
        espdTemplateElements.add(createProcedureMask());
        espdTemplateElements.add(createExclusionCriteriaMask());
        espdTemplateElements.add(createSelectionCriteriaMask());
        espdTemplateElements.add(createFinishMask());

        return espdTemplate;
    }
    /**
     * Create Procedure mask.
     *
     * @return
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
     * Create mask for Exclusion Criteria.
     * tbd.
     *
     * @return
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
     * Create mask for Selection Criteria.
     * tbd.
     *
     * @return
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
     * Create mask for additional information.
     * tbd.
     *
     * @return
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
