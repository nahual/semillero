package org.nahual.semillero.views;


import com.vaadin.data.Property;
import com.vaadin.data.hbnutil.ContainerFilter;
import com.vaadin.data.hbnutil.HbnContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.nahual.semillero.model.Busqueda;
import org.nahual.semillero.model.Egresado;
import org.nahual.semillero.model.Empleador;
import org.nahual.semillero.model.Postulacion;
import org.nahual.utils.SpringHelper;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

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
        egresadoCB.addStyleName("margins");
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
        //cambiarEgresado();
        egresadoCB.setValue(egresado);
        layout.addComponent(egresadoCB);

        /* Tabla de postulaciones */
        final Table table = new Table();
        table.setWidth("75%");

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

        table.addGeneratedColumn("activa", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                final CheckBox activo = new CheckBox("");
                activo.setValue(hbn.getItem(itemId).getPojo().getActiva());
                activo.setImmediate(true);
                activo.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        TransactionTemplate transactionTemplate = SpringHelper.getBean("transactionTemplate", TransactionTemplate.class);
                        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                            @Override
                            protected void doInTransactionWithoutResult(TransactionStatus status) {
                                Postulacion postulacion = hbn.getItem(itemId).getPojo();
                                postulacion.setActiva(activo.getValue());
                                hbn.updateEntity(postulacion);

                            }
                        });
                    }
                });
                return activo;
            }
        });

        table.addGeneratedColumn("exitosa", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                final CheckBox exitosa = new CheckBox("");
                exitosa.setValue(hbn.getItem(itemId).getPojo().getExitosa());
                exitosa.setImmediate(true);
                exitosa.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        TransactionTemplate transactionTemplate = SpringHelper.getBean("transactionTemplate", TransactionTemplate.class);
                        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                            @Override
                            protected void doInTransactionWithoutResult(TransactionStatus status) {
                                Postulacion postulacion = hbn.getItem(itemId).getPojo();
                                postulacion.setExitosa(exitosa.getValue());
                                hbn.updateEntity(postulacion);

                            }
                        });
                    }
                });
                return exitosa;
            }
        });

        activaCB = new CheckBox("Mostrar solo postulaciones activas");
        activaCB.addStyleName("margins");
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

        // Edición de postulaciones
        table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent event) {
                Window window = new Window();
                getUI().addWindow(window);
                window.setModal(true);
                window.setHeight("600px");
                window.setWidth("500px");
                PostulacionView postulacionView = new PostulacionView(event.getItem());
                postulacionView.setWindow(window);
                window.setContent(postulacionView);
            }
        });

        layout.setMargin(true);
        topLayout.setMargin(true);

        this.addComponent(layout);

    }

    private void cambiarFiltroPostulacionActiva() {
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

    private void cargarEgresados() {
        HbnContainer<Egresado> hbn = new HbnContainer<Egresado>(Egresado.class, SpringHelper.getBean("sessionFactory", SessionFactory.class));

        ArrayList ids = (ArrayList) hbn.getItemIds();
        for (Object id : ids) {
            if (hbn.getItem(id).getPojo().getActivo()) {
                Egresado tmp = (hbn.getItem(id).getPojo());
                egresadoCB.addItem(tmp);
            }
        }
    }

    private void cambiarEgresado() {
        if (egresado != null)
            hbn.addContainerFilter(new ContainerFilter(CONTAINER_FILTER_EGRESADO) {
                @Override
                public Criterion getFieldCriterion(String fullPropertyName) {
                    return Restrictions.eq(fullPropertyName, egresado);
                }
            });
        else
            hbn.removeContainerFilters(CONTAINER_FILTER_EGRESADO);
    }

    public void setEgresado(final Egresado egresado) {
        this.egresado = egresado;
    }

}