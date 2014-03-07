package org.nahual.semillero.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import org.nahual.semillero.components.ContenedorPrincipalUI;

import javax.servlet.annotation.WebServlet;

@Theme("mytheme")
@SuppressWarnings("serial")
public class SemilleroAppUI extends UI {

    private Navigator navigator;
    private ContenedorPrincipalUI marco;

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = SemilleroAppUI.class, widgetset = "org.nahual.AppWidgetSet")
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
        getPage().setTitle("Semillero");

        marco = new ContenedorPrincipalUI();
        setContent(marco);
        setSizeFull();
        navigator = new Navigator(this, marco.getBody());
        marco.setNavigator(navigator);
        marco.setupMenu();
        navigator.navigateTo(ContenedorPrincipalUI.VIEW_EMPLEADORES);
    }

    public Navigator getNavigator() {
        return navigator;
    }

    public ContenedorPrincipalUI getMarco() {
        return marco;
    }
}
