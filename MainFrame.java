package UI;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame{
    private static final String TITLE = "Theseus";
    private static final int height =  800;
    private static final int width = 1000;
    private PuzzlePanel panel;

    public MainFrame(){
        init();
    }
    private void init() {
        panel = new PuzzlePanel(width,height);
        this.add(panel);
        Dimension size = new Dimension(width, height);
        setSize(size);
        setLayout(new GridLayout(1, 1, 0, 0));
        setTitle(TITLE);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        pack();
    }
    public static void main(String[] args){
        MainFrame frame = new MainFrame();
    }
}
