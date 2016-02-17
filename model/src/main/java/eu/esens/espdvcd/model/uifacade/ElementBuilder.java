package eu.esens.espdvcd.model.uifacade;

import java.util.Collection;

/**
 * Created by ixuz on 2/16/16.
 */
public class ElementBuilder<T> {
    private ElementType elementType;
    private String id;
    private String name;
    private String shortDescription;
    private String detailedDescription;
    private String hint;
    private Boolean visible;
    private Boolean editable;
    private Collection<T> defaultContent;
    private T content;

    public ElementBuilder() {
        this.elementType         = ElementType.COMPOSITE;
        this.id                  = "";
        this.name                = "";
        this.shortDescription    = null;
        this.detailedDescription = null;
        this.hint                = null;
        this.visible             = true;
        this.editable            = true;
        this.defaultContent      = null;
        this.content             = null;
    }

    public ElementBuilder elementType(ElementType elementType) {
        this.elementType = elementType;
        return this;
    }

    public ElementBuilder id(String id) {
        this.id = id;
        return this;
    }

    public ElementBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ElementBuilder shortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
        return this;
    }

    public ElementBuilder detailedDescription(String detailedDescription) {
        this.detailedDescription = detailedDescription;
        return this;
    }

    public ElementBuilder hint(String hint) {
        this.hint = hint;
        return this;
    }

    public ElementBuilder visible(Boolean visible) {
        this.visible = visible;
        return this;
    }

    public ElementBuilder editable(Boolean editable) {
        this.editable = editable;
        return this;
    }


    public ElementBuilder defaultContent(Collection<T> defaultContent) {
        this.defaultContent = defaultContent;
        return this;
    }

    public ElementBuilder content(T content) {
        this.content = content;
        return this;
    }

    public ElementContainer build() {
        ElementContainer element = new ElementContainerImpl(elementType,
            id,
            name,
            shortDescription,
            detailedDescription,
            hint,
            visible,
            editable,
            defaultContent,
            content);
        return element;
    }
}
