package org.nahual;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;

import javax.servlet.annotation.WebServlet;

@Theme("mytheme")
@SuppressWarnings("serial")
public class SemilleroAppUI extends UI
{

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = SemilleroAppUI.class, widgetset = "org.nahual.AppWidgetSet")
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        setContent(layout);

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
                addWindow(ventana);
                VerticalLayout contenidoVentana = new VerticalLayout();
                contenidoVentana.addComponent(new Label("Gracias por clickear"));
                ventana.setContent(contenidoVentana);
            }
        });
        fl.addComponent(button);
    }

}
