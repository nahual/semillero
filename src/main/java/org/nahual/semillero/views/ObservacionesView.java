package org.nahual.semillero.views;

import com.vaadin.data.hbnutil.HbnContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import org.nahual.semillero.model.Empleador;
import org.nahual.semillero.model.Observacion;
import org.nahual.utils.SpringHelper;

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

    public ObservacionesView(Empleador unEmpleador) {
        this.empleador = unEmpleador;
    }

    private VerticalLayout createLayout(final Empleador unEmpleador) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);

        Label tituloObservaciones = new Label("Lista de Observaciones");
        layout.addComponent(tituloObservaciones);

        final Table table = new Table();

        final HbnContainer<Observacion> hbn = new HbnContainer<Observacion>(Observacion.class, SpringHelper.getSession());


        table.setContainerDataSource(hbn);

        return layout;
    }

}
