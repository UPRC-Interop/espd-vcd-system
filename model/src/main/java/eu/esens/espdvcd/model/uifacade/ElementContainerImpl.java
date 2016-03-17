package eu.esens.espdvcd.model.uifacade;

import java.util.Collection;

/**
 *
 */
public class ElementContainerImpl<T> implements ElementContainer<T> {
	
	private ElementType elementType;
	private String id;
	private String name;
	private String shortDescription;
	private String detailedDescription;
	private String hint;
	private boolean visible; // might be replaced by construct using Lambda expression
	private boolean editable;
	private Collection<T> defaultContent;
	private T content;
	
	
	

	public ElementContainerImpl(ElementType elementType, String id, String name, String shortDescription,
			String detailedDescription, String hint, boolean visible, boolean editable, Collection<T> defaultContent, T content) {
		super();
		this.elementType = elementType;
		this.id = id;
		this.name = name;
		this.shortDescription = shortDescription;
		this.detailedDescription = detailedDescription;
		this.hint = hint;
		this.visible = visible;
		this.editable = editable;
		this.defaultContent = defaultContent;
		this.content = content;
	}

	@Override
	public ElementType getElementType() {
		return elementType;
	}

	@Override
	public String getID() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getShortDescription() {
		return shortDescription;
	}

	@Override
	public String getDetailedDescription() {
		return detailedDescription;
	}
	
	@Override
	public String getHint() {
		return hint;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public boolean isEditable() {
		return editable;
	}

	@Override
	public Collection<T> getDefaultContent() {
		return defaultContent;
	}

	@Override
	public T getContent() {
		return content;
	}

	@Override
	public void setContent(T content) {
		this.content = content;
	}

}
