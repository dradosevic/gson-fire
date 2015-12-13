package io.gsonfire.util.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by julio on 12/12/15.
 */
public class MethodInvoker {

    private final Method method;
    private final List<Class> argsOrder;

    public MethodInvoker(Method method, Set<Class> supportedInjectionTypes) {
        this.method = method;
        this.argsOrder = new ArrayList<Class>(supportedInjectionTypes.size());

        for (Parameter parameter : this.method.getParameters()) {
            if (supportedInjectionTypes.contains(parameter.getType())) {
                argsOrder.add(parameter.getType());
            } else {
                throw new IllegalArgumentException("Cannot auto inject type: " + parameter.getType());
            }
        }
    }

    public void invoke(Object obj, ValueSupplier supplier) throws InvocationTargetException, IllegalAccessException {
        Object[] args = new Object[method.getParameters().length];
        for (int i = 0; i < args.length; i++) {
            args[i] = supplier.getValueForType(argsOrder.get(i));
        }
        this.method.invoke(obj, args);
    }

    public interface ValueSupplier {

        Object getValueForType(Class type);

    }

}
