package org.nahual.semillero.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import org.nahual.semillero.components.ContenedorPrincipalUI;

import javax.servlet.annotation.WebServlet;

@Theme("mytheme")
@SuppressWarnings("serial")
public class SemilleroAppUI extends UI {

    public static final String LOGGED_IN_SESSION_ID = "logged_in";

    private Navigator navigator;
    private ContenedorPrincipalUI marco;

    private VerticalLayout loginLayout;
    private TextField usuarioTF;
    private PasswordField password;
    private FormLayout fl;

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = SemilleroAppUI.class, widgetset = "org.nahual.AppWidgetSet")
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
        checkLogin(request);
    }

    private void showSemillero() {
        getPage().setTitle("Semillero");
        marco = new ContenedorPrincipalUI();
        setContent(marco);
        navigator = new Navigator(this, marco.getBody());
        marco.setNavigator(navigator);
        marco.setupMenu();
        navigator.navigateTo(ContenedorPrincipalUI.VIEW_EMPLEADORES);
    }

    private void buildLoginView() {
        loginLayout = new VerticalLayout();
        loginLayout.setMargin(true);

        fl = new FormLayout();
        loginLayout.addComponent(fl);

        usuarioTF = new TextField("Usuario");
        usuarioTF.setRequired(true);
        usuarioTF.addShortcutListener(new AbstractField.FocusShortcut(password, ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                checkPass();
            }
        });
        fl.addComponent(usuarioTF);

        password = new PasswordField("Contraseña");
        password.setRequired(true);
        password.addShortcutListener(new AbstractField.FocusShortcut(password, ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                 checkPass();
            }
        });
        fl.addComponent(password);

        Button button = new Button("Ingresar");
        fl.addComponent(button);
        button.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                checkPass();
            }
        });
        setContent(loginLayout);

    }

    protected void checkPass(){
        if (usuarioTF.getValue().equals("semillero") && password.getValue().equals("nahual")) {
            getSession().setAttribute(LOGGED_IN_SESSION_ID,true);
            showSemillero();
        } else {
            new Notification("El usuario y contraseña no son válidos", Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());
        }
    }

    private void checkLogin(VaadinRequest request) {
        if (getSession().getAttribute(LOGGED_IN_SESSION_ID) == null) {
            buildLoginView();
        } else {
            showSemillero();
        }
        setSizeFull();
    }

    public Navigator getNavigator() {
        return navigator;
    }

    public ContenedorPrincipalUI getMarco() {
        return marco;
    }
}
