package org.squashleague.web.tasks;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Future;

/**
 * @author jamesdbloom
 */
public class CommandHolder<T> implements MethodInterceptor {

    private Future<?> delegate;

    public CommandHolder(Future<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        return proxy.invoke(delegate.get(), args);
    }

    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Future<T> delegate, Class<? extends T> clazz) {

        CommandHolder<T> interceptor = new CommandHolder<>(delegate);
        Enhancer enhancer = new Enhancer();
        enhancer.setCallback(interceptor);
        enhancer.setSuperclass(clazz);
        return (T) enhancer.create();
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> newListInstance(Future<List<T>> delegate, Class<T> clazz) {

        CommandHolder<List<T>> interceptor = new CommandHolder<>(delegate);
        Enhancer enhancer = new Enhancer();
        enhancer.setCallback(interceptor);
        enhancer.setSuperclass(List.class);
        return (List<T>) enhancer.create();
    }
}
