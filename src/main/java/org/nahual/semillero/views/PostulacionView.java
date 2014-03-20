package org.nahual.semillero.views;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.hbnutil.ContainerFilter;
import com.vaadin.data.hbnutil.HbnContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.nahual.semillero.components.ContenedorPrincipalUI;
import org.nahual.semillero.model.Busqueda;
import org.nahual.semillero.model.Egresado;
import org.nahual.semillero.model.Empleador;
import org.nahual.semillero.model.Postulacion;
import org.nahual.utils.SpringHelper;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;


public class PostulacionView extends VerticalLayout implements View {

    private HbnContainer<Postulacion> hbn;
    private boolean nuevoItem;
    private TextField egresadoTF;
    private ComboBox empleador;
    private ComboBox busqueda;
    private TextArea descripcionTF;
    private CheckBox activaCB;

    private FieldGroup fieldGroup;
    private Window window;

    public PostulacionView(Egresado unEgresado) {
        init(unEgresado);

        this.hbn = new HbnContainer<Postulacion>(Postulacion.class, SpringHelper.getBean("sessionFactory", SessionFactory.class));

        Postulacion postulacion = new Postulacion();
        postulacion.setEgresado(unEgresado);
        postulacion.setActiva(true);
        Item newItem = new BeanItem<Postulacion>(postulacion);
        nuevoItem = true;
        setElemento(newItem);
    }

    private void init(Egresado unEgresado) {
        this.setSizeFull();
        this.setMargin(true);
        this.addComponent(createLayout(unEgresado));
        fieldGroup = new FieldGroup();
        fieldGroup.bind(this.empleador, "empleador");
        fieldGroup.bind(this.busqueda, "busqueda");
        fieldGroup.bind(this.descripcionTF, "descripcion");
        fieldGroup.bind(this.activaCB, "activa");

        // Se aplica un estilo particular a los captions de los fields
        for (Object field : fieldGroup.getFields()) {
            ((AbstractComponent) field).setStyleName("textField");
            if (field instanceof AbstractTextField)
                ((AbstractTextField) field).setNullRepresentation("");
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

    private VerticalLayout createLayout(Egresado unEgresado) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);

        Label tituloPostulacion = new Label("Nueva Postulación");
        tituloPostulacion.setStyleName("titulo");

        layout.addComponent(tituloPostulacion);

        FormLayout fl = new FormLayout();
        layout.addComponent(fl);

        fl.setSizeUndefined();

        egresadoTF = new TextField("Egresado");
        egresadoTF.setValue(unEgresado.getNombre());
        egresadoTF.setReadOnly(true);
        fl.addComponent(egresadoTF);

        empleador = new ComboBox("Empleador");
        empleador.setRequired(true);
        empleador.setRequiredError("El empleador es un dato obligatorio");
        empleador.setImmediate(true);
        this.cargarEmpleadores();
        Property.ValueChangeListener listener = new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                Busqueda valorBusqueda = (Busqueda) busqueda.getValue();
                if ((valorBusqueda == null) || (valorBusqueda != null && valorBusqueda.getEmpleador() != empleador.getValue()))
                    cargarBusquedas((Empleador) empleador.getValue());
            }
        };
        empleador.addValueChangeListener(listener);

        fl.addComponent(empleador);

        busqueda = new ComboBox("Búsqueda");
        busqueda.setImmediate(true);
        // Por defecto cargo todas las búsquedas activas
        this.cargarBusquedas(null);

        // Cuando se selecciona una búsqueda, el campo de empleador se carga automáticamente
        Property.ValueChangeListener listenerBusquedas = new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                Busqueda valorBusqueda = (Busqueda) busqueda.getValue();
                if (valorBusqueda != null) {
                    empleador.select(valorBusqueda.getEmpleador());
                }
            }
        };
        busqueda.addValueChangeListener(listenerBusquedas);
        fl.addComponent(busqueda);

        activaCB = new CheckBox("Activa");
        fl.addComponent(activaCB);

        descripcionTF = new TextArea("Descripción");
        fl.addComponent(descripcionTF);

        Button button = new Button("Aceptar");
        button.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                TransactionTemplate transactionTemplate = SpringHelper.getBean("transactionTemplate", TransactionTemplate.class);
                transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                    @Override
                    protected void doInTransactionWithoutResult(TransactionStatus status) {
                        try {
                            fieldGroup.commit();
                            if (nuevoItem) {
                                hbn.saveEntity(((BeanItem<Postulacion>) fieldGroup.getItemDataSource()).getBean());
                            }

                            if (window != null)
                                window.close();
                            else
                                UI.getCurrent().getNavigator().navigateTo(ContenedorPrincipalUI.VIEW_EGRESADOS);
                        } catch (FieldGroup.CommitException e) {
                            new Notification("Revisar los datos cargados!",
                                    Notification.Type.ERROR_MESSAGE)
                                    .show(Page.getCurrent());
                            e.printStackTrace();
                        }

                    }
                });
            }

        });

        fl.addComponent(button);

        return layout;
    }

    public void setElemento(Item elemento) {
        fieldGroup.setItemDataSource(elemento);
    }

    public Window getWindow() {
        return window;
    }

    public void setWindow(Window window) {
        this.window = window;
    }

    private void cargarEmpleadores() {
        this.empleador.removeAllItems();
        HbnContainer hbn = new HbnContainer<Empleador>(Empleador.class, SpringHelper.getBean("sessionFactory", SessionFactory.class));

        ArrayList ids = (ArrayList) hbn.getItemIds();
        for (Object id : ids) {
            empleador.addItem(((Empleador) (hbn.getItem(id).getPojo())));
        }

    }

    private void cargarBusquedas(final Empleador empleador) {
        this.busqueda.removeAllItems();
        HbnContainer hbn = new HbnContainer<Busqueda>(Busqueda.class, SpringHelper.getBean("sessionFactory", SessionFactory.class));

        // Solamente cargar las búsquedas del empleador dado
        if (empleador != null) {
            hbn.addContainerFilter(new ContainerFilter("empleador") {
                @Override
                public Criterion getFieldCriterion(String fullPropertyName) {
                    return Restrictions.eq(fullPropertyName, empleador);
                }
            });
        }
        // o bien, todas las activas si no se dió un empleador en particular
        else {
            hbn.addContainerFilter(new ContainerFilter("activa") {
                @Override
                public Criterion getFieldCriterion(String fullPropertyName) {
                    return Restrictions.eq(fullPropertyName, Boolean.TRUE);
                }
            });
        }

        ArrayList ids = (ArrayList) hbn.getItemIds();
        for (Object id : ids) {
            busqueda.addItem(((Busqueda) (hbn.getItem(id).getPojo())));
        }

    }

}