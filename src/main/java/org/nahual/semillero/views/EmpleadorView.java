package org.nahual.semillero.views;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.hbnutil.HbnContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import org.hibernate.SessionFactory;
import org.nahual.semillero.components.ContenedorPrincipalUI;
import org.nahual.semillero.model.Busqueda;
import org.nahual.semillero.model.Empleador;
import org.nahual.utils.SpringHelper;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Collection;


public class EmpleadorView extends VerticalLayout implements View {
    private boolean nuevoItem;
    private String title;
    private HbnContainer<Empleador> hbn;
    private TextField empresaTF;
    private TextField contactoTF;
    private FieldGroup fieldGroup;

    public EmpleadorView(HbnContainer<Empleador> hbn) {
        this.title = "Nuevo Empleador";
        init();
        this.hbn = hbn;
        Empleador empleador = new Empleador();
        empleador.setActivo(true);
        Item newItem = new BeanItem<Empleador>(empleador);
        this.nuevoItem = true;
        setElemento(newItem);
    }

    public Window getWindow() {
        return window;
    }

    public void setWindow(Window window) {
        this.window = window;
    }

    public Window window;

    public EmpleadorView() {
        this(new HbnContainer<Empleador>(Empleador.class, SpringHelper.getBean("sessionFactory", SessionFactory.class)));

    }

    private void init() {
        this.setSizeFull();
        this.setMargin(true);
        this.addComponent(createLayout());
        fieldGroup = new FieldGroup();
        fieldGroup.bind(this.empresaTF, "empresa");
        fieldGroup.bind(this.contactoTF, "contacto");
        Collection<Field<?>> fields = fieldGroup.getFields();
        for (Field<?> field : fields) {
            if (field instanceof AbstractTextField) {
                ((AbstractTextField) field).setNullRepresentation("");
            }
        }
    }

    public EmpleadorView(Item item) {
        this.title = "Editar Empleador";
        init();
        this.nuevoItem = false;
        setElemento(item);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        this.empresaTF.setValue("");
        this.contactoTF.setValue("");
    }

    private VerticalLayout createLayout() {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);

        Label tituloEmpleador = new Label(this.title);
        tituloEmpleador.setStyleName("titulo");

        layout.addComponent(tituloEmpleador);

        FormLayout fl = new FormLayout();
        layout.addComponent(fl);

        fl.setSizeUndefined();

        empresaTF = new TextField("Empresa");
        fl.addComponent(empresaTF);


        empresaTF.setRequired(true);
        empresaTF.setRequiredError("Empresa no puede estar vacio");

        contactoTF = new TextField("Contacto");
        fl.addComponent(contactoTF);

        contactoTF.setRequired(true);
        contactoTF.setRequiredError("Contacto no puede estar vacio");

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
                                Empleador empleador = ((BeanItem<Empleador>) fieldGroup.getItemDataSource()).getBean();
                                hbn.saveEntity(empleador);
                                // Para cada empleador debe haber una (y solo una) búsqueda ficticia
                                crearBusquedaFicticia(empleador);
                            }
                            if (window != null)
                                window.close();
                            else
                                UI.getCurrent().getNavigator().navigateTo(ContenedorPrincipalUI.VIEW_EMPLEADORES);
                        } catch (FieldGroup.CommitException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

        });
        fl.addComponent(button);
        return layout;
    }

    private void setElemento(Item elemento) {
        fieldGroup.setItemDataSource(elemento);
    }

    private void crearBusquedaFicticia(Empleador empleador) {
        Busqueda busquedaFicticia = new Busqueda();
        busquedaFicticia.setEmpleador(empleador);
        busquedaFicticia.setFicticia(true);
        busquedaFicticia.setActiva(true);
        HbnContainer<Busqueda> hbnBusqueda = new HbnContainer<Busqueda>(Busqueda.class, SpringHelper.getBean("sessionFactory", SessionFactory.class));
        hbnBusqueda.saveEntity(busquedaFicticia);
    }
}