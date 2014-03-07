package org.nahual.semillero.views;


import com.vaadin.data.hbnutil.ContainerFilter;
import com.vaadin.data.hbnutil.HbnContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.nahual.semillero.model.Busqueda;
import org.nahual.semillero.model.Empleador;
import org.nahual.utils.SpringHelper;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

public class BusquedasView extends VerticalLayout implements View {

    private HbnContainer<Busqueda> hbn;
    private Empleador empleador;

    public BusquedasView() {
        this.setSizeFull();
        this.setMargin(true);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        init();
    }

    private void init() {
        this.removeAllComponents();
        final VerticalLayout layout = new VerticalLayout();
        final HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth("50%");

        Label tituloEmpleadores = new Label("Busquedas");
        tituloEmpleadores.setStyleName("titulo");
        tituloEmpleadores.setHeight("100%");
        layout.addComponent(tituloEmpleadores);
        layout.addComponent(topLayout);


        /* Tabla de empleadores */
        final Table table = new Table();
        table.setWidth("50%");

        hbn = new HbnContainer<Busqueda>(Busqueda.class, SpringHelper.getSession());

        hbn.addContainerFilter(new ContainerFilter("activa") {
            @Override
            public Criterion getFieldCriterion(String fullPropertyName) {
                return Restrictions.eq(fullPropertyName, Boolean.TRUE);
            }
        });

        if (this.empleador != null)
            hbn.addContainerFilter(new ContainerFilter("empleador") {
                @Override
                public Criterion getFieldCriterion(String fullPropertyName) {
                    return Restrictions.eq(fullPropertyName, empleador);
                }
            });
        table.setContainerDataSource(hbn);

        layout.addComponent(table);

        layout.setMargin(true);
        topLayout.setMargin(true);

        this.addComponent(layout);
    }

    public void setEmpleador(final Empleador empleador) {
        this.empleador = empleador;
    }

}