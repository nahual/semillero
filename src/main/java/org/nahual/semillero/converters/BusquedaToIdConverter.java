package org.nahual.semillero.converters;


import com.vaadin.data.hbnutil.ContainerFilter;
import com.vaadin.data.hbnutil.HbnContainer;
import com.vaadin.data.util.converter.Converter;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.nahual.semillero.model.Busqueda;
import org.nahual.semillero.model.Empleador;
import org.nahual.utils.SpringHelper;

import java.util.ArrayList;
import java.util.Locale;

public class BusquedaToIdConverter implements Converter<Busqueda, Long> {

    public Busqueda convertToPresentation(final Long idBusqueda, Class<? extends Busqueda> targetType, Locale locale) throws ConversionException {
        HbnContainer hbnBusqueda = new HbnContainer<Busqueda>(Busqueda.class, SpringHelper.getBean("sessionFactory", SessionFactory.class));
        Busqueda busquedaTmp = null;

        hbnBusqueda.addContainerFilter(new ContainerFilter("id") {
            @Override
            public Criterion getFieldCriterion(String fullPropertyName) {
                return Restrictions.eq(fullPropertyName, idBusqueda);
            }
        });

        ArrayList ids = (ArrayList) hbnBusqueda.getItemIds();

        // Solo debe haber uno ya que se filtró por id de empleador
        for (Object id : ids) {
            busquedaTmp = (Busqueda) hbnBusqueda.getItem(id).getPojo();
        }

        return busquedaTmp;
    }

    public Long convertToModel(Busqueda busqueda, Class<? extends Long> targetType, Locale locale) throws ConversionException {
        if (busqueda != null)
            return busqueda.getId();

        return null;
    }

    public Class<Long> getModelType() {
        return Long.class;
    }

    public Class<Busqueda> getPresentationType() {
        return Busqueda.class;
    }
}
