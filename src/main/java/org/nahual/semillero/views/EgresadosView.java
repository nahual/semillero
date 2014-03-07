package org.nahual.semillero.views;

import com.vaadin.data.hbnutil.HbnContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import org.nahual.semillero.model.Egresado;
import org.nahual.semillero.model.Empleador;
import org.nahual.utils.SpringHelper;

/** Main view with a menu */
public class EgresadosView extends VerticalLayout implements View {

    public EgresadosView(){
        this.setSizeFull();
        this.setMargin(true);
        final VerticalLayout layout = new VerticalLayout();

        Label label = new Label("Egresados");
        layout.addComponent(label);


        final HbnContainer<Egresado> hbn = new HbnContainer<Egresado>(Egresado.class, SpringHelper.getSession());
        final HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth("50%");

        layout.addComponent(topLayout);


        Button botonNuevoEgresado = new Button("Nuevo Egresado");
        botonNuevoEgresado.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                Window window = new Window();
                getUI().addWindow(window);
                window.setModal(true);
                window.setHeight("600px");
                window.setWidth("500px");
                EgresadoView egresadoView = new EgresadoView(hbn);
                egresadoView.setWindow(window);
                window.setContent(egresadoView);
            }
        });
        topLayout.addComponent(botonNuevoEgresado);

          /* Tabla de egresados */
        final Table table = new Table();
        table.setWidth("70%");

        table.setContainerDataSource(hbn);
        table.addContainerProperty("Id", Long.class, null);
        table.setVisibleColumns(new Object[]{"nombre", "telefonoFijo", "telefonoMovil", "correoElectronico", "nodo",
                                                "cuatrimestre", "observaciones"});

        layout.addComponent(table);

        table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent event) {
                Window window = new Window();
                getUI().addWindow(window);
                window.setModal(true);
                window.setHeight("600px");
                window.setWidth("500px");
                EgresadoView egresadoView = new EgresadoView();
                egresadoView.setElemento(event.getItem());
                egresadoView.setContainer(hbn);
                window.setContent(egresadoView);
            }
        });


        layout.setMargin(true);
        topLayout.setMargin(true);

        this.addComponent(layout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}