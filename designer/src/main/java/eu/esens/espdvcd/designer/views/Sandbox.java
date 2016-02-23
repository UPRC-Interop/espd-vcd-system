package eu.esens.espdvcd.designer.views;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Form;
import com.vaadin.ui.FormLayout;
import com.vaadin.data.fieldgroup.*;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import eu.esens.espdvcd.designer.UserManager;
import eu.esens.espdvcd.designer.components.LoginFormWindow;

/**
 * Created by ixuz on 2/23/16.
 */

public class Sandbox extends Master implements View {
    private Navigator navigator = null;

    // Here is a bean
    public class TestForm extends FormLayout {
        @PropertyId("name")
        TextField greger = new TextField();
        TextField age = new TextField();
        public TestForm() {
            addComponent(greger);
            addComponent(age);
        }
    }

    // Here is a bean
    public class Person {
        @javax.validation.constraints.Size(min=2, max=10)
        String name;
        int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

    public Sandbox(Navigator navigator) {
        super(navigator);

        content.addComponent(new Label("This is the sandbox!"));

        TestForm testForm = new TestForm();
        content.addComponent(testForm);

        // Have a bean
        Person bean = new Person("Mung bean", 100);

        // Form for editing the bean
        final BeanFieldGroup<Person> binder = new BeanFieldGroup<Person>(Person.class);
        binder.bindMemberFields(testForm);
        binder.setItemDataSource(bean);

        // Buffer the form content
        binder.setBuffered(true);
        content.addComponent(new Button("OK", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                try {
                    System.out.println("Name pre : " + bean.getName());
                    binder.commit();
                    System.out.println("Name post: " + bean.getName());
                } catch (CommitException e) {

                }
            }
        }));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
