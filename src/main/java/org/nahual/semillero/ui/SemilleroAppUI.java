package org.nahual.semillero.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import org.nahual.semillero.components.ContenedorPrincipalUI;

import javax.servlet.annotation.WebServlet;

@Theme("mytheme")
@SuppressWarnings("serial")
public class SemilleroAppUI extends UI {

    private Navigator navigator;
    private ContenedorPrincipalUI marco;

    private VerticalLayout loginLayout;
    private TextField usuarioTF;
    private PasswordField password;

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = SemilleroAppUI.class, widgetset = "org.nahual.AppWidgetSet")
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
        if (!isLoggedIn()) {
            buildLoginView();
            setContent(loginLayout);
        } else {
            getPage().setTitle("Semillero");
            marco = new ContenedorPrincipalUI();
            setContent(marco);
            setSizeFull();
            navigator = new Navigator(this, marco.getBody());
            marco.setNavigator(navigator);
            marco.setupMenu();
            navigator.navigateTo(ContenedorPrincipalUI.VIEW_EMPLEADORES);
        }
    }

    private Boolean isLoggedIn() {
        //TODO: make the login, check if the user exists, etc
        return true;
    }

    private void buildLoginView() {
        loginLayout = new VerticalLayout();
        loginLayout.setMargin(true);

        FormLayout fl = new FormLayout();
        loginLayout.addComponent(fl);

        usuarioTF = new TextField("Usuario");
        usuarioTF.setRequired(true);
        fl.addComponent(usuarioTF);

        password = new PasswordField("Contraseña");
        password.setRequired(true);
        fl.addComponent(password);

        Button button = new Button("Ingresar");
        fl.addComponent(button);
    }

    public Navigator getNavigator() {
        return navigator;
    }

    public ContenedorPrincipalUI getMarco() {
        return marco;
    }
}
