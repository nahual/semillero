package org.nahual.semillero.views;

import com.vaadin.data.hbnutil.ContainerFilter;
import com.vaadin.data.hbnutil.HbnContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.nahual.semillero.components.ContenedorPrincipalUI;
import org.nahual.semillero.model.Empleador;
import org.nahual.semillero.ui.SemilleroAppUI;
import org.nahual.utils.SpringHelper;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;


public class EmpleadoresView extends VerticalLayout implements View {

    public EmpleadoresView() {
        this.setSizeFull();
        this.setMargin(true);

        final VerticalLayout layout = new VerticalLayout();
        final HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth("50%");

        Label tituloEmpleadores = new Label("Empleadores");
        tituloEmpleadores.setStyleName("titulo");
        tituloEmpleadores.setHeight("100%");
        layout.addComponent(tituloEmpleadores);
        layout.addComponent(topLayout);


        /* Tabla de empleadores */
        final Table table = new Table();

        final HbnContainer<Empleador> hbn = new HbnContainer<Empleador>(Empleador.class, SpringHelper.getSession());

        hbn.addContainerFilter(new ContainerFilter("activo") {
            @Override
            public Criterion getFieldCriterion(String fullPropertyName) {
                return Restrictions.eq(fullPropertyName, Boolean.TRUE);
            }
        });


        table.setContainerDataSource(hbn);
        table.setVisibleColumns(new Object[]{"empresa", "contacto", "observaciones"});

        /* topLayout */
        Button botonNuevoEmpleador = new Button("Nuevo Empleador");
        botonNuevoEmpleador.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                Window window = new Window();
                getUI().addWindow(window);
                window.setModal(true);
                window.setHeight("500px");
                window.setWidth("350px");
                EmpleadorView empleadorView = new EmpleadorView(hbn);
                empleadorView.setWindow(window);
                window.setContent(empleadorView);
            }
        });
        topLayout.addComponent(botonNuevoEmpleador);

        /* busquedaLayout */
        final HorizontalLayout busquedaLayout = new HorizontalLayout();
        topLayout.addComponent(busquedaLayout);
        final TextField campoBusqueda = new TextField();
        Button searchButton = new Button("Buscar");
        searchButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        searchButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                hbn.addContainerFilter(new ContainerFilter("empresa") {
                    @Override
                    public Criterion getFieldCriterion(String fullPropertyName) {
                        String value = "%" + campoBusqueda.getValue() + "%";
                        return Restrictions.or(
                                Restrictions.ilike("contacto", value),
                                Restrictions.or(
                                        Restrictions.ilike("observaciones", value),
                                        Restrictions.ilike("empresa", value)
                                )
                        );
                    }
                });
            }
        });
        topLayout.setComponentAlignment(busquedaLayout, Alignment.TOP_RIGHT);
        busquedaLayout.addComponent(campoBusqueda);
        busquedaLayout.addComponent(searchButton);


        table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent event) {
                Window window = new Window();
                getUI().addWindow(window);
                window.setModal(true);
                window.setHeight("500px");
                window.setWidth("350px");
                EmpleadorView empleadorView = new EmpleadorView(event.getItem());
                empleadorView.setWindow(window);
                window.setContent(empleadorView);
            }
        });

        table.addGeneratedColumn("Acciones", new Table.ColumnGenerator() {

            @Override
            public Object generateCell(final Table source, final Object itemId, Object columnId) {
                HorizontalLayout cell = new HorizontalLayout();

                Button eliminarButton = new Button("");
                eliminarButton.setDescription("Eliminar empleador");
                eliminarButton.addClickListener(new Button.ClickListener() {

                    @Override
                    public void buttonClick(Button.ClickEvent event) {

                        TransactionTemplate transactionTemplate = SpringHelper.getBean("transactionTemplate", TransactionTemplate.class);
                        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                            @Override
                            protected void doInTransactionWithoutResult(TransactionStatus status) {
                                Empleador empleadorEliminar = hbn.getItem(itemId).getPojo();
                                empleadorEliminar.setActivo(false);
                                hbn.updateEntity(empleadorEliminar);

                            }
                        });
                    }
                });
                eliminarButton.setStyleName("iconButton");
                eliminarButton.setIcon(new ThemeResource("img/eliminar.png"), "Eliminar empleador");
                cell.addComponent(eliminarButton);

                Button nuevaBusquedaButton = new Button("");
                nuevaBusquedaButton.setDescription("Ingresar nueva búsqueda");
                nuevaBusquedaButton.addClickListener(new Button.ClickListener() {

                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        Empleador empleador = hbn.getItem(itemId).getPojo();
                        BusquedaView busquedaView = new BusquedaView(empleador);

                        Window window = new Window();
                        getUI().addWindow(window);
                        window.setModal(true);
                        window.setHeight("500px");
                        window.setWidth("350px");
                        busquedaView.setWindow(window);
                        window.setContent(busquedaView);
                    }
                });
                nuevaBusquedaButton.setStyleName("iconButton");
                nuevaBusquedaButton.setIcon(new ThemeResource("img/nueva_busqueda.png"), "Ingresar nueva búsqueda");
                cell.addComponent(nuevaBusquedaButton);

                Button busquedasActivas = new Button("");
                busquedasActivas.setDescription("Ver búsquedas activas del empleador");
                busquedasActivas.addClickListener(new Button.ClickListener() {

                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        Empleador empleador = hbn.getItem(itemId).getPojo();
                        ((SemilleroAppUI) getUI()).getMarco().getBusquedasEmpleadorView().setEmpleador(empleador);
                        getUI().getNavigator().navigateTo(ContenedorPrincipalUI.VIEW_BUSQUEDAS_EMPLEADOR);
                    }
                });
                busquedasActivas.setStyleName("iconButton");
                busquedasActivas.setIcon(new ThemeResource("img/busquedas_activas.png"), "Ver búsquedas activas del empleador");
                cell.addComponent(busquedasActivas);

                Button nuevaObservacionButton = new Button("");
                nuevaObservacionButton.setDescription("Agregar observación");
                nuevaObservacionButton.addClickListener(new Button.ClickListener() {

                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        Empleador empleador = hbn.getItem(itemId).getPojo();
                        ObservacionView observacionView = new ObservacionView(empleador);

                        Window window = new Window();
                        getUI().addWindow(window);
                        window.setModal(true);
                        window.setHeight("500px");
                        window.setWidth("350px");
                        observacionView.setWindow(window);
                        window.setContent(observacionView);
                    }
                });
                nuevaObservacionButton.setStyleName("iconButton");
                nuevaObservacionButton.setIcon(new ThemeResource("img/agregar_observacion.png"), "Agregar observación");
                cell.addComponent(nuevaObservacionButton);

                return cell;
            }
        });

        layout.addComponent(table);

        layout.setMargin(true);
        topLayout.setMargin(true);

        this.addComponent(layout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
