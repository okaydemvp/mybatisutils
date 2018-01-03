package org.mybatis.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Objects;

public class SpringContext implements ApplicationContextAware {

    private ApplicationContext context;
    private volatile static SpringContext springContextUtil = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Objects.requireNonNull(applicationContext);
        SpringContext.getInstance().setContext(applicationContext);
    }

    public static SpringContext getInstance() {
        if (springContextUtil == null) {
            synchronized (SpringContext.class) {
                if (springContextUtil == null) {
                    springContextUtil = new SpringContext();
                }
            }
        }
        return springContextUtil;
    }

    private void setContext(final ApplicationContext context) {
        Objects.requireNonNull(context);
        this.context = context;
    }

    private ApplicationContext getContext() {
        return context;
    }

    public static ApplicationContext getCtx() {
        return SpringContext.getInstance().getContext();
    }

    public static <T> T getBean(Class<T> beanType) {
        Objects.requireNonNull(beanType);
        T t = getCtx().getBean(beanType);
        return t;
    }

    public static <T> T getBean(String beanId) {
        Objects.requireNonNull(beanId);
        T t = (T) getCtx().getBean(beanId);
        return t;
    }
}
