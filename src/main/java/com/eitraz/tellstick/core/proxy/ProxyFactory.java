package com.eitraz.tellstick.core.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Proxy Factory
 * <p/>
 * Created by Petter Alstermark on 2014-11-06.
 */
public final class ProxyFactory {
    @SuppressWarnings("unchecked")
    public static <T> T newProxyInstance(Class<T> targetClass, final InvocationHandler invocationHandler) {
        MethodInterceptor interceptor = new MethodInterceptor() {
            @Override
            public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                return invocationHandler.invoke(object, method, args);
            }
        };

        return (T) Enhancer.create(targetClass, interceptor);
    }
}
