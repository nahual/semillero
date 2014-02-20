package org.nahual.semillero.views;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.hbnutil.HbnContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import org.hibernate.SessionFactory;
import org.nahual.semillero.components.ContenedorPrincipalUI;
import org.nahual.semillero.model.Egresado;
import org.nahual.utils.SpringHelper;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;


public class NuevoEgresadoView extends VerticalLayout implements View {

    private TextField nombreTF;
    private TextField telefonoFijoTF;
    private TextField telefonoMovilTF;
    private TextField correoElectronico;
    private ComboBox nodo;
    private ComboBox cuatrimestre;
    private TextArea observacionesTF;

    private FieldGroup fieldGroup;

    private ArrayList<String> cuatrimestresValidos = new ArrayList<String>();

    public NuevoEgresadoView() {
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

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        this.nombreTF.setValue("");
        this.telefonoFijoTF.setValue("");
        this.telefonoMovilTF.setValue("");
        this.correoElectronico.setValue("");
        this.nodo.setValue("");
        this.cuatrimestre.setValue("");
        this.observacionesTF.setValue("");
        HbnContainer hbn = new HbnContainer<Egresado>(Egresado.class, SpringHelper.getBean("sessionFactory", SessionFactory.class));
        setElemento(hbn.getItem(hbn.saveEntity(new Egresado())));
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
        nombreTF.setRequiredError("Nombre no puede estar vacio");

        telefonoFijoTF = new TextField("Telefono fijo");
        fl.addComponent(telefonoFijoTF);

        telefonoMovilTF = new TextField("Telefono movil");
        fl.addComponent(telefonoMovilTF);

        correoElectronico = new TextField("Correo electrónico");
        fl.addComponent(correoElectronico);

        cuatrimestre = new ComboBox("Cuatrimestre");
        /*cuatrimestre.setRequired(true);
        cuatrimestre.setRequiredError("Cuatrimestre no puede estar vacio");*/
        fl.addComponent(cuatrimestre);

        nodo = new ComboBox("Nodo");
        /*nodo.setRequired(true);
        nodo.setRequiredError("Nodo no puede estar vacio");*/
        fl.addComponent(nodo);

        observacionesTF = new TextArea("Observaciones");
        fl.addComponent(observacionesTF);


        Button button = new Button("Aceptar");
        button.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                TransactionTemplate transactionTemplate = SpringHelper.getBean("transactionTemplate", TransactionTemplate.class);
                transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                    @Override
                    protected void doInTransactionWithoutResult(TransactionStatus status) {
                        try {
                            fieldGroup.commit();
                        } catch (FieldGroup.CommitException e) {
                            e.printStackTrace();
                        }
                        UI.getCurrent().getNavigator().navigateTo(ContenedorPrincipalUI.VIEW_EGRESADOS);
                    }
                });
            }

        });

        fl.addComponent(button);
        return layout;
    }

    public void setElemento(Item elemento) {
        fieldGroup.setItemDataSource(elemento);
    }

}
