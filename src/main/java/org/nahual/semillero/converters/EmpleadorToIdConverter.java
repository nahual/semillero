package org.nahual.semillero.converters;


import com.vaadin.data.hbnutil.ContainerFilter;
import com.vaadin.data.hbnutil.HbnContainer;
import com.vaadin.data.util.converter.Converter;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.nahual.semillero.model.Empleador;
import org.nahual.utils.SpringHelper;

import java.util.ArrayList;
import java.util.Locale;

public class EmpleadorToIdConverter implements Converter<Empleador, Long> {

    public Empleador convertToPresentation(final Long idEmpleado, Class<? extends Empleador> targetType, Locale locale) throws ConversionException {
        HbnContainer hbnEmpleador = new HbnContainer<Empleador>(Empleador.class, SpringHelper.getBean("sessionFactory", SessionFactory.class));
        Empleador empleadorTmp = null;

        hbnEmpleador.addContainerFilter(new ContainerFilter("id") {
            @Override
            public Criterion getFieldCriterion(String fullPropertyName) {
                return Restrictions.eq(fullPropertyName, idEmpleado);
            }
        });

        ArrayList ids = (ArrayList) hbnEmpleador.getItemIds();

        // Solo debe haber uno ya que se filtró por id de empleador
        for (Object id : ids) {
            empleadorTmp = (Empleador) hbnEmpleador.getItem(id).getPojo();
        }

        return empleadorTmp;
    }

    public Long convertToModel(Empleador empleador, Class<? extends Long> targetType, Locale locale) throws ConversionException {
        return empleador.getId();
    }

    public Class<Long> getModelType() {
        return Long.class;
    }

    public Class<Empleador> getPresentationType() {
        return Empleador.class;
    }
}
