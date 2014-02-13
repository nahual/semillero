package org.nahual.semillero.views;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.hbnutil.HbnContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import org.hibernate.SessionFactory;
import org.nahual.semillero.components.ContenedorPrincipalUI;
import org.nahual.semillero.model.Empleador;
import org.nahual.utils.SpringHelper;


public class NuevoEmpleadorView extends VerticalLayout implements View {

    private TextArea observacionesTF;
    private TextField empresaTF;
    private TextField contactoTF;
    private Item elemento;
    private FieldGroup fieldGroup;
    private HbnContainer<Empleador> container;

    public NuevoEmpleadorView() {
        this.setSizeFull();
        this.setMargin(true);
        this.addComponent(createLayout());
        fieldGroup = new FieldGroup();
        fieldGroup.bind(this.empresaTF, "empresa");
        fieldGroup.bind(this.observacionesTF, "observaciones");
        fieldGroup.bind(this.contactoTF, "contacto");
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        this.empresaTF.setValue("");
        this.contactoTF.setValue("");
        this.observacionesTF.setValue("");
    }

    private VerticalLayout createLayout() {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);

        Label tituloEmpleador = new Label("Nuevo Empleador");
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

        observacionesTF = new TextArea("Observaciones");
        fl.addComponent(observacionesTF);


        Button button = new Button("Aceptar");
        button.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                HbnContainer hbn = new HbnContainer<Empleador>(Empleador.class, SpringHelper.getBean("sessionFactory", SessionFactory.class));
                if (fieldGroup.getItemDataSource().getItemProperty("Id").getValue() != null) {
                    try {
                        fieldGroup.commit();
                        hbn.saveEntity((Empleador) ((HbnContainer.EntityItem) fieldGroup.getItemDataSource()).getPojo());
                    } catch (FieldGroup.CommitException e) {
                        e.printStackTrace();
                    }
                } else {
                    Empleador empleador = new Empleador();
                    empleador.setEmpresa(empresaTF.getValue());
                    empleador.setContacto(contactoTF.getValue());
                    empleador.setObservaciones(observacionesTF.getValue());
                    hbn.saveEntity(empleador);
                    UI.getCurrent().getNavigator().navigateTo(ContenedorPrincipalUI.VIEW_EMPLEADORES);
                }
            }

        });
        fl.addComponent(button);
        return layout;
    }

    public void setElemento(Item elemento) {
        this.elemento = elemento;
        fieldGroup.setItemDataSource(this.elemento);
    }

    public void setContainer(HbnContainer<Empleador> container) {
        this.container = container;
    }
}
