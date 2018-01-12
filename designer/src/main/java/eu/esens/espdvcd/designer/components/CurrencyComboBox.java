package eu.esens.espdvcd.designer.components;

import com.vaadin.ui.ComboBox;
import eu.esens.espdvcd.codelist.CodeListV1;
import org.apache.commons.lang3.text.WordUtils;

public class CurrencyComboBox extends ComboBox<String> {

    public CurrencyComboBox(String title) {
        super(title, CodeListV1.Currency.getBiMap().keySet());
        this.setItemCaptionGenerator(i -> WordUtils.capitalize(CodeListV1.Currency.getValueForId(i)));
        this.setPlaceholder("Currency");
    }
}
