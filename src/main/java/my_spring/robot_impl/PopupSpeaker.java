package my_spring.robot_impl;

import javax.swing.*;

/**
 * @author Evgeny Borisov
 */
public class PopupSpeaker implements Speaker {
    @Override
    public void speak(String message) {
        JOptionPane.showMessageDialog(null,message);
    }
}
