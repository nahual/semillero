package org.nahual.semillero.views;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.hbnutil.HbnContainer;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import org.nahual.semillero.model.Empleador;

/**
 * Created by fdviosteam on 06/03/14.
 */
public class BusquedaView {
    private HbnContainer<Empleador> hbn;
    private TextArea descripcionTA;
    private TextField tituloTF;
    private DateField fechaInicioDF;
    private DateField fechaFinDF;
    private CheckBox activaCB;
    private FieldGroup fieldGroup;
}
