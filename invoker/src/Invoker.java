import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: blackhawk
 * Date: 03.05.13
 * Time: 22:11
 */
public class Invoker {

    public static void main(String... args) {
        if (args.length < 2) {
            System.out.println("Usage: Invoker class-name method-name [args...]");
            return;
        }

        final String className = args[0];
        final String methodName = args[1];

        final Class<?> clazz = loadClass(className);
        if (clazz == null) {
            return;
        }

        if (clazz.isAnnotation()) {
            System.out.println(className + " is annotation");
            return;
        } else if (clazz.isInterface()) {
            System.out.println(className + " is interface");
            return;
        } else if (Modifier.isAbstract(clazz.getModifiers())) {
            System.out.println(className + " is abstract");
            return;
        }

        System.out.println("Loaded: " + clazz);

        final Object instance = makeInstance(clazz);
        if (instance == null) {
            return;
        }
        System.out.println("Instance created: " + instance);

        final int argCount = args.length - 2;
        final boolean[] anythingFound = {false};

        final Set<List<Class<?>>> invoked = new HashSet<>();

        for (Class<?> cls = clazz; cls != null; cls = cls.getSuperclass()) {
            final boolean usePrivateMethods = cls == clazz;
            Arrays.stream(cls.getDeclaredMethods())
                    .filter(m -> m.getName().equals(methodName)
                                    && (usePrivateMethods || !Modifier.isPrivate(m.getModifiers())))
                    .forEach(m -> {
                        Class<?>[] parameterTypes = m.getParameterTypes();
                        if (parameterTypes.length > argCount
                                || (!m.isVarArgs() && parameterTypes.length < argCount)) {
                            return;
                        }

                        if (invoked.contains(Arrays.asList(parameterTypes))) {
                            return;
                        }

                        invoked.add(Arrays.asList(parameterTypes));

                        int varargsStart = parameterTypes.length;
                        for (int i = 0; i < parameterTypes.length; i++) {
                            if (i == parameterTypes.length - 1
                                    && parameterTypes[i].isAssignableFrom(String[].class)
                                    && m.isVarArgs()) {
                                varargsStart = i;
                            } else if (!parameterTypes[i].isAssignableFrom(String.class)) {
                                return;
                            }
                        }
                        anythingFound[0] = true;

                        System.out.println(m.toString());
                        if (varargsStart == parameterTypes.length) {
                            final String[] nonVararg = new String[varargsStart];
                            System.arraycopy(args, 2, nonVararg, 0, varargsStart);
                            invokeMethod(m, instance, nonVararg);
                        } else if (varargsStart == 0) {
                            final String[] vararg = new String[argCount];
                            System.arraycopy(args, 2, vararg, 0, argCount);
                            invokeMethod(m, instance, new Object[]{vararg});
                        } else {
                            Object[] methodArgs = new Object[varargsStart + 1];
                            System.arraycopy(args, 2, methodArgs, 0, varargsStart);
                            String[] vararg = new String[argCount - varargsStart];
                            methodArgs[varargsStart] = vararg;
                            System.arraycopy(args, 2 + varargsStart, vararg, 0, argCount - varargsStart);
                            invokeMethod(m, instance, methodArgs);
                        }
                    });
        }
        if (!anythingFound[0]) {
            System.out.println("No methods matching " + methodName + "(" + paramsString(argCount) + ") found");
        }
    }

    private static Class<?> loadClass(String name) {
        URL currentDir;

        try {
            currentDir = new URL("file://.");
        } catch (MalformedURLException e) {
            System.out.println("Error in code: should never reach here");
            return null;
        }

        ClassLoader loader = new URLClassLoader(new URL[]{currentDir});
        try {
            return loader.loadClass(name);
        } catch (ClassNotFoundException e) {
            System.out.println("Class " + name + " not found");
            return null;
        }
    }

    private static Object makeInstance(Class<?> clazz) {
        try {
            final Constructor<?> defaultConstructor = clazz.getDeclaredConstructor();
            return defaultConstructor.newInstance();
        } catch (NoSuchMethodException e) {
            System.out.println(clazz.getName() + " has no default constructor");
            return null;
        } catch (InvocationTargetException e) {
            System.out.println("Constructor threw an exception: " + e.getCause());
            return null;
        } catch (InstantiationException e) {
            System.out.println("Should never reach here. Exception: " + e);
            return null;
        } catch (IllegalAccessException e) {
            System.out.println(clazz.getName() + " has private default constructor");
            return null;
        }
    }

    private static void invokeMethod(Method method, Object that, Object[] args) {
        try {
            method.setAccessible(true);
            method.invoke(that, args);
            System.out.println("Object after invocation: " + that);
        } catch (InvocationTargetException e) {
            System.out.println("Method threw an exception: " + e.getCause());
        } catch (IllegalAccessException e) {
            System.out.println("Method " + method.getName() + " is private");
        }
    }

    private static String paramsString(int count) {
        String s = "String";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(i == 0 ? s : ", " + s);
        }
        return sb.toString();
    }
}
