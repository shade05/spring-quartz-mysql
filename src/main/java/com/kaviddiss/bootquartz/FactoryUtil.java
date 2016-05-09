package com.kaviddiss.bootquartz;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class FactoryUtil {

    @Autowired
    private ApplicationContext ctx;

    public <T> T getBeanByType(final Class<T> claz) throws UnsupportedOperationException, BeansException {
        @SuppressWarnings("rawtypes")
        Map beansOfType = ctx.getBeansOfType(claz);
        final int size = beansOfType.size();
        switch (size) {
        case 0:
            throw new UnsupportedOperationException("No bean found of type" + claz);
        case 1:
            String name = (String) beansOfType.keySet().iterator().next();
            return claz.cast(ctx.getBean(name, claz));
        default:
            throw new UnsupportedOperationException("Unknown bean found of type" + claz);
        }
    }

    public <T> T getBeanByType(final Class<T> claz, String qualifier) throws UnsupportedOperationException, BeansException {
        T bean = ctx.getBean(qualifier, claz);
        return bean;
    }

    public <T> T getBeanByType(final Class<T> claz, Object... objects) throws UnsupportedOperationException, BeansException {
        T bean = ctx.getBean(claz, objects);
        return bean;
    }

    public Object getBeanByType(String qualifier, Object... objects) throws UnsupportedOperationException, BeansException {
        Object bean = ctx.getBean(qualifier, objects);
        return bean;
    }

    public <T> T getBean(final Class<T> claz, Object... objects) throws UnsupportedOperationException, BeansException {
        T bean = ctx.getBean(claz, objects);
        return bean;
    }

}