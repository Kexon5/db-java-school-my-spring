package my_spring.annotations.benchmark;

import lombok.SneakyThrows;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

public class ProxyAnnotationHandler {
    private final Reflections scanner = new Reflections("my_spring.annotations.benchmark");
    private final Set<ProxyAnnotationConfigurator> proxyAnnotationConfigurators = new HashSet<>();

    @SneakyThrows
    public ProxyAnnotationHandler() {
        for (var handler : scanner.getSubTypesOf(ProxyAnnotationConfigurator.class)) {
            if (!Modifier.isAbstract(handler.getModifiers())) {
                proxyAnnotationConfigurators.add(handler.getDeclaredConstructor().newInstance());
            }
        }
    }

    public <T> T createProxy(T t) {
        for (ProxyAnnotationConfigurator configurator: proxyAnnotationConfigurators) {
            t = configurator.configure(t);
        }
        return t;
    }
}
