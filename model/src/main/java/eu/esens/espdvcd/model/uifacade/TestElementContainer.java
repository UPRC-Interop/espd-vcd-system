package eu.esens.espdvcd.model.uifacade;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Demonstation class to show the usage of ElementContainer.
 * 
 *
 */
public class TestElementContainer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// prepare data structure for ESPD Template
		TestElementContainer tec = new TestElementContainer();
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
		espdTemplateElements.add(tec.createProcedureMask());
		espdTemplateElements.add(tec.createExclusionCriteriaMask());
		espdTemplateElements.add(tec.createSelectionCriteriaMask());
		espdTemplateElements.add(tec.createFinishMask());
		
		// print data structure to console
		tec.printElement(espdTemplate, "");
	}
	
	/**
	 * Recursive method to print the content of ec to the console.
	 * 
	 * @param ec
	 * @param indent
	 */
	private void printElement(ElementContainer ec, String indent) {
		switch (ec.getElementType()) {
		case STATICTEXT:
			System.out.println(indent + ec.getName());
			break;

		case TEXTFIELD:
			if (ec.getShortDescription() != null) System.out.println(indent + ec.getShortDescription() + "\n");
			System.out.println(indent + ec.getName() + "|" + ec.getContent() + "|");
			if (ec.getDetailedDescription() != null) System.out.println(indent + ec.getDetailedDescription() + "\n");
			break;
			
		case SELECTIONLIST:
			if (ec.getShortDescription() != null) System.out.println(indent + ec.getShortDescription() + "\n");
			System.out.println(indent + ec.getName() + "[" + ec.getDefaultContent() + "]");
			if (ec.getDetailedDescription() != null) System.out.println(indent + ec.getDetailedDescription() + "\n");
			break;
			
		case COMPOSITE:
			System.out.println(indent + "------ begin: " + ec.getName() + " -----------------------------------------------------------");
			if (ec.getShortDescription() != null) System.out.println(indent + ec.getShortDescription() + "\n");
			for (Object sec : ec.getDefaultContent()) {
				if (sec instanceof ElementContainer) {
					printElement((ElementContainer) sec, indent + "     ");
					
				}
			}
			if (ec.getDetailedDescription() != null) System.out.println(indent + ec.getDetailedDescription() + "\n");
			System.out.println(indent + "------ end: " + ec.getName() + " -------------------------------------------------------------\n");
			break;
			
		default:
			break;
		}
	}
	
	/**
	 * Create Procedure mask.
	 * 
	 * @return
	 */
	private ElementContainer createProcedureMask() {
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
	private ElementContainer createExclusionCriteriaMask() {
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
	private ElementContainer createSelectionCriteriaMask() {
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
	private ElementContainer createFinishMask() {
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
