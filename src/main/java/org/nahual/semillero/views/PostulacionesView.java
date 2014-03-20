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
import org.nahual.semillero.model.Egresado;
import org.nahual.semillero.model.Empleador;
import org.nahual.semillero.model.Postulacion;
import org.nahual.utils.SpringHelper;

import java.util.ArrayList;

public class PostulacionesView extends VerticalLayout implements View {

    private static final String CONTAINER_FILTER_ACTIVA = "activa";
    private static final String CONTAINER_FILTER_EGRESADO = "egresado";

    private HbnContainer<Postulacion> hbn;
    private Egresado egresado;
    private CheckBox activaCB;
    private ComboBox egresadoCB;

    public PostulacionesView() {
        this.setSizeFull();
        this.setMargin(true);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        init();
    }

    private void init() {
        hbn = new HbnContainer<Postulacion>(Postulacion.class, SpringHelper.getSession());

        this.removeAllComponents();
        final VerticalLayout layout = new VerticalLayout();
        final HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth("50%");

        Label tituloPostulaciones = new Label("Postulaciones");
        tituloPostulaciones.setStyleName("titulo");
        tituloPostulaciones.setHeight("100%");
        layout.addComponent(tituloPostulaciones);
        layout.addComponent(topLayout);

        egresadoCB = new ComboBox("Egresado");
        this.cargarEgresados();
        egresadoCB.setTextInputAllowed(false);
        egresadoCB.setImmediate(true);
        egresadoCB.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                egresado = (Egresado) ((ComboBox) ((Field.ValueChangeEvent) event).getComponent()).getValue();
                cambiarEgresado();
            }
        });
        cambiarEgresado();
        layout.addComponent(egresadoCB);

        /* Tabla de postulaciones */
        final Table table = new Table();
        table.setWidth("50%");

        table.setContainerDataSource(hbn);
        table.setVisibleColumns(new Object[]{"descripcion"});

        table.addGeneratedColumn("egresado", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                return hbn.getItem(itemId).getPojo().getEgresado().getNombre();
            }
        });
        table.addGeneratedColumn("empleador", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                Empleador empleador = hbn.getItem(itemId).getPojo().getEmpleador();
                if (empleador != null)
                    return empleador.getEmpresa();
                return null;
            }
        });
        table.addGeneratedColumn("busqueda", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                Busqueda busqueda = hbn.getItem(itemId).getPojo().getBusqueda();
                if (busqueda != null)
                    return busqueda.toString();
                return null;
            }
        });

        activaCB = new CheckBox("Postulaciones Activas");
        activaCB.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                cambiarFiltroPostulacionActiva();
            }
        });
        activaCB.setImmediate(true);
        activaCB.setValue(true);
        cambiarFiltroPostulacionActiva();

        layout.addComponent(activaCB);

        layout.addComponent(table);

        layout.setMargin(true);
        topLayout.setMargin(true);

        this.addComponent(layout);

    }

    private void cambiarFiltroPostulacionActiva() {
        hbn.addContainerFilter(new ContainerFilter(CONTAINER_FILTER_ACTIVA) {
            @Override
            public Criterion getFieldCriterion(String fullPropertyName) {
                return Restrictions.eq(fullPropertyName, activaCB.getValue());
            }
        });
    }

    private void cargarEgresados() {
        HbnContainer<Egresado> hbn = new HbnContainer<Egresado>(Egresado.class, SpringHelper.getBean("sessionFactory", SessionFactory.class));

        ArrayList ids = (ArrayList) hbn.getItemIds();
        for (Object id : ids) {
            if (hbn.getItem(id).getPojo().getActivo()) {
                Egresado tmp = (hbn.getItem(id).getPojo());
                egresadoCB.addItem(tmp);
            }
        }

        egresadoCB.select(egresado);
    }

    private void cambiarEgresado() {
        hbn.addContainerFilter(new ContainerFilter(CONTAINER_FILTER_EGRESADO) {
            @Override
            public Criterion getFieldCriterion(String fullPropertyName) {
                return Restrictions.eq(fullPropertyName, egresado);
            }
        });
    }

    public void setEgresado(final Egresado egresado) {
        this.egresado = egresado;
    }

}