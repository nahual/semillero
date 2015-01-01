package org.nahual.semillero.views;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import org.nahual.semillero.model.Empleador;
import org.nahual.semillero.model.Observacion;

/**
 * Created by Jose Sanchez on 3/14/14.
 */
public class ObservacionesView extends VerticalLayout implements View{

    private Window window;
    private Empleador empleador;


    public Window getWindow() {
        return window;
    }

    public void setWindow(Window window) {
        this.window = window;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }

    private void init(Empleador unEmpleador) {
        this.setSizeFull();
        this.setMargin(true);
        this.addComponent(createLayout(unEmpleador));
    }

    public ObservacionesView(Empleador unEmpleador) {
        this.empleador = unEmpleador;
        init(unEmpleador);
    }

    private VerticalLayout createLayout(final Empleador unEmpleador) {

        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSizeFull();

        Label tituloObservaciones = new Label("Lista de Observaciones");
        tituloObservaciones.setStyleName("titulo");
        layout.addComponent(tituloObservaciones);

        final Table table = new Table();

        final BeanItemContainer<Observacion> beanItemContainer = new BeanItemContainer<Observacion>(Observacion.class);
        beanItemContainer.addAll(unEmpleador.getObservaciones());

        table.setContainerDataSource(beanItemContainer);
        table.setColumnHeaders(new String[]{"Fecha", "Observación"});
        table.setSortContainerPropertyId("fecha");
        table.setSortAscending(false);
        table.sort();

        table.setStyleName("wordwrap-table");

        layout.addComponent(table);

        layout.setExpandRatio(table, 1.0f);
         int count = table.getVisibleItemIds().size();
        table.setPageLength(count);

        return layout;
    }

}
