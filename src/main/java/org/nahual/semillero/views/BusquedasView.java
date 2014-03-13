package org.nahual.semillero.views;


import com.vaadin.data.Property;
import com.vaadin.data.hbnutil.ContainerFilter;
import com.vaadin.data.hbnutil.HbnContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.nahual.semillero.model.Busqueda;
import org.nahual.semillero.model.Empleador;
import org.nahual.utils.SpringHelper;

import java.util.ArrayList;

public class BusquedasView extends VerticalLayout implements View {

    private static final String CONTAINER_FILTER_ACTIVA = "activa";
    private static final String CONTAINER_FILTER_EMPLEADOR = "empleador";

    private HbnContainer<Busqueda> hbn;
    private Empleador empleador;
    private ComboBox combo;
    private CheckBox activaCB;

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
        combo.setTextInputAllowed(false);
        combo.setImmediate(true);
        combo.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                empleador = (Empleador) ((ComboBox) ((Field.ValueChangeEvent) event).getComponent()).getValue();
                cambiarEmpleador();
            }
        });
        layout.addComponent(combo);


        /* Tabla de empleadores */
        final Table table = new Table();
        table.setWidth("50%");

        hbn = new HbnContainer<Busqueda>(Busqueda.class, SpringHelper.getSession());

        table.setContainerDataSource(hbn);
        table.setVisibleColumns(new Object[]{"titulo", "descripcion", "fechaInicio", "fechaFin", "activa"});
        table.addGeneratedColumn("empresa", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                return hbn.getItem(itemId).getPojo().getEmpleador().getEmpresa();
            }
        });

        if (empleador != null)
            combo.setValue(empleador);

        activaCB = new CheckBox("Busquedas Activas");
        activaCB.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                cambiarFiltroBusquedaActiva();
            }
        });
        activaCB.setImmediate(true);
        activaCB.setValue(true);

        layout.addComponent(activaCB);

        cambiarFiltroBusquedaActiva();


        layout.addComponent(table);

        layout.setMargin(true);
        topLayout.setMargin(true);

        this.addComponent(layout);
    }

    private void cambiarFiltroBusquedaActiva() {
        if (activaCB.getValue())
            hbn.addContainerFilter(new ContainerFilter(CONTAINER_FILTER_ACTIVA) {
                @Override
                public Criterion getFieldCriterion(String fullPropertyName) {
                    return Restrictions.eq(fullPropertyName, activaCB.getValue());
                }
            });
        else
            hbn.removeContainerFilters(CONTAINER_FILTER_ACTIVA);

    }


    private void cargarEmpleadores() {
        HbnContainer<Empleador> hbn = new HbnContainer<Empleador>(Empleador.class, SpringHelper.getBean("sessionFactory", SessionFactory.class));

        ArrayList ids = (ArrayList) hbn.getItemIds();
        for (Object id : ids) {
            if (hbn.getItem(id).getPojo().getActivo())
                combo.addItem((hbn.getItem(id).getPojo()));
        }
    }

    private void cambiarEmpleador() {
        if (this.empleador != null)
            hbn.addContainerFilter(new ContainerFilter(CONTAINER_FILTER_EMPLEADOR) {
                @Override
                public Criterion getFieldCriterion(String fullPropertyName) {
                    return Restrictions.eq(fullPropertyName, empleador);
                }
            });
        else
            hbn.removeContainerFilters(CONTAINER_FILTER_EMPLEADOR);
    }

    public void setEmpleador(final Empleador empleador) {
        this.empleador = empleador;
    }
}