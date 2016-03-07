package eu.esens.espdvcd.designer.views;

import com.vaadin.data.Item;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import eu.esens.espdvcd.designer.UserManager;

/**
 * Created by ixuz on 3/4/16.
 */
public class EspdTemplates extends Master {

    private Table table = new Table(null);

    public EspdTemplates(Navigator navigator) {
        super(navigator, true);

        mainContent.addComponent(table);
        mainContent.setComponentAlignment(table, Alignment.TOP_CENTER);

        table.setPageLength(table.size());
        table.addContainerProperty("Status",  String.class, null);
        table.addContainerProperty("Title",  String.class, null);
        table.addContainerProperty("Time",  String.class, null);
        table.addContainerProperty("Action",  String.class, null);

        {
            Object newItemId = table.addItem();
            Item row1 = table.getItem(newItemId);
            row1.getItemProperty("Status").setValue("Checkbox");
            row1.getItemProperty("Title").setValue("The quick brown fox jumps over the lazy dog");
            row1.getItemProperty("Time").setValue("2015-03-02");
            row1.getItemProperty("Action").setValue("Buttons");
        }
        {
            Object newItemId = table.addItem();
            Item row1 = table.getItem(newItemId);
            row1.getItemProperty("Status").setValue("Checkbox");
            row1.getItemProperty("Title").setValue("Neque porro quisquam est qui dolorem ipsum quia dolor sit amet");
            row1.getItemProperty("Time").setValue("2015-03-03");
            row1.getItemProperty("Action").setValue("Buttons");
        }
        {
            Object newItemId = table.addItem();
            Item row1 = table.getItem(newItemId);
            row1.getItemProperty("Status").setValue("Checkbox");
            row1.getItemProperty("Title").setValue("Lorem ipsum dolor sit amet, consectetur adipiscing elit");
            row1.getItemProperty("Time").setValue("2015-03-01");
            row1.getItemProperty("Action").setValue("Buttons");
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        super.enter(event);
    }
}
