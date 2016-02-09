/**
 * Created by ixuz on 2/3/16.
 */

package eu.esens.espdvcd.components;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.ComboBox;

import java.util.*;

public class CountryComboBox extends ComboBox {

    public CountryComboBox(String title, String defaultValue) {
        super(title);

        HashMap<String, String> countries = new HashMap<String, String>();
        countries.put("Denmark", "img/flags_iso/24/dk.png");
        countries.put("Finland", "img/flags_iso/24/fi.png");
        countries.put("Sweden", "img/flags_iso/24/se.png");
        countries.put("Norway", "img/flags_iso/24/no.png");

        IndexedContainer ic = new IndexedContainer();
        setContainerDataSource(ic);
        //setValue(ic.getItemIds().iterator().next());

        for(Map.Entry<String, String> entry : countries.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            ic.addItem(key);
            setItemIcon(key, new ThemeResource(value));
        }
        setValue(defaultValue);
    }
}
