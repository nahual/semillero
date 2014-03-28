package org.nahual.semillero.views;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.hbnutil.HbnContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import org.hibernate.SessionFactory;
import org.nahual.semillero.components.ContenedorPrincipalUI;
import org.nahual.semillero.converters.SemilleroConverterFactory;
import org.nahual.semillero.model.Busqueda;
import org.nahual.semillero.model.Egresado;
import org.nahual.semillero.model.Empleador;
import org.nahual.semillero.model.Postulacion;
import org.nahual.utils.SpringHelper;
import org.nahual.utils.StsHbnContainer;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Collection;

public class BusquedaView extends VerticalLayout implements View {
    private String title;
    private boolean nuevoItem;
    private StsHbnContainer<Busqueda> hbn;
    private TextArea descripcionTA;
    private TextField tituloTF;
    private DateField fechaInicioDF;
    private DateField fechaFinDF;

    private CheckBox activaCB;
    private FieldGroup fieldGroup;
    private Window window;

    public BusquedaView(Empleador unEmpleador) {
        this.title = "Nueva Busqueda";
        init(unEmpleador);

        this.hbn = new StsHbnContainer<Busqueda>(Busqueda.class, SpringHelper.getBean("sessionFactory", SessionFactory.class));
        this.nuevoItem = true;
        Busqueda busqueda = new Busqueda();
        busqueda.setEmpleador(unEmpleador);
        Item newItem = new BeanItem<Busqueda>(busqueda);
        setElemento(newItem);
    }

    public BusquedaView(Item item) {
        VaadinSession.getCurrent().setConverterFactory(new SemilleroConverterFactory());

        Empleador empleador = ((Busqueda) ((StsHbnContainer.EntityItem) (item)).getPojo()).getEmpleador();
        this.title = "Editar Busqueda";
        init(empleador);
        this.nuevoItem = false;
        setElemento(item);
    }

    private void init(Empleador unEmpleador) {
        this.setSizeFull();
        this.setMargin(true);
        this.addComponent(createLayout(unEmpleador));
        fieldGroup = new FieldGroup();
        fieldGroup.bind(this.tituloTF, "titulo");
        fieldGroup.bind(this.fechaInicioDF, "fechaInicio");
        fieldGroup.bind(this.fechaFinDF, "fechaFin");
        fieldGroup.bind(this.activaCB, "activa");
        fieldGroup.bind(this.descripcionTA, "descripcion");

        // Se aplica un estilo particular a los captions de los fields
        for (Object field : fieldGroup.getFields()) {
            ((AbstractComponent) field).setStyleName("textField");
            if (field instanceof AbstractTextField)
                ((AbstractTextField) field).setNullRepresentation("");
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }

    private VerticalLayout createLayout(Empleador unEmpleador) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);

        Label tituloEmpleador = new Label(this.title);
        tituloEmpleador.setStyleName("titulo");

        layout.addComponent(tituloEmpleador);

        FormLayout fl = new FormLayout();
        layout.addComponent(fl);

        fl.setSizeUndefined();

        TextField empleador = new TextField("Empresa");
        empleador.setValue(unEmpleador.getEmpresa());
        empleador.setReadOnly(true);
        empleador.addStyleName("readOnly");
        fl.addComponent(empleador);

        tituloTF = new TextField("Título");
        fl.addComponent(tituloTF);
        tituloTF.setRequired(true);
        tituloTF.setRequiredError("Título no puede estar vacio");

        fechaInicioDF = new DateField("Fecha Inicio");
        fl.addComponent(fechaInicioDF);
        fechaInicioDF.setRequired(true);
        fechaInicioDF.setRequiredError("Fecha Inicio no puede estar vacio");

        fechaFinDF = new DateField("Fecha Fin");
        fl.addComponent(fechaFinDF);
        fechaFinDF.setRequired(true);
        fechaFinDF.setRequiredError("Fecha Fin no puede estar vacio");

        activaCB = new CheckBox("Activa");
        activaCB.setValue(false);
        fl.addComponent(activaCB);

        descripcionTA = new TextArea("Descripción");
        fl.addComponent(descripcionTA);

        Button button = new Button("Aceptar");
        button.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                TransactionTemplate transactionTemplate = SpringHelper.getBean("transactionTemplate", TransactionTemplate.class);
                transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                    @Override
                    protected void doInTransactionWithoutResult(TransactionStatus status) {
                        try {
                            fieldGroup.commit();
                            if (nuevoItem)
                                hbn.saveEntity(((BeanItem<Busqueda>) fieldGroup.getItemDataSource()).getBean());
                            if (window != null)
                                window.close();
                        } catch (FieldGroup.CommitException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

        });
        fl.addComponent(button);
        return layout;
    }

    private void setElemento(Item elemento) {
        fieldGroup.setItemDataSource(elemento);
    }

    public void setWindow(Window window) {
        this.window = window;
    }
}
