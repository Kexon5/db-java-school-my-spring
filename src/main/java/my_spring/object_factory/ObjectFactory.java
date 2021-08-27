package my_spring.object_factory;

import lombok.Getter;
import lombok.SneakyThrows;
import my_spring.annotations.benchmark.ProxyAnnotationHandler;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

import javax.annotation.PostConstruct;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Evgeny Borisov
 */
public class ObjectFactory {
    @Getter
    private static final ObjectFactory instance = new ObjectFactory();
    private final Config config = new JavaConfig();
    private final Reflections scanner = new Reflections("my_spring");

    private final List<ObjectConfigurator> configurators = new ArrayList<>();
    private final ProxyAnnotationHandler proxyAnnotationHandler = new ProxyAnnotationHandler();

    @SneakyThrows
    public ObjectFactory() {
        Set<Class<? extends ObjectConfigurator>> classes = scanner.getSubTypesOf(ObjectConfigurator.class);
        for (Class<? extends ObjectConfigurator> aClass : classes) {
            if (!Modifier.isAbstract(aClass.getModifiers())) {
                configurators.add(aClass.getDeclaredConstructor().newInstance());
            }
        }
    }




    @SneakyThrows
    public <T> T createObject(Class<T> type) {

        type = resolveImple(type);

        T t = type.getDeclaredConstructor().newInstance();

        configure(t);

        invokeInit(type, t);

        return configurePostAnnotation(t);
    }

    private <T> T configurePostAnnotation(T t) {
        return proxyAnnotationHandler.createProxy(t);
    }


    private <T> void invokeInit(Class<T> type, T t) throws IllegalAccessException, InvocationTargetException {
        Set<Method> allMethods = ReflectionUtils
                .getAllMethods(type, method -> method.isAnnotationPresent(PostConstruct.class));

        for (Method method : allMethods) {
            method.invoke(t);
        }
    }

    private <T> void configure(T t) {
        configurators.forEach(configurator -> configurator.configure(t));
    }

    private <T> Class<T> resolveImple(Class<T> type) {
        if (type.isInterface()) {
            Class<T> implClass = config.getImplClass(type);
            if (implClass == null) {
                Set<Class<? extends T>> classes = scanner.getSubTypesOf(type);
                if (classes.size() != 1) {
                    throw new IllegalStateException(type + " has 0 or more than one impl was found, please update your config");
                }
                implClass = (Class<T>) classes.iterator().next();
            }
            type = implClass;
        }
        return type;
    }


}
