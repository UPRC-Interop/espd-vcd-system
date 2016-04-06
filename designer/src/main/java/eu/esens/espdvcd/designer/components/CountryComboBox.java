/**
 * Created by ixuz on 2/3/16.
 */

package eu.esens.espdvcd.designer.components;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.UI;
import eu.esens.espdvcd.codelist.Codelists;
import org.apache.commons.lang3.text.WordUtils;

import java.io.File;
import java.io.InputStream;
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
            String flagPath = "img/flags_iso/24/" + countryId.toLowerCase() + ".png";

            UI ui = UI.getCurrent();
            InputStream s = ui.getSession().getService().getThemeResourceAsStream(ui, ui.getTheme(), flagPath);
            if (s != null) {
                ThemeResource iconResource = new ThemeResource("img/flags_iso/24/" + countryId.toLowerCase() + ".png");
                setItemIcon(countryId, iconResource);
            } else {
                ThemeResource iconResource = new ThemeResource("img/flags_iso/24/null.png");
                setItemIcon(countryId, iconResource);
            }
        }

        this.setInputPrompt("Select country");
    }
}
