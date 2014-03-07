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
import org.nahual.semillero.model.Egresado;
import org.nahual.semillero.model.Empleador;
import org.nahual.utils.SpringHelper;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

/** Main view with a menu */
public class EgresadosView extends VerticalLayout implements View {

    public EgresadosView(){
        this.setSizeFull();
        this.setMargin(true);
        final VerticalLayout layout = new VerticalLayout();

        Label label = new Label("Egresados");
        layout.addComponent(label);


        final HbnContainer<Egresado> hbn = new HbnContainer<Egresado>(Egresado.class, SpringHelper.getSession());

        hbn.addContainerFilter(new ContainerFilter("activo") {
            @Override
            public Criterion getFieldCriterion(String fullPropertyName) {
                return Restrictions.eq(fullPropertyName, Boolean.TRUE);
            }
        });

        final HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth("50%");

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
        table.setWidth("70%");

        table.setContainerDataSource(hbn);
        table.addContainerProperty("Id", Long.class, null);
        table.setVisibleColumns(new Object[]{"nombre", "telefonoFijo", "telefonoMovil", "correoElectronico", "nodo",
                                                "cuatrimestre", "observaciones"});

        table.addGeneratedColumn("Acciones", new Table.ColumnGenerator() {

            @Override
            public Object generateCell(final Table source, final Object itemId, Object columnId) {
                HorizontalLayout cell = new HorizontalLayout();

                Button eliminarButton = new Button("Eliminar");

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
                cell.addComponent(eliminarButton);

//                Button nuevaBusquedaButton = new Button("Nueva Búsqueda");
//
//                nuevaBusquedaButton.addClickListener(new Button.ClickListener() {
//
//                    @Override
//                    public void buttonClick(Button.ClickEvent event) {
//                        Empleador empleador = hbn.getItem(itemId).getPojo();
//                        BusquedaView busquedaView = new BusquedaView(empleador);
//
//                        Window window = new Window();
//                        getUI().addWindow(window);
//                        window.setModal(true);
//                        window.setHeight("500px");
//                        window.setWidth("350px");
//                        busquedaView.setWindow(window);
//                        window.setContent(busquedaView);
//                    }
//                });
//                cell.addComponent(nuevaBusquedaButton);

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
                EgresadoView egresadoView = new EgresadoView();
                egresadoView.setElemento(event.getItem());
                egresadoView.setContainer(hbn);
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