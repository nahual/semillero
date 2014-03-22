package org.nahual.semillero.converters;


import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.DefaultConverterFactory;
import org.nahual.semillero.model.Busqueda;
import org.nahual.semillero.model.Empleador;

public class SemilleroConverterFactory extends DefaultConverterFactory {

    @Override
    public <PRESENTATION, MODEL> Converter<PRESENTATION, MODEL>
    createConverter(Class<PRESENTATION> presentationType,
                    Class<MODEL> modelType) {

        // Handle one particular type conversion
        if ((Empleador.class == presentationType) &&
                (Long.class == modelType)) {
            return (Converter<PRESENTATION, MODEL>)
                    new EmpleadorToIdConverter();
        }

        // Handle one particular type conversion
        if ((Busqueda.class == presentationType) &&
                (Long.class == modelType)) {
            return (Converter<PRESENTATION, MODEL>)
                    new BusquedaToIdConverter();
        }

        // Default to the supertype
        return super.createConverter(presentationType,
                modelType);

    }

}
