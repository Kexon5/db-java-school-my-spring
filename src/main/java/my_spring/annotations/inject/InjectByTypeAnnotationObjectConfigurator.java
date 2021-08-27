package my_spring.annotations.inject;

import lombok.SneakyThrows;
import my_spring.object_factory.ObjectConfigurator;
import my_spring.object_factory.ObjectFactory;

import java.lang.reflect.Field;

/**
 * @author Evgeny Borisov
 */
public class InjectByTypeAnnotationObjectConfigurator implements ObjectConfigurator {



    @Override
    @SneakyThrows
    public void configure(Object t) {
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(InjectByType.class)) {
                field.setAccessible(true);
                Object object = ObjectFactory.getInstance().createObject(field.getType());
                field.set(t, object);
            }
        }
    }
}
