package org.nahual.semillero.components;

import com.vaadin.navigator.Navigator;
import com.vaadin.ui.*;
import org.nahual.semillero.views.*;


public class ContenedorPrincipalUI extends CustomComponent {
    public static final String VIEW_EMPLEADORES = "empleadores";
    public static final String VIEW_NUEVO_EMPLEADOR = "nuevoEmpleador";
    public static final String VIEW_EGRESADOS = "egresados";
    public static final String VIEW_BUSQUEDAS = "busquedas";

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
        mainLayout.setMargin(true);
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
        NuevoEgresadoView egresadosView = new NuevoEgresadoView();
        EmpleadorView empleadorView = new EmpleadorView();
        BusquedasView busquedasView = new BusquedasView();
        EmpleadoresView empleadoresView = new EmpleadoresView();

        ErrorView errorView = new ErrorView();

        navigator.addView(VIEW_EMPLEADORES, empleadoresView);
        navigator.addView(VIEW_EGRESADOS, egresadosView);
        navigator.addView(VIEW_BUSQUEDAS, busquedasView);
        navigator.addView(VIEW_NUEVO_EMPLEADOR, EmpleadorView.class);
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
