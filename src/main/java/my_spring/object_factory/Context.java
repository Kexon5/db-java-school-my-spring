package my_spring.object_factory;

import java.util.Map;

/**
 * @author Evgeny Borisov
 */
public interface Context {
    String getPackagesToScan();
    Map<Class,Class> getIfc2ImplClass();

    <T> T getObject(Class<T> type);
}
