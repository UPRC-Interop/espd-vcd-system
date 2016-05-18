package eu.esens.espdvcd.designer.components;

import com.vaadin.ui.ComboBox;
import eu.esens.espdvcd.codelist.Codelists;
import org.apache.commons.lang3.text.WordUtils;

import java.util.*;

public class CurrencyComboBox extends ComboBox {

    public CurrencyComboBox(String title) {
        super(title);

        Iterator<String> iterator = Codelists.Currency.getBiMap().values().iterator();
        while (iterator.hasNext()) {
            String countryName = iterator.next();
            String countryId = Codelists.Currency.getIdForData(countryName);
            addItem(countryId);
            setItemCaption(countryId, WordUtils.capitalize(countryName.toLowerCase()));
        }

        this.setInputPrompt("Currency");
    }
}
