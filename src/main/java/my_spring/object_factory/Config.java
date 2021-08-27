package my_spring.object_factory;

/**
 * @author Evgeny Borisov
 */
public interface Config {
    <T> Class<T> getImplClass(Class<T> type);
}
