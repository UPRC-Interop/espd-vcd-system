package eu.esens.espdvcd.designer.components.requirement;

import eu.esens.espdvcd.model.requirement.response.DescriptionResponse;

public class PeriodResponseForm extends DescriptionResponseForm {

    public PeriodResponseForm(DescriptionResponse descriptionResponse, String caption, boolean readOnly) {
        super(descriptionResponse, caption, readOnly);
        description.setCaption(caption);
        description.setWidth(280, Unit.PIXELS);
    }
}
