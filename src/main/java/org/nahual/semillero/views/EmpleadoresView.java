package org.nahual.semillero.views;

import com.vaadin.data.hbnutil.HbnContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import org.nahual.semillero.components.ContenedorPrincipalUI;
import org.nahual.semillero.model.Empleador;
import org.nahual.utils.SpringHelper;


public class EmpleadoresView extends VerticalLayout implements View {

    public EmpleadoresView() {
        this.setSizeFull();
        this.setMargin(true);

        final VerticalLayout layout = new VerticalLayout();
        final HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth("50%");

        Label tituloEmpleadores = new Label("Empleadores");
        tituloEmpleadores.setStyleName("titulo");
        tituloEmpleadores.setHeight("100%");
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
        final Table table = new Table();
        table.setWidth("50%");
        final HbnContainer<Empleador> hbn = new HbnContainer<Empleador>(Empleador.class, SpringHelper.getSession());
        table.setContainerDataSource(hbn);
        table.addContainerProperty("Id", Long.class, null);

        table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent event) {
                Window window = new Window();
                getUI().addWindow(window);
                window.setModal(true);
                window.setHeight("500px");
                window.setWidth("350px");
                NuevoEmpleadorView empleadorView = new NuevoEmpleadorView();
                empleadorView.setElemento(event.getItem());
                empleadorView.setContainer(hbn);
                window.setContent(empleadorView);
            }
        });

      /*  table.addGeneratedColumn("", new Table.ColumnGenerator() {

            @Override
            public Object generateCell(final Table source, final Object itemId, Object columnId) {

                Button button = new Button("Delete");

                button.addClickListener(new Button.ClickListener() {

                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        hbn.removeItem(itemId);
                        table.removeItem(itemId);
                    }
                });

                return button;
            }
        });    */
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
