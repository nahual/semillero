package org.nahual.semillero.components;

import com.vaadin.navigator.Navigator;
import com.vaadin.server.ClassResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import org.nahual.semillero.model.Postulacion;
import org.nahual.semillero.views.*;


public class ContenedorPrincipalUI extends CustomComponent {
    public static final String VIEW_EMPLEADORES = "empleadores";
    public static final String VIEW_NUEVO_EMPLEADOR = "nuevoEmpleador";
    public static final String VIEW_EGRESADOS = "egresados";
    public static final String VIEW_NUEVO_EGRESADO = "nuevoEgresado";
    public static final String VIEW_BUSQUEDAS = "busquedas";
    public static final String VIEW_POSTULACIONES = "postulaciones";
    public static final String VIEW_BUSQUEDAS_EMPLEADOR = "busquedas_empleador";

    private MenuBar barraDeMenu = new MenuBar();
    private VerticalLayout mainLayout;
    private Navigator navigator;
    private ComponentContainer body;
    private BusquedasView busquedasEmpleadorView;
    private PostulacionesView postulacionesView;
    private HorizontalLayout logo;

    public ContenedorPrincipalUI() {
        buildMainLayout();
        setCompositionRoot(mainLayout);
    }


    private void buildMainLayout() {
        mainLayout = new VerticalLayout();
        mainLayout.setMargin(true);
        logo = new HorizontalLayout();
        ThemeResource resource = new ThemeResource("img/logo.png");
        Image image = new Image("", resource);
        logo.addComponent(image);
        Label tituloEmpleadores = new Label("Semillero");
        tituloEmpleadores.setStyleName("tituloLogo");
        logo.addComponent(tituloEmpleadores);
        logo.setComponentAlignment(tituloEmpleadores, Alignment.BOTTOM_CENTER);
        body = new VerticalLayout();
        body.setSizeFull();
        body.setHeight("100%");
        mainLayout.addComponent(logo);
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
        busquedasEmpleadorView = new BusquedasView();
        postulacionesView = new PostulacionesView();
        EmpleadoresView empleadoresView = new EmpleadoresView();
        EgresadosView egresadosView = new EgresadosView();

        ErrorView errorView = new ErrorView();

        navigator.addView(VIEW_EMPLEADORES, empleadoresView);
        navigator.addView(VIEW_EGRESADOS, egresadosView);
        navigator.addView(VIEW_NUEVO_EGRESADO, EgresadoView.class);
        navigator.addView(VIEW_BUSQUEDAS, BusquedasView.class);
        navigator.addView(VIEW_NUEVO_EMPLEADOR, EmpleadorView.class);
        navigator.addView(VIEW_POSTULACIONES, PostulacionesView.class);

        navigator.addView(VIEW_BUSQUEDAS_EMPLEADOR, busquedasEmpleadorView);
        navigator.addView(VIEW_POSTULACIONES, postulacionesView);
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
        barraDeMenu.addItem("Postulaciones", null, new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem menuItem) {
                navigator.navigateTo(VIEW_POSTULACIONES);
            }
        });
        barraDeMenu.setStyleName("barraMenu");
    }

    public ComponentContainer getBody() {
        return body;
    }

    public BusquedasView getBusquedasEmpleadorView() {
        return busquedasEmpleadorView;
    }

    public PostulacionesView getPostulacionesView() {
        return postulacionesView;
    }
}
