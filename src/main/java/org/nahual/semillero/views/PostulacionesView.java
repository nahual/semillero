package org.nahual.semillero.views;


import com.vaadin.data.Property;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.nahual.semillero.model.Busqueda;
import org.nahual.semillero.model.Egresado;
import org.nahual.semillero.model.Empleador;
import org.nahual.semillero.model.Postulacion;
import org.nahual.utils.SpringHelper;
import org.nahual.utils.StsContainerFilter;
import org.nahual.utils.StsHbnContainer;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;

public class PostulacionesView extends VerticalLayout implements View {

    public static final String COLUMNA_FECHA = "fecha";
    public static final String COLUMN_ACTIVA = "activa";
    public static final String COLUMN_EGRESADO = "egresado";
    public static final String COLUMN_EMPLEADOR = "empleador";
    public static final String COLUMN_DESCRIPCION = "descripcion";
    public static final String COLUMN_EXITOSA = "exitosa";
    public static final String COLUMN_ACCIONES = "acciones";

    private static final String CONTAINER_FILTER_ACTIVA = COLUMN_ACTIVA;
    private static final String CONTAINER_FILTER_EGRESADO = COLUMN_EGRESADO;

    public static final String CRUD_TABLE_WIDTH = "90%";
    public static final String COLUMN_BUSQUEDA = "busqueda";


    private StsHbnContainer<Postulacion> hbn;
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
        hbn = new StsHbnContainer<Postulacion>(Postulacion.class, SpringHelper.getSession());
        hbn.sort(new String[]{"fecha"}, new boolean[]{false});

        this.removeAllComponents();
        final VerticalLayout layout = new VerticalLayout();
        final HorizontalLayout topLayout = new HorizontalLayout();

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
        table.setWidth(CRUD_TABLE_WIDTH);

        table.setContainerDataSource(hbn);

        table.addGeneratedColumn(COLUMN_EGRESADO, new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                return hbn.getItem(itemId).getPojo().getEgresado().getNombre();
            }
        });

        table.addGeneratedColumn(COLUMN_EMPLEADOR, new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                Empleador empleador = hbn.getItem(itemId).getPojo().getEmpleador();
                if (empleador != null)
                    return empleador.getEmpresa();
                return null;
            }
        });

        table.addGeneratedColumn(COLUMN_DESCRIPCION, new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                return hbn.getItem(itemId).getPojo().getDescripcion();
            }
        });

        table.addGeneratedColumn(COLUMNA_FECHA, new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                return hbn.getItem(itemId).getPojo().getFecha();
            }
        });

        table.addGeneratedColumn(COLUMN_BUSQUEDA, new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                Busqueda busqueda = hbn.getItem(itemId).getPojo().getBusqueda();
                if (busqueda != null)
                    return busqueda.toString();
                return null;
            }
        });

        table.addGeneratedColumn(COLUMN_ACTIVA, new Table.ColumnGenerator() {
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

        table.addGeneratedColumn(COLUMN_EXITOSA, new Table.ColumnGenerator() {
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

        table.addGeneratedColumn(COLUMN_ACCIONES, new Table.ColumnGenerator() {

            @Override
            public Object generateCell(final Table source, final Object itemId, Object columnId) {
                HorizontalLayout cell = new HorizontalLayout();

                Button nuevaObservacionButton = new Button();
                nuevaObservacionButton.setDescription("Agregar feedback");
                nuevaObservacionButton.addClickListener(new Button.ClickListener() {

                    @Override
                    public void buttonClick(Button.ClickEvent event) {

                        Postulacion postulacion = hbn.getItem(itemId).getPojo();
                        FeedbackView feedbackView = new FeedbackView(postulacion);
                        Window window = new Window();
                        getUI().addWindow(window);
                        window.setModal(true);
                        window.setHeight("500px");
                        window.setWidth("350px");
                        feedbackView.setWindow(window);
                        window.setContent(feedbackView);
                    }
                });
                nuevaObservacionButton.setStyleName("iconButton");
                nuevaObservacionButton.setIcon(new ThemeResource("img/agregar_observacion.png"), "Agregar feedback");
                cell.addComponent(nuevaObservacionButton);

                Button verFeedbacksButton = new Button();
                verFeedbacksButton.setDescription("Ver feedback");
                verFeedbacksButton.addClickListener(new Button.ClickListener() {

                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        Postulacion postulacion = hbn.getItem(itemId).getPojo();
                        FeedbacksView feedbacksView = new FeedbacksView(postulacion);

                        Window window = new Window();
                        getUI().addWindow(window);
                        window.setModal(true);
                        window.setHeight("300px");
                        window.setWidth("500px");
                        feedbacksView.setWindow(window);
                        window.setContent(feedbacksView);
                    }
                });

                verFeedbacksButton.setStyleName("iconButton");
                verFeedbacksButton.setIcon(new ThemeResource("img/observaciones.png"), "Ver feedbacks");
                cell.addComponent(verFeedbacksButton);

                return cell;
            }
        });

        table.setVisibleColumns(COLUMNA_FECHA, COLUMN_ACTIVA, COLUMN_EGRESADO, COLUMN_EMPLEADOR, COLUMN_BUSQUEDA, COLUMN_DESCRIPCION, COLUMN_EXITOSA, COLUMN_ACCIONES);

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
                window.setHeight("500px");
                window.setWidth("550px");
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
            hbn.addContainerFilter(new StsContainerFilter(CONTAINER_FILTER_ACTIVA) {
                @Override
                public Criterion getFieldCriterion(String fullPropertyName) {
                    return Restrictions.eq(fullPropertyName, activaCB.getValue());
                }
            });
        else
            hbn.removeContainerFilters(CONTAINER_FILTER_ACTIVA);
    }

    private void cargarEgresados() {
        StsHbnContainer<Egresado> hbn = new StsHbnContainer<Egresado>(Egresado.class, SpringHelper.getSession());

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
            hbn.addContainerFilter(new StsContainerFilter(CONTAINER_FILTER_EGRESADO) {
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