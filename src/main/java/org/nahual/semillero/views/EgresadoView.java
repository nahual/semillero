package org.nahual.semillero.views;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.hbnutil.HbnContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import org.hibernate.SessionFactory;
import org.nahual.semillero.components.ContenedorPrincipalUI;
import org.nahual.semillero.model.Cuatrimestre;
import org.nahual.semillero.model.Egresado;
import org.nahual.semillero.model.Nodo;
import org.nahual.utils.CvUploader;
import org.nahual.utils.SpringHelper;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;


public class EgresadoView extends VerticalLayout implements View {

    private HbnContainer<Egresado> hbn;
    private boolean nuevoItem;
    private TextField nombreTF;
    private TextField telefonoFijoTF;
    private TextField telefonoMovilTF;
    private TextField correoElectronico;
    private ComboBox nodo;
    private ComboBox cuatrimestre;
    private TextArea observacionesTF;

    private FieldGroup fieldGroup;
    private Window window;
    private HbnContainer<Egresado> container;

    public EgresadoView(HbnContainer<Egresado> hbn) {
        this.hbn = hbn;
        init();
        Item newItem = new BeanItem<Egresado>(new Egresado());
        this.nuevoItem = true;
        setElemento(newItem);
    }

    public EgresadoView() {
        this(new HbnContainer<Egresado>(Egresado.class, SpringHelper.getBean("sessionFactory", SessionFactory.class)));
    }

    public EgresadoView(Item item) {
        init();
        this.nuevoItem = false;
        setElemento(item);
    }

    private void init() {
        this.setSizeFull();
        this.setMargin(true);
        this.addComponent(createLayout());
        fieldGroup = new FieldGroup();
        fieldGroup.bind(this.nombreTF, "nombre");
        fieldGroup.bind(this.telefonoFijoTF, "telefonoFijo");
        fieldGroup.bind(this.telefonoMovilTF, "telefonoMovil");
        fieldGroup.bind(this.correoElectronico, "correoElectronico");
        fieldGroup.bind(this.nodo, "nodo");
        fieldGroup.bind(this.cuatrimestre, "cuatrimestre");
        fieldGroup.bind(this.observacionesTF, "observaciones");

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

    private VerticalLayout createLayout() {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);

        Label tituloEgresado = new Label("Nuevo Egresado");
        tituloEgresado.setStyleName("titulo");

        layout.addComponent(tituloEgresado);

        FormLayout fl = new FormLayout();
        layout.addComponent(fl);

        fl.setSizeUndefined();

        nombreTF = new TextField("Nombre");
        fl.addComponent(nombreTF);
        nombreTF.setRequired(true);
        nombreTF.setValidationVisible(true);
        nombreTF.setRequiredError("Nombre no puede estar vacio");

        telefonoFijoTF = new TextField("Telefono fijo");
        fl.addComponent(telefonoFijoTF);

        telefonoMovilTF = new TextField("Telefono movil");
        fl.addComponent(telefonoMovilTF);

        correoElectronico = new TextField("Correo electrónico");
        correoElectronico.addValidator(new EmailValidator("e-mail inválido"));
        correoElectronico.setValidationVisible(true);
        correoElectronico.setImmediate(true); // Esto es para que la validación se haga de inmediato
        fl.addComponent(correoElectronico);

        cuatrimestre = new ComboBox("Cuatrimestre");
        cuatrimestre.setRequired(true);
        cuatrimestre.setRequiredError("Cuatrimestre no puede estar vacio");
        this.cargarCuatrimestres();
        fl.addComponent(cuatrimestre);

        nodo = new ComboBox("Nodo");
        nodo.setRequired(true);
        nodo.setRequiredError("Nodo no puede estar vacio");
        this.cargarNodos();
        fl.addComponent(nodo);

        observacionesTF = new TextArea("Observaciones");
        fl.addComponent(observacionesTF);

        // Adjuntar CV
        final CvUploader uploader = new CvUploader();
        Upload uploadCV = new Upload("CV", uploader);
        uploadCV.setStyleName("textField");
        uploadCV.setButtonCaption("Adjuntar");
        uploadCV.addSucceededListener(uploader);
        fl.addComponent(uploadCV);

        Button button = new Button("Aceptar");
        button.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                TransactionTemplate transactionTemplate = SpringHelper.getBean("transactionTemplate", TransactionTemplate.class);
                transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                    @Override
                    protected void doInTransactionWithoutResult(TransactionStatus status) {
                        try {
                            fieldGroup.commit();
                            if (nuevoItem) {
                                hbn.saveEntity(((BeanItem<Egresado>) fieldGroup.getItemDataSource()).getBean());
                            }

                            // Actualizar destino de cv (si existe)
                            uploader.relocateFileFor((Egresado) ((BeanItem<Egresado>) fieldGroup.getItemDataSource()).getBean());

                            if (window != null)
                                window.close();
                            else
                                UI.getCurrent().getNavigator().navigateTo(ContenedorPrincipalUI.VIEW_EMPLEADORES);
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

    private void cargarNodos() {
        HbnContainer hbn = new HbnContainer<Nodo>(Nodo.class, SpringHelper.getBean("sessionFactory", SessionFactory.class));

        ArrayList ids = (ArrayList) hbn.getItemIds();
        for (Object id : ids) {
            nodo.addItem(((Nodo) (hbn.getItem(id).getPojo())).getNombre());
        }

    }

    /* TODO: Mejorar leyendo la fecha actual y una fecha inicial y calcular la cantidad de cuatrimestres
    que hubo en el intermedio para no leer valores fijos */
    private void cargarCuatrimestres() {
        HbnContainer hbn = new HbnContainer<Cuatrimestre>(Cuatrimestre.class, SpringHelper.getBean("sessionFactory", SessionFactory.class));

        ArrayList ids = (ArrayList) hbn.getItemIds();
        for (Object id : ids) {
            cuatrimestre.addItem(((Cuatrimestre) (hbn.getItem(id).getPojo())).getCuatrimestre());
        }
    }


    public void setElemento(Item elemento) {
        fieldGroup.setItemDataSource(elemento);
    }

    public Window getWindow() {
        return window;
    }

    public void setWindow(Window window) {
        this.window = window;
    }


    public void setContainer(HbnContainer<Egresado> container) {
        this.container = container;
    }
}
