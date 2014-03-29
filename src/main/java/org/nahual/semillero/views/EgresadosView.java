package org.nahual.semillero.views;

import com.vaadin.data.hbnutil.ContainerFilter;
import com.vaadin.data.hbnutil.HbnContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.nahual.semillero.components.ContenedorPrincipalUI;
import org.nahual.semillero.model.Egresado;
import org.nahual.semillero.ui.SemilleroAppUI;
import org.nahual.utils.CvDownloader;
import org.nahual.utils.SpringHelper;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.InputStream;

/**
 * Main view with a menu
 */
public class EgresadosView extends VerticalLayout implements View {

    public EgresadosView() {
        this.setSizeFull();
        this.setMargin(true);
        final VerticalLayout layout = new VerticalLayout();

        Label tituloEgresados = new Label("Egresados");
        layout.addComponent(tituloEgresados);
        tituloEgresados.setStyleName("titulo");

        final HbnContainer<Egresado> hbn = new HbnContainer<Egresado>(Egresado.class, SpringHelper.getSession());

        hbn.addContainerFilter(new ContainerFilter("activo") {
            @Override
            public Criterion getFieldCriterion(String fullPropertyName) {
                return Restrictions.eq(fullPropertyName, Boolean.TRUE);
            }
        });

        final HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth("90%");

        layout.addComponent(topLayout);


        Button botonNuevoEgresado = new Button("Nuevo Egresado");
        botonNuevoEgresado.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                Window window = new Window();
                getUI().addWindow(window);
                window.setModal(true);
                window.setHeight("600px");
                window.setWidth("500px");
                EgresadoView egresadoView = new EgresadoView(hbn);
                egresadoView.setWindow(window);
                window.setContent(egresadoView);
            }
        });
        topLayout.addComponent(botonNuevoEgresado);

         /* busquedaLayout */
        final HorizontalLayout busquedaLayout = new HorizontalLayout();
        topLayout.addComponent(busquedaLayout);
        final TextField campoBusqueda = new TextField();
        Button searchButton = new Button("Buscar");
        searchButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        searchButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                hbn.addContainerFilter(new ContainerFilter("nombre") {
                    @Override
                    public Criterion getFieldCriterion(String fullPropertyName) {
                        String value = "%" + campoBusqueda.getValue() + "%";
                        return Restrictions.or(
                                Restrictions.ilike("telefonoFijo", value),
                                Restrictions.or(
                                        Restrictions.ilike("telefonoMovil", value),
                                        Restrictions.ilike("correoElectronico", value),
                                        Restrictions.ilike("cuatrimestre", value),
                                        Restrictions.ilike("nodo", value),
                                        Restrictions.ilike("observaciones", value)
                                )
                        );
                    }
                });
            }
        });
        topLayout.setComponentAlignment(busquedaLayout, Alignment.TOP_RIGHT);
        busquedaLayout.addComponent(campoBusqueda);
        busquedaLayout.addComponent(searchButton);


          /* Tabla de egresados */
        final Table table = new Table();
        table.setWidth("90%");

        table.setContainerDataSource(hbn);
        table.addContainerProperty("Id", Long.class, null);
        table.setVisibleColumns(new Object[]{"nombre", "telefonoFijo", "telefonoMovil", "correoElectronico", "nodo",
                "cuatrimestre", "observaciones"});

        table.addGeneratedColumn("Acciones", new Table.ColumnGenerator() {

            @Override
            public Object generateCell(final Table source, final Object itemId, Object columnId) {
                HorizontalLayout cell = new HorizontalLayout();

                Button eliminarButton = new Button();
                eliminarButton.setDescription("Eliminar egresado");
                eliminarButton.addClickListener(new Button.ClickListener() {

                    @Override
                    public void buttonClick(Button.ClickEvent event) {

                        TransactionTemplate transactionTemplate = SpringHelper.getBean("transactionTemplate", TransactionTemplate.class);
                        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                            @Override
                            protected void doInTransactionWithoutResult(TransactionStatus status) {
                                Egresado egresadoEliminar = hbn.getItem(itemId).getPojo();
                                egresadoEliminar.setActivo(false);
                                hbn.updateEntity(egresadoEliminar);

                            }
                        });
                    }
                });
                eliminarButton.setStyleName("iconButton");
                eliminarButton.setIcon(new ThemeResource("img/eliminar.png"), "Eliminar egresado");
                cell.addComponent(eliminarButton);

                Button nuevaPostulacionButton = new Button();
                nuevaPostulacionButton.setDescription("Nueva postulacion");
                nuevaPostulacionButton.addClickListener(new Button.ClickListener() {

                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        Egresado egresado = hbn.getItem(itemId).getPojo();
                        PostulacionView postulacionView = new PostulacionView(egresado);

                        Window window = new Window();
                        getUI().addWindow(window);
                        window.setModal(true);
                        window.setHeight("500px");
                        window.setWidth("550px");
                        postulacionView.setWindow(window);
                        window.setContent(postulacionView);
                    }
                });
                nuevaPostulacionButton.setStyleName("iconButton");
                nuevaPostulacionButton.setIcon(new ThemeResource("img/nueva_postulacion.png"), "Nueva postulación");
                cell.addComponent(nuevaPostulacionButton);

                Button postulacionesActivas = new Button();
                postulacionesActivas.setDescription("Postulaciones activas");
                postulacionesActivas.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        Egresado egresado = hbn.getItem(itemId).getPojo();
                        ((SemilleroAppUI) getUI()).getMarco().getPostulacionesView().setEgresado(egresado);
                        getUI().getNavigator().navigateTo(ContenedorPrincipalUI.VIEW_POSTULACIONES);
                    }
                });

                cell.addComponent(postulacionesActivas);
                postulacionesActivas.setStyleName("iconButton");
                postulacionesActivas.setIcon(new ThemeResource("img/postulacion.png"), "Postulaciones activas");

                final Button descargarCV = new Button("Descargar CV");
                postulacionesActivas.setDescription("Descargar CV");
                Egresado egresado = hbn.getItem(itemId).getPojo();
                CvDownloader fileDownloader = new CvDownloader(egresado);
                fileDownloader.extend(descargarCV);
                cell.addComponent(descargarCV);

                return cell;
            }
        });

        layout.addComponent(table);

        table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent event) {
                Window window = new Window();
                getUI().addWindow(window);
                window.setModal(true);
                window.setHeight("600px");
                window.setWidth("500px");
                EgresadoView egresadoView = new EgresadoView(event.getItem());
                egresadoView.setWindow(window);
                window.setContent(egresadoView);
            }
        });

        layout.setMargin(true);
        topLayout.setMargin(true);

        this.addComponent(layout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

}