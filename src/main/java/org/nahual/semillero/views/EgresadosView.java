package org.nahual.semillero.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

/** Main view with a menu */
public class EgresadosView extends VerticalLayout implements View {

    public EgresadosView(){
        this.setSizeFull();
        this.setMargin(true);
        Label label = new Label("Egresados");
        this.addComponent(label);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}