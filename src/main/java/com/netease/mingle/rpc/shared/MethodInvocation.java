package com.netease.mingle.rpc.shared;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Method Invocation Descriptor
 * Created by Michael Jiang on 16-12-2.
 */
public class MethodInvocation implements Serializable {
    private final static String version = "1.0";
    private static final long serialVersionUID = -8292303732547500854L;

    private String className;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameters;

    public MethodInvocation(String className, String methodName, Class<?>[] parameterTypes) {
        this(className, methodName, parameterTypes, null);
    }

    public MethodInvocation(String className, String methodName, Class<?>[] parameterTypes, Object[] parameters) {
        this.className = className;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.parameters = parameters;
    }

    public static MethodInvocation from(Method method) {
        if (method == null) {
            throw new NullPointerException("method is null");
        }
        String className = method.getDeclaringClass().getName();
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        return new MethodInvocation(className, methodName, parameterTypes);
    }

    public static String getVersion() {
        return version;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public boolean isSameMethod(MethodInvocation other) {
        return other != null
                && this.className.equals(other.className)
                && this.methodName.equals(other.methodName)
                && Arrays.equals(this.parameterTypes, other.parameterTypes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodInvocation that = (MethodInvocation) o;

        if (className != null ? !className.equals(that.className) : that.className != null) return false;
        if (methodName != null ? !methodName.equals(that.methodName) : that.methodName != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(parameterTypes, that.parameterTypes)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        int result = className != null ? className.hashCode() : 0;
        result = 31 * result + (methodName != null ? methodName.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(parameterTypes);
        result = 31 * result + Arrays.hashCode(parameters);
        return result;
    }
}
