package org.nahual.semillero.views;


import com.vaadin.data.hbnutil.ContainerFilter;
import com.vaadin.data.hbnutil.HbnContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.nahual.semillero.model.Busqueda;
import org.nahual.semillero.model.Egresado;
import org.nahual.semillero.model.Empleador;
import org.nahual.semillero.model.Postulacion;
import org.nahual.utils.SpringHelper;

public class PostulacionesView extends VerticalLayout implements View {

    private HbnContainer<Postulacion> hbn;
    private Egresado egresado;

    public PostulacionesView() {
        this.setSizeFull();
        this.setMargin(true);
    }

    public PostulacionesView(Egresado egresado) {
        this.setSizeFull();
        this.setMargin(true);
        this.egresado = egresado;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        init();
    }

    private void init() {
        this.removeAllComponents();
        final VerticalLayout layout = new VerticalLayout();
        final HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth("50%");

        Label tituloPostulaciones = new Label("Postulaciones");
        tituloPostulaciones.setStyleName("titulo");
        tituloPostulaciones.setHeight("100%");
        layout.addComponent(tituloPostulaciones);
        layout.addComponent(topLayout);


        /* Tabla de postulaciones */
        final Table table = new Table();
        table.setWidth("50%");

        hbn = new HbnContainer<Postulacion>(Postulacion.class, SpringHelper.getSession());

        hbn.addContainerFilter(new ContainerFilter("activa") {
            @Override
            public Criterion getFieldCriterion(String fullPropertyName) {
                return Restrictions.eq(fullPropertyName, Boolean.TRUE);
            }
        });

        if (this.egresado != null)
            hbn.addContainerFilter(new ContainerFilter("egresado") {
                @Override
                public Criterion getFieldCriterion(String fullPropertyName) {
                    return Restrictions.eq(fullPropertyName, egresado);
                }
            });
        table.setContainerDataSource(hbn);
        table.setVisibleColumns(new Object[]{"egresado", "empleador", "busqueda", "descripcion"});

        layout.addComponent(table);

        layout.setMargin(true);
        topLayout.setMargin(true);

        this.addComponent(layout);
    }

    public void setEgresado(final Egresado egresado) {
        this.egresado = egresado;
    }

}