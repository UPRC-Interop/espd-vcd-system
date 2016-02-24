/**
 * 
 */
package eu.esens.espdvcd.model.uifacade;

/**
 * Type of possible ui elements
 * related to cbc:TypeCode and other requirements
 * 
 * TODO: needs to be discussed and extended
 * 
 * @author Ulf Lotzmann
 *
 */
public enum ElementType {
	COMPOSITE, // contains other element(s) of type ElementContainer; might also contain description, hint etc. to be displayed
	STATICTEXT, // element should be displayed as static text without any user interaction/inputs (except possibly hint/help button...)
	TEXTFIELD, // element should be displayed as a text input field (might be editable or not) 
	SELECTIONLIST, // element should be displayed e.g. as a drop-down menu (with more than two options) or a switch (with two options, e.g. yes/no)
	CHECKBOX, // element should be displayed e.g. as a checkbox
	RADIOLIST // element with a similar semantics as the SELECTIONSLIST, but which should be displayed in a "radio button"-like way
	//TODO: specify any further needed elements 
}
