/**
 * Created by ixuz on 2/3/16.
 */

package eu.esens.espdvcd.designer.components;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.ComboBox;
import eu.esens.espdvcd.codelist.Codelists;
import org.apache.commons.lang3.text.WordUtils;
import java.util.*;

public class CountryComboBox extends ComboBox {

    public CountryComboBox(String title) {
        super(title);

        Iterator<String> iterator = Codelists.CountryIdentification.getBiMap().values().iterator();
        while (iterator.hasNext()) {
            String countryName = iterator.next();
            String countryId = Codelists.CountryIdentification.getIdForData(countryName);
            addItem(countryId);
            setItemCaption(countryId, WordUtils.capitalize(countryName.toLowerCase()));

            ThemeResource iconResource = new ThemeResource("img/flags_iso/24/" + countryId.toLowerCase() + ".png");
            setItemIcon(countryId, iconResource);
            //    ThemeResource iconResource = new ThemeResource("img/flags_iso/24/null.png");
            //    setItemIcon(countryId, iconResource);
        }

        this.setInputPrompt("Select country");
    }
}
