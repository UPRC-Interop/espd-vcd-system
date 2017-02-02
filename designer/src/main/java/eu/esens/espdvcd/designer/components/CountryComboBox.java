/**
 * Created by ixuz on 2/3/16.
 */

package eu.esens.espdvcd.designer.components;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.UI;
import eu.esens.espdvcd.codelist.Codelists;
import org.apache.commons.lang3.text.WordUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            InputStream s;
            try {
                s = ui.getSession().getService().getThemeResourceAsStream(ui, ui.getTheme(), flagPath);
          
                ThemeResource iconResource = new ThemeResource("img/flags_iso/24/" + countryId.toLowerCase() + ".png");
                setItemIcon(countryId, iconResource);
                try {
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                 } catch (IOException ex) {
                 ThemeResource iconResource = new ThemeResource("img/flags_iso/24/null.png");
                setItemIcon(countryId, iconResource);
            }
        }

        this.setInputPrompt("Select country");
    }
}
