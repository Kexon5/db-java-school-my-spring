package my_spring.robot_impl;

/**
 * @author Evgeny Borisov
 */
public class ConsoleSpeaker implements Speaker {
    private int volume;
    @Override
    public void speak(String message) {
        System.out.println(message);
    }
}
