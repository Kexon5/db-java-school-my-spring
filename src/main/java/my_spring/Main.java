package my_spring;

import my_spring.object_factory.ObjectFactory;
import my_spring.robot_impl.IRobot;

/**
 * @author Evgeny Borisov
 */
public class Main {
    public static void main(String[] args) {


       /* ApplicationContext context = new ApplicationContext("com.epam", Map.of(Speaker.class, ConsoleSpeaker.class));
        context.getObject(IRobot.class).cleanRoom();*/


        IRobot iRobot = ObjectFactory.getInstance().createObject(IRobot.class);

        iRobot.cleanRoom();
    }
}
