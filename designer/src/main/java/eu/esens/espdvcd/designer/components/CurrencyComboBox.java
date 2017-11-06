package eu.esens.espdvcd.designer.components;

import com.vaadin.ui.ComboBox;
import eu.esens.espdvcd.codelist.CodeListsVersioner;
import org.apache.commons.lang3.text.WordUtils;

import java.util.*;

public class CurrencyComboBox extends ComboBox<String> {

    public CurrencyComboBox(String title) {
        super(title, CodeListsVersioner.ForVersion1.CURRENCY.getBiMap().keySet());
        this.setItemCaptionGenerator(i -> WordUtils.capitalize(CodeListsVersioner.ForVersion1.CURRENCY.getValueForId(i)));
        this.setPlaceholder("Currency");
    }
}
