package org.nahual.semillero.views;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.hbnutil.HbnContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import org.hibernate.SessionFactory;
import org.nahual.semillero.model.Feedback;
import org.nahual.semillero.model.Postulacion;
import org.nahual.utils.SpringHelper;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Date;

/**
 * Created by fdviosteam on 07/03/14.
 */
public class FeedbackView extends VerticalLayout implements View {
    private Feedback feedback;
    private HbnContainer<Postulacion> hbn;
    private FieldGroup fieldGroup;
    private TextArea feedbackTA;
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

    private void init(Postulacion unaPostulacion) {
        this.setSizeFull();
        this.setMargin(true);
        this.addComponent(createLayout(unaPostulacion));
        fieldGroup = new FieldGroup();
        fieldGroup.bind(this.feedbackTA, "texto");

        // Se aplica un estilo particular a los captions de los fields
        for (Object field : fieldGroup.getFields()) {
            ((AbstractComponent) field).setStyleName("textField");
            if (field instanceof AbstractTextField)
                ((AbstractTextField) field).setNullRepresentation("");
        }
    }

    public FeedbackView(Postulacion unaPostulacion) {
        init( unaPostulacion);

        this.hbn = new HbnContainer<Postulacion>(Postulacion.class, SpringHelper.getBean("sessionFactory", SessionFactory.class));
        feedback = new Feedback();
        feedback.setFecha(new Date());
        unaPostulacion.getFeedbacks().add(feedback);

        Item newItem = new BeanItem<Feedback>(feedback);
        setElemento(newItem);
    }

    private VerticalLayout createLayout(final Postulacion unaPostulacion) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);

        Label tituloEmpleador = new Label("Nuevo Feedback");
        tituloEmpleador.setStyleName("titulo");

        layout.addComponent(tituloEmpleador);

        FormLayout fl = new FormLayout();
        layout.addComponent(fl);

        fl.setSizeUndefined();



        TextField postulacion = new TextField("Postulación");
        postulacion.setValue(unaPostulacion.getDescripcion());
        postulacion.setReadOnly(true);
        fl.addComponent(postulacion);

        feedbackTA = new TextArea("Descripción");
        fl.addComponent(feedbackTA);

        Button button = new Button("Aceptar");
        button.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                TransactionTemplate transactionTemplate = SpringHelper.getBean("transactionTemplate", TransactionTemplate.class);
                transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                    @Override
                    protected void doInTransactionWithoutResult(TransactionStatus status) {
                        try {
                            fieldGroup.commit();
                            hbn.updateEntity( unaPostulacion);
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