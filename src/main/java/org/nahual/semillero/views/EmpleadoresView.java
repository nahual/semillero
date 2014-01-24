package org.nahual.semillero.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;


public class EmpleadoresView extends VerticalLayout implements View {

    public EmpleadoresView() {
        this.setSizeFull();
        this.setMargin(true);
/*        Label label = new Label("Home");
        this.addComponent(label);*/

        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);

        Label tituloEmpleador = new Label("Nuevo Empleador");
        tituloEmpleador.setStyleName("titulo");

        layout.addComponent(tituloEmpleador);

        // A FormLayout used outside the context of a Form
        FormLayout fl = new FormLayout();
        layout.addComponent(fl);

// Make the FormLayout shrink to its contents
        fl.setSizeUndefined();

        TextField empresaTF = new TextField("Empresa");
        fl.addComponent(empresaTF);


        empresaTF.setRequired(true);
        empresaTF.setRequiredError("Empresa no puede estar vacio");

        TextField contactoTF = new TextField("Contacto");
        fl.addComponent(contactoTF);

        contactoTF.setRequired(true);
        contactoTF.setRequiredError("Contacto no puede estar vacio");

        TextArea observacionesTF = new TextArea("Observaciones");
        fl.addComponent(observacionesTF);


        Button button = new Button("Aceptar");
        button.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                Window ventana = new Window("Gracias");
                UI.getCurrent().addWindow(ventana);
                VerticalLayout contenidoVentana = new VerticalLayout();
                contenidoVentana.addComponent(new Label("Gracias por clickear"));
                ventana.setContent(contenidoVentana);
            }
        });
        fl.addComponent(button);

        this.addComponent(layout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }
}
