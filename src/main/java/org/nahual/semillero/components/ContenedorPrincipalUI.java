package org.nahual.semillero.components;

import com.vaadin.navigator.Navigator;
import com.vaadin.ui.*;
import org.nahual.semillero.views.ErrorView;
import org.nahual.semillero.views.BusquedasView;
import org.nahual.semillero.views.EgresadosView;
import org.nahual.semillero.views.EmpleadoresView;


public class ContenedorPrincipalUI extends CustomComponent {
    public static final String VIEW_EMPLEADORES = "empleadores";
    private static final String VIEW_EGRESADOS = "egresados";
    private static final String VIEW_BUSQUEDAS = "busquedas";

    private MenuBar barraDeMenu = new MenuBar();
    private VerticalLayout mainLayout;
    private Navigator navigator;
    private ComponentContainer body;

    public ContenedorPrincipalUI() {
        buildMainLayout();
        setCompositionRoot(mainLayout);
    }


    private void buildMainLayout() {
        mainLayout = new VerticalLayout();
        body = new VerticalLayout();
        body.setSizeFull();
        body.setHeight("100%");
        mainLayout.addComponent(barraDeMenu);
        mainLayout.addComponent(body);
        mainLayout.setComponentAlignment(body, Alignment.TOP_LEFT);
        mainLayout.setExpandRatio(body, 1f);
        mainLayout.setComponentAlignment(barraDeMenu, Alignment.MIDDLE_CENTER);

    }

    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
    }

    public void setupMenu() {
        EgresadosView egresadosView = new EgresadosView();
        EmpleadoresView empleadoresView = new EmpleadoresView();
        BusquedasView busquedasView = new BusquedasView();

        ErrorView errorView = new ErrorView();
        navigator.addView(VIEW_EMPLEADORES, empleadoresView);
        navigator.addView(VIEW_EGRESADOS, egresadosView);
        navigator.addView(VIEW_BUSQUEDAS, busquedasView);
        navigator.setErrorView(errorView);

        barraDeMenu.addItem("Empleadores", null, new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem menuItem) {
                navigator.navigateTo(VIEW_EMPLEADORES);
            }
        });
        barraDeMenu.addItem("Egresados", null, new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem menuItem) {
                navigator.navigateTo(VIEW_EGRESADOS);
            }
        });
        barraDeMenu.addItem("Busquedas", null, new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem menuItem) {
                navigator.navigateTo(VIEW_BUSQUEDAS);
            }
        });
    }

    public ComponentContainer getBody() {
        return body;
    }
}
