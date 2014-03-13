package org.nahual.semillero.views;


import com.vaadin.data.Property;
import com.vaadin.data.hbnutil.ContainerFilter;
import com.vaadin.data.hbnutil.HbnContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.nahual.semillero.model.Busqueda;
import org.nahual.semillero.model.Cuatrimestre;
import org.nahual.semillero.model.Empleador;
import org.nahual.utils.SpringHelper;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;

public class BusquedasView extends VerticalLayout implements View {

    private HbnContainer<Busqueda> hbn;
    private Empleador empleador;
    private ComboBox combo;

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
        combo = new ComboBox("Empleador");
        this.cargarEmpleadores();
        layout.addComponent(combo);
        combo.setTextInputAllowed(false);
        combo.setImmediate(true);
        combo.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                empleador = (Empleador) ((ComboBox) ((Field.ValueChangeEvent) event).getComponent()).getValue();
                changeEmpleador();
            }
        });


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

        if (empleador != null)
            combo.setValue(empleador);
        table.setContainerDataSource(hbn);

        layout.addComponent(table);

        layout.setMargin(true);
        topLayout.setMargin(true);

        this.addComponent(layout);
    }


    public void setEmpleador(final Empleador empleador) {
        this.empleador = empleador;
    }

    private void cargarEmpleadores() {
        HbnContainer<Empleador> hbn = new HbnContainer<Empleador>(Empleador.class, SpringHelper.getBean("sessionFactory", SessionFactory.class));

        ArrayList ids = (ArrayList) hbn.getItemIds();
        for (Object id : ids) {
            if (hbn.getItem(id).getPojo().getActivo())
                combo.addItem((hbn.getItem(id).getPojo()));
        }
    }

    private void changeEmpleador() {
        if (this.empleador != null)
            hbn.addContainerFilter(new ContainerFilter("empleador") {
                @Override
                public Criterion getFieldCriterion(String fullPropertyName) {
                    return Restrictions.eq(fullPropertyName, empleador);
                }
            });
        else
            hbn.removeContainerFilters("empleador");
    }
}