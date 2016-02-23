/**
 * Created by ixuz on 2/3/16.
 */

package eu.esens.espdvcd.designer.components;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.ComboBox;
import eu.esens.espdvcd.codelist.CountryCodeGC;

import java.util.*;
import java.io.File;
import java.io.InputStream;

public class CountryComboBox extends ComboBox {

    private HashMap<String, String> countries = new HashMap<String, String>();
    IndexedContainer ic = new IndexedContainer();

    public CountryComboBox(String title, String defaultValue) {
        super(title);

        setContainerDataSource(ic);
        setValue(defaultValue);
    }

    public CountryComboBox(String title, List<String> countries) {
        super(title);

        IndexedContainer ic = new IndexedContainer(countries);
        for (int i=0; i<ic.size(); i++) {
            String country = (String)ic.getIdByIndex(i);
            String isoCode = "";
            try {
                isoCode = CountryCodeGC.getISOCode(country.toUpperCase());
            } catch (IllegalArgumentException e) {
                isoCode = "null";
            }
            addIconItem(country, "img/flags_iso/24/" + isoCode.toLowerCase() + ".png");
        }

        setContainerDataSource(ic);
    }

    public void addIconItem(String name, String iconPath) {
        countries.put(name, iconPath);
        ic.addItem(name);
        ThemeResource iconResource = new ThemeResource(iconPath);
        setItemIcon(name, iconResource);

        if (ic.size() == 1) {
            setValue(name);
        }
    }
}
