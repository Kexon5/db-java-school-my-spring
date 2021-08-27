package my_spring.object_factory;

import my_spring.robot_impl.Cleaner;
import my_spring.robot_impl.CleanerImpl;
import my_spring.robot_impl.ConsoleSpeaker;
import my_spring.robot_impl.Speaker;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Evgeny Borisov
 */
public class JavaConfig implements Config {

    private Map<Class, Class> ifc2ImplClass = new HashMap<>();

    public JavaConfig() {
        ifc2ImplClass.put(Speaker.class, ConsoleSpeaker.class);
        ifc2ImplClass.put(Cleaner.class, CleanerImpl.class);

    }

    @Override
    public <T> Class<T> getImplClass(Class<T> type) {
        return ifc2ImplClass.get(type);
    }
}





