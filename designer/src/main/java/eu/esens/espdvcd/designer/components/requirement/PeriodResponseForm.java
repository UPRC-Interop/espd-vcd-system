package eu.esens.espdvcd.designer.components.requirement;

import eu.esens.espdvcd.model.requirement.DescriptionResponse;

/**
 * Created by ixuz on 4/11/16.
 */
public class PeriodResponseForm extends DescriptionResponseForm {

    public PeriodResponseForm(DescriptionResponse descriptionResponse) {
        super(descriptionResponse);
        description.setCaption("Period: ");
    }
}
