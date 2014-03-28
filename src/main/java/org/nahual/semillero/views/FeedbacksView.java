package org.nahual.semillero.views;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import org.nahual.semillero.model.Feedback;
import org.nahual.semillero.model.Postulacion;

/**
 * Created by Jose Sanchez on 3/27/14.
 */
public class FeedbacksView extends VerticalLayout implements View {

    private Window window;
    private Postulacion postulacion;


    public Window getWindow() {
        return window;
    }

    public void setWindow(Window window) {
        this.window = window;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }

    private void init(Postulacion unaPostulacion) {
        this.setSizeFull();
        this.setMargin(true);
        this.addComponent(createLayout(unaPostulacion));
    }

    public FeedbacksView(Postulacion unaPostulacion) {

        this.postulacion = unaPostulacion;
        init(unaPostulacion);
    }

    private VerticalLayout createLayout(final Postulacion unaPostulacion) {

        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSizeFull();

        Label tituloObservaciones = new Label("Lista de Feedback");
        tituloObservaciones.setStyleName("titulo");
        layout.addComponent(tituloObservaciones);

        final Table table = new Table();

        final BeanItemContainer<Feedback> beanItemContainer = new BeanItemContainer<Feedback>(Feedback.class);
        beanItemContainer.addAll(unaPostulacion.getFeedbacks());

        table.setContainerDataSource(beanItemContainer);
        table.setColumnHeaders(new String[]{"Fecha", "Feedback"});
        table.setSortContainerPropertyId("fecha");
        table.setSortAscending(true);
        table.sort();

        table.setStyleName("wordwrap-table");

        layout.addComponent(table);

        layout.setExpandRatio(table, 1.0f);
        int count = table.getVisibleItemIds().size();
        table.setPageLength(count);

        return layout;
    }

}
