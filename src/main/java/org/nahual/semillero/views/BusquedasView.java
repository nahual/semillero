package org.nahual.semillero.views;


import com.vaadin.data.Property;
import com.vaadin.data.hbnutil.ContainerFilter;
import com.vaadin.data.hbnutil.HbnContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.nahual.semillero.model.Busqueda;
import org.nahual.semillero.model.Empleador;
import org.nahual.utils.SpringHelper;
import org.nahual.utils.StsContainerFilter;
import org.nahual.utils.StsHbnContainer;

import java.util.ArrayList;
import java.util.Date;

public class BusquedasView extends VerticalLayout implements View {

    private static final String CONTAINER_FILTER_ACTIVA = "activa";
    private static final String CONTAINER_FILTER_EMPLEADOR = "empleador";
    private static final Object CONTAINER_FILTER_FECHA_INICIO = "fecha_inicio";

    private StsHbnContainer<Busqueda> hbn;
    private Empleador empleador;
    private ComboBox combo;
    private CheckBox activaCB;
    private Date fechaInicio;
    private DateField fechaInicioDF;
    private DateField fechaFinDF;
    private Date fechaFin;

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

        hbn = new StsHbnContainer<Busqueda>(Busqueda.class, SpringHelper.getSession());

        hbn.addContainerFilter(new StsContainerFilter("filtro_empleado_activo") {
            @Override
            protected Criteria customizeCriteria(Criteria criteria) {
                return criteria.createAlias("empleador", "empleador")
                        .add(Restrictions.eq("empleador.activo", Boolean.TRUE));
            }
        });

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

        activaCB = new CheckBox("Mostrar solo Busquedas Activas");
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
        fechaInicioDF = new DateField("Fecha Inicio");
        fechaInicioDF.setImmediate(true);
        fechaInicioDF.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                fechaInicio = ((DateField) ((Field.ValueChangeEvent) event).getComponent()).getValue();
                cambiarFechaInicio();
            }
        });
        layout.addComponent(fechaInicioDF);

        fechaFinDF = new DateField("Fecha Fin");
        fechaFinDF.setImmediate(true);
        fechaFinDF.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                fechaFin = ((DateField) ((Field.ValueChangeEvent) event).getComponent()).getValue();
                cambiarFechaFin();
            }
        });
        layout.addComponent(fechaFinDF);
        layout.addComponent(table);

        layout.setMargin(true);
        topLayout.setMargin(true);

        this.addComponent(layout);
    }

    private void cambiarFiltroBusquedaActiva() {
        if (activaCB.getValue())
            hbn.addContainerFilter(new StsContainerFilter(CONTAINER_FILTER_ACTIVA) {
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
            hbn.addContainerFilter(new StsContainerFilter(CONTAINER_FILTER_EMPLEADOR) {
                @Override
                public Criterion getFieldCriterion(String fullPropertyName) {
                    return Restrictions.eq(fullPropertyName, empleador);
                }
            });
        else
            hbn.removeContainerFilters(CONTAINER_FILTER_EMPLEADOR);
    }

    private void cambiarFechaInicio() {
        if (this.fechaInicio != null)
            hbn.addContainerFilter(new StsContainerFilter(CONTAINER_FILTER_FECHA_INICIO) {
                @Override
                public Criterion getFieldCriterion(String fullPropertyName) {
                    return Restrictions.ge("fechaInicio", fechaInicio);
                }
            });
        else
            hbn.removeContainerFilters(CONTAINER_FILTER_FECHA_INICIO);
    }

    private void cambiarFechaFin() {
        if (this.fechaInicio != null)
            hbn.addContainerFilter(new StsContainerFilter(CONTAINER_FILTER_FECHA_INICIO) {
                @Override
                public Criterion getFieldCriterion(String fullPropertyName) {
                    return Restrictions.le("fechaFin", fechaFin);
                }
            });
        else
            hbn.removeContainerFilters(CONTAINER_FILTER_FECHA_INICIO);

    }

    public void setEmpleador(final Empleador empleador) {
        this.empleador = empleador;
    }
}