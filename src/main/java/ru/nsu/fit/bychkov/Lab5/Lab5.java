package ru.nsu.fit.bychkov.Lab5;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Lab5 {

    static class MyInvocationHandler implements InvocationHandler {
        Object obj;
        List<String> methodNames;

        public MyInvocationHandler(Object obj, List<String> methodNames) {
            this.obj = obj;
            this.methodNames = methodNames;
        }

        public Object invoke(Object o, Method method, Object[] args) throws Throwable {
            if (methodNames.contains(method.getName())) {
                System.out.println("\tINVOKE " + method.getName());
            }
            return method.invoke(obj, args);
        }
    }

    static <T> T setLogging(final T obj) {
        //todo get all interfaces: obj.getClass().getInterfaces()
        //todo Intercept with java.lang.reflect.Proxy
        //todo Log each interface call into System.out
        //todo OUTPUT EXAMPLE:
        //todo      CALLING:  java.lang.Runnable#run()
        //todo      RETURN VAL: null

        List<String> methodNames = Arrays.stream(obj.getClass().getInterfaces())
                .flatMap(
                        it -> Arrays.stream(it.getDeclaredMethods()).map(Method::getName)
                )
                .collect(Collectors.toList());

        return (T) Proxy.newProxyInstance(
                obj.getClass().getClassLoader(),
                obj.getClass().getInterfaces(),
                new MyInvocationHandler(obj, methodNames)
        );
    }

    public static void main(String[] args) {
        List<Object> list = new ArrayList();
        list = setLogging(list);

        list.add(new Object());
        list.add(new Object());
        list.equals(new Object());
    }
}