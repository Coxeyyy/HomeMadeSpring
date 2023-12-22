package com.coxey.app.framework;

import com.coxey.app.annotations.Autowired;
import com.coxey.app.annotations.Component;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class Context {
    private final Map<String, Class<?>> loadedClasses;

    private Context(Map<String, Class<?>> loadedClasses) {
        this.loadedClasses = loadedClasses;
    }

    public static Context load(String packageName) {
        Reflections reflections = new Reflections(packageName, new SubTypesScanner(false));
        Map<String, Class<?>> clazzes = reflections.getSubTypesOf(Object.class)
                .stream()
                .filter(clazz -> clazz.isAnnotationPresent(Component.class)).
                collect(Collectors.toMap(clazz -> clazz.getAnnotation(Component.class).value(), clazz -> clazz));

        return new Context(clazzes);
    }

    public Map<String, Class<?>> getLoadedClasses() {
        return loadedClasses;
    }

    public Object get(String className) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (!loadedClasses.containsKey(className)) {
            throw new RuntimeException("Нет такого объекта");
        }

        Class<?> clazz = loadedClasses.get(className);
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        Optional<Constructor<?>> annotatedConstructor = Arrays.stream(constructors)
                .filter(con -> con.isAnnotationPresent(Autowired.class))
                .findFirst();
        if (annotatedConstructor.isPresent()) {
            return createObjectWithAutowiredConstructor(annotatedConstructor);
        } else {
            return createObjectWithDefaultConstructor(clazz);
        }
    }

    private Object createObjectWithAutowiredConstructor(Optional<Constructor<?>> annotatedConstructor) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<?> constructor = annotatedConstructor.get();
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        List<Object> params = Arrays.stream(parameterTypes)
                .map(
                        cl -> {
                            try {
                                return get(cl.getAnnotation(Component.class).value());
                            } catch (Exception e) {
                                throw new RuntimeException("Такой тип нельзя подставлять как параметр");
                            }
                        }

                ).collect(Collectors.toList());
        return constructor.newInstance(params.toArray());
    }

    private Object createObjectWithDefaultConstructor(Class<?> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Object newObject = clazz.getConstructor().newInstance();
        Field[] declaredFields = clazz.getDeclaredFields();
        List<Field> autowiredList = Arrays.stream(declaredFields)
                .filter(field -> field.isAnnotationPresent(Autowired.class))
                .collect(Collectors.toList());
        if(!autowiredList.isEmpty()) {
            autowiredList.stream()
                    .forEach(field -> {
                        field.setAccessible(true);
                        try {
                            field.set(newObject, get(field.getType().getAnnotation(Component.class).value()));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
        return newObject;
    }
}