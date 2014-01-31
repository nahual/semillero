package org.nahual.semillero.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import org.nahual.semillero.components.ContenedorPrincipalUI;


public class EmpleadoresView extends VerticalLayout implements View {

    public EmpleadoresView() {
        this.setSizeFull();
        this.setMargin(true);

        final VerticalLayout layout = new VerticalLayout();
        final HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth("50%");

        Label tituloEmpleadores = new Label("Empleadores");
        layout.addComponent(tituloEmpleadores);
        layout.addComponent(topLayout);

        /* topLayout */
        Button botonNuevoEmpleador = new Button("Nuevo Empleador");
        botonNuevoEmpleador.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                UI.getCurrent().getNavigator().navigateTo(ContenedorPrincipalUI.VIEW_NUEVO_EMPLEADOR);
            }
        });
        topLayout.addComponent(botonNuevoEmpleador);

        /* busquedaLayout */
        final HorizontalLayout busquedaLayout = new HorizontalLayout();
        topLayout.addComponent(busquedaLayout);
        TextField campoBusqueda = new TextField();
        Button searchButton = new Button("Buscar");
        topLayout.setComponentAlignment(busquedaLayout, Alignment.TOP_RIGHT);
        busquedaLayout.addComponent(campoBusqueda);
        busquedaLayout.addComponent(searchButton);

        /* Tabla de empleadores */
        Table table = new Table();
        table.setWidth("50%");

        table.addContainerProperty("Empleador", String.class, null);
        table.addContainerProperty("Busquedas Activas", String.class, null);
        table.addContainerProperty("Acciones", Integer.class, null);

        layout.addComponent(table);

        layout.setMargin(true);
        topLayout.setMargin(true);

        this.addComponent(layout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
