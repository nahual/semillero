package org.nahual.semillero.views;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.hbnutil.HbnContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import org.nahual.semillero.components.ContenedorPrincipalUI;
import org.nahual.semillero.model.Empleador;
import org.nahual.utils.SpringHelper;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Created by fdviosteam on 06/03/14.
 */
public class BusquedaView extends VerticalLayout implements View {
    private HbnContainer<Empleador> hbn;
    private TextArea descripcionTA;
    private TextField tituloTF;
    private DateField fechaInicioDF;
    private DateField fechaFinDF;
    private CheckBox activaCB;
    private FieldGroup fieldGroup;

    public BusquedaView(Empleador empleador) {

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }

    private VerticalLayout createLayout() {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);

        Label tituloEmpleador = new Label("Nuevo Empleador");
        tituloEmpleador.setStyleName("titulo");

        layout.addComponent(tituloEmpleador);

        FormLayout fl = new FormLayout();
        layout.addComponent(fl);

        fl.setSizeUndefined();

        tituloTF = new TextField("Título");
        fl.addComponent(tituloTF);
        tituloTF.setRequired(true);
        tituloTF.setRequiredError("Título no puede estar vacio");

        fechaInicioDF = new DateField("Fecha Inicio");
        fl.addComponent(fechaInicioDF);
        fechaInicioDF.setRequired(true);
        fechaInicioDF.setRequiredError("Fecha Inicio no puede estar vacio");

        fechaFinDF = new DateField("Fecha Inicio");
        fl.addComponent(fechaFinDF);
        fechaFinDF.setRequired(true);
        fechaFinDF.setRequiredError("Fecha Fin no puede estar vacio");

        activaCB = new CheckBox("Activa");
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
                            hbn.saveEntity(((BeanItem<Empleador>) fieldGroup.getItemDataSource()).getBean());
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
}
