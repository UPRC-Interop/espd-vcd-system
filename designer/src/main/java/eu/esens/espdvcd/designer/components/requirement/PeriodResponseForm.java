package eu.esens.espdvcd.designer.components.requirement;

import eu.esens.espdvcd.model.requirement.response.DescriptionResponse;

/**
 * Created by ixuz on 4/11/16.
 */
public class PeriodResponseForm extends DescriptionResponseForm {

    public PeriodResponseForm(DescriptionResponse descriptionResponse, String caption) {
        super(descriptionResponse, caption);
        description.setCaption(caption);
        description.setNullRepresentation("");
        description.setWidth(280, Unit.PIXELS);

    }
}
