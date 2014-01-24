package org.nahual.semillero.views;


import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class BusquedasView extends VerticalLayout implements View {

    public BusquedasView(){
        this.setSizeFull();
        this.setMargin(true);
        Label label = new Label("Busquedas");
        this.addComponent(label);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}