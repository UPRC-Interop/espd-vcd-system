/**
 * Created by ixuz on 2/3/16.
 */

package eu.esens.espdvcd.designer.components;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.ComboBox;
import eu.esens.espdvcd.codelist.CodelistsV1;
import org.apache.commons.lang3.text.WordUtils;

public class CountryComboBox extends ComboBox<String> {

    public CountryComboBox(String title) {
        super(title, CodelistsV1.CountryIdentification.getDataMap().keySet());
        this.setItemCaptionGenerator(i -> WordUtils.capitalize(CodelistsV1.CountryIdentification.getValueForId(i)));
        this.setItemIconGenerator(this::getIconForCountryId);
        this.setPlaceholder("Select country");
    }
    
    private ThemeResource getIconForCountryId(String countryId) {
         return new ThemeResource("img/flags_iso/24/" + countryId.toLowerCase() + ".png");        
    }        
}
