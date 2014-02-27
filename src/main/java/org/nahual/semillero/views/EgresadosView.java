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
        Label label = new Label("Egresados");
        this.addComponent(label);

          /* Tabla de empleadores */
        final Table table = new Table();
        table.setWidth("70%");
        final HbnContainer<Egresado> hbn = new HbnContainer<Egresado>(Egresado.class, SpringHelper.getSession());
        table.setContainerDataSource(hbn);
        table.addContainerProperty("Id", Long.class, null);
        table.setVisibleColumns(new Object[]{"nombre", "telefonoFijo", "telefonoMovil", "correoElectronico", "nodo",
                                                "cuatrimestre", "observaciones"});

        this.addComponent(table);

        table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent event) {
                Window window = new Window();
                getUI().addWindow(window);
                window.setModal(true);
                window.setHeight("500px");
                window.setWidth("350px");
                EgresadoView egresadoView = new EgresadoView();
                egresadoView.setElemento(event.getItem());
                egresadoView.setContainer(hbn);
                window.setContent(egresadoView);
            }
        });
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}