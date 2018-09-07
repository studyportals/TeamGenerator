package studyportals;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Team Generator");
        TeamGeneratorGui teamGeneratorGui = new TeamGeneratorGui(frame);
        frame.setContentPane(teamGeneratorGui.getMainPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
