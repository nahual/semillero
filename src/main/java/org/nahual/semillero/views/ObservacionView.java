package org.nahual.semillero.views;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.hbnutil.HbnContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import org.hibernate.SessionFactory;
import org.nahual.semillero.model.Empleador;
import org.nahual.semillero.model.Observacion;
import org.nahual.utils.SpringHelper;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Date;

/**
 * Created by fdviosteam on 07/03/14.
 */
public class ObservacionView extends VerticalLayout implements View {
    private Observacion observacion;
    private HbnContainer<Empleador> hbn;
    private FieldGroup fieldGroup;
    private TextArea observacionTA;
    private Window window;

    public Window getWindow() {
        return window;
    }

    public void setWindow(Window window) {
        this.window = window;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }

    private void init(Empleador unEmpleador) {
        this.setSizeFull();
        this.setMargin(true);
        this.addComponent(createLayout(unEmpleador));
        fieldGroup = new FieldGroup();
        fieldGroup.bind(this.observacionTA, "texto");

        // Se aplica un estilo particular a los captions de los fields
        for (Object field : fieldGroup.getFields()) {
            ((AbstractComponent) field).setStyleName("textField");
            if (field instanceof AbstractTextField)
                ((AbstractTextField) field).setNullRepresentation("");
        }
    }

    public ObservacionView(Empleador unEmpleador) {
        init(unEmpleador);

        this.hbn = new HbnContainer<Empleador>(Empleador.class, SpringHelper.getBean("sessionFactory", SessionFactory.class));
        observacion = new Observacion();
        observacion.setFecha(new Date());
        unEmpleador.getObservaciones().add(observacion);

        Item newItem = new BeanItem<Observacion>(observacion);
        setElemento(newItem);
    }

    private VerticalLayout createLayout(final Empleador unEmpleador) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);

        Label tituloEmpleador = new Label("Nueva Observación");
        tituloEmpleador.setStyleName("titulo");

        layout.addComponent(tituloEmpleador);

        FormLayout fl = new FormLayout();
        layout.addComponent(fl);

        fl.setSizeUndefined();

        TextField empleador = new TextField("Empresa");
        empleador.setValue(unEmpleador.getEmpresa());
        empleador.setReadOnly(true);
        fl.addComponent(empleador);

        observacionTA = new TextArea("Descripción");
        fl.addComponent(observacionTA);

        Button button = new Button("Aceptar");
        button.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                TransactionTemplate transactionTemplate = SpringHelper.getBean("transactionTemplate", TransactionTemplate.class);
                transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                    @Override
                    protected void doInTransactionWithoutResult(TransactionStatus status) {
                        try {
                            fieldGroup.commit();
                            hbn.updateEntity( unEmpleador);
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
}