package eu.esens.espdvcd.model.uifacade;

import java.util.Collection;

/**
 *
 */
public interface ElementContainer<T> {
	
	/**
	 * @return type of ui element; for possible types see ElementType
	 */
	ElementType getElementType();
	
	/**
	 * FIXME: probably change type
	 * 
	 * @return id of ui element; used e.g. for cross-referencing of dependent elements
	 */
	String getID();
	
	/**
	 * @return name/title of ui element
	 */
	String getName();
	
	/**
	 * @return short description of ui element
	 */
	String getShortDescription();
	
	/**
	 * @return detailed description or instructions for filling in the ui element
	 */
	String getDetailedDescription();
	
	/**
	 * related element in DG GROW ESPD Service: symbol (i) next to short description
	 * @return hint to be shown e.g. on mouse hover event or dedicated button press
	 */
	String getHint();
	
	/**
	 * This variable determines whether the element is visible in the UI form or not; 
	 * this can change dynamically, e.g. with respect to assignments of related ui elements.  
	 * FIXME: needs to be adapted to the implementation of business rules; there are at least two possibilities:
	 * 1. when invoking isVisible(), the rule behind this element is evaluated
	 * 2. when setting content of the element, all rules associated with this element are evaluated and a visibility 
	 * flag of these elements are set; additionally there might be a signalling function to notify the frontend that 
	 * the content has changed
	 * 
	 * @return (dynamic) visibility of element, might be dependent on values assigned to other related elements
	 */
	boolean isVisible();
	
	/**
	 * @return true, if user is allowed to modify element content
	 */
	boolean isEditable();
	
	/**
	 * @return a collection of default content values, for text fields or checkboxes typically one or null, 
	 * for selection lists more than one
	 */
	Collection<T> getDefaultContent();
	
	/**
	 * @return content
	 */
	T getContent();
	
	/**
	 * set content as filled in by user
	 */
	void setContent(T content);

}
