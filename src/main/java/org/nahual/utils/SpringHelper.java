package org.nahual.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author Alejandro Gomez (alejandro.gomez@fdvsolutions.com)
 *         Date: 1/24/14
 *         Time: 9:10 AM
 */
public class SpringHelper implements ApplicationContextAware {

    private static final SpringHelper INSTANCE = new SpringHelper();

    private ApplicationContext applicationContext;

    private SpringHelper() {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static SpringHelper getInstance() {
        return INSTANCE;
    }

    public static <T> T getBean(final String beanRef, Class<T> expectedType) {
        if (INSTANCE.applicationContext == null) {
            throw new RuntimeException("SpringHelper no fue correctamente inicializado");
        }
        return INSTANCE.applicationContext.getBean(beanRef, expectedType);
    }
}
