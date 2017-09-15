package eu.esens.espdvcd.designer.components;

import com.vaadin.ui.ComboBox;
import eu.esens.espdvcd.codelist.Codelists;
import org.apache.commons.lang3.text.WordUtils;

import java.util.*;

public class CurrencyComboBox extends ComboBox<String> {

    public CurrencyComboBox(String title) {
        super(title, Codelists.forV1.Currency.getBiMap().keySet());
        this.setItemCaptionGenerator(i -> WordUtils.capitalize(Codelists.forV1.Currency.getValueForId(i)));
        this.setPlaceholder("Currency");
    }
}
