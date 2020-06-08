package UI;
import Downloader.SeleniumDownloader;
import Parser.HTMLParser;
import Puzzle.PuzzleManager;
import java.awt.event.KeyEvent;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static jdk.nashorn.internal.objects.NativeString.toUpperCase;

public class PuzzlePanel extends JPanel{
    private MouseListener listen;
    private KeyboardListener keyListen;
    private BufferedImage logo;
    private char chartoprint;
    private int[] currentCell;
    private boolean isRevealed = false;
    private boolean isSolved = false;
    private HTMLParser htmlparse;
    private String[] log = new String[5];
    private int counterStringLog = 2;
    private ArrayList<String> answers;
    String[][] answers_list; // First 5 elements are across, other 5 elements are down in order.
    PuzzleManager manager;
    String suitableAnswers = "";
    boolean counter_log = false;
    boolean sleep_fast = true;
    String puzzle_date = "10-12-2018";
    boolean once = true;

    public PuzzlePanel(int width,int height){
        init(width,height);
    }
    private void init(int width, int height){
        htmlparse = new HTMLParser();
        // 17 - 12 - 2018 - 3 adet

        // 11 - 10 - 2018 - 5 adet
        // 23 - 10 - 2018 - 10 adet
        // 25 - 10 - 2018 - 5 adet
        // 30 - 10 - 2018 - 5 adet
        // 2 - 11 - 2018 - 10 adet
        // 4 - 11 - 2018 - 5 adet
        // 5 - 12 - 2018 - 10 adet
        // 9 - 12 - 2018 - 10 adet
        // 10 - 12 - 2018 - 7 adet
        // 16 - 12 - 2018 - 7 adet

        if(puzzle_date == "17-12-2018"){
            SeleniumDownloader erim = new SeleniumDownloader();

        }
        htmlparse.readPuzzle(puzzle_date);

        answers_list =  new String[10][];
        answers = new ArrayList<String>();
        manager = new PuzzleManager();


        //suitableAnswers = manager.getSuitableAnswers(answers_list, htmlparse);

        for(int i = 0; i < 5; i++){
            log[i] = "";
        }
        this.setFocusable(true);
        this.requestFocus();
        chartoprint = ' ';
        currentCell = new int[2];
        currentCell[0] = 0;
        currentCell[1] = 0;
        Dimension dimension = new Dimension(width,height);
        setVisible(true);
        this.setPreferredSize(dimension);
        listen = new MouseListener();
        this.addMouseListener(listen);
        keyListen = new KeyboardListener();
        this.addKeyListener(keyListen);
        // Add logo
        try{
            logo = ImageIO.read(getClass().getResourceAsStream("/resources/images/Theseus_emblem.png"));
        }
        catch(IOException exception){
            exception.printStackTrace();
        }

        repaint();
    }
    public void paint(Graphics g){
        paint_background(g);
        paint_puzzle(g);
        paint_hint_blocks(g);
        paint_amblem(g);
        if(isRevealed) {
            paint_revealed_puzzle(g);
        }
        paint_buttons(g);
        paint_images(g);
        paint_text_box(g);
    }
    private void paint_puzzle(Graphics g){
        g.setColor(Color.white);
        int startX = 50;
        int currentX = startX;
        int currentY = 125;
        int rectangle_size = 75;
        int white_space = 3;
        boolean hasDrawn;

        // Test if first char of hint is the number of question.
        // System.out.println("First letter: " + htmlparse.hints[1].get(0).substring(0,1));
        int black_counter_down = 0;

        for(int i = 0; i < manager.getMatrixSize(); i++){
            for(int j = 0; j < manager.getMatrixSize(); j++){
                hasDrawn = false;
                g.setColor(Color.white);
                if(htmlparse.puzzle[j][i].getLetterOnCell() == "-1"){
                    g.setColor(Color.black);
                }
                if(htmlparse.puzzle[i][j].getLetterOnCell() == "-1" && j == 0){
                    black_counter_down = black_counter_down + 1;
                }
                g.fillRect(currentX,currentY,rectangle_size,rectangle_size);
                // TODO: BUGGY Number code !, Check if it works with other examples.
                g.setColor(Color.black);
                if(htmlparse.puzzle[j][i].getLetterOnCell() != "-1") {
                    // This will only change x while y is stable:
                    g.setColor(Color.black);
                    g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
                    //g.drawString(htmlparse.hints[0].get(i-black_counter_down).substring(0,1), currentX + 10, currentY + 15); // down
                    g.drawString(htmlparse.puzzle[j][i].getQuestionNumber(), currentX + 10, currentY + 15);
                }

                g.setFont(new Font("TimesRoman",Font.PLAIN,35));
                g.drawString(toUpperCase(""+ manager.getMatrixElement(i,j)),currentX+25,currentY+50);


                if(manager.scores_locations.size() != 0) {
                    if(!hasDrawn) {
                        if (manager.scores_locations.get(manager.scores_locations.size() - 1).charAt(i * 5 + j) != '0') {
                            g.drawString("" + toUpperCase(manager.scores_locations.get(manager.scores_locations.size() - 1).charAt(i * 5 + j)), currentX + 35, currentY + 50);
                        }
                        hasDrawn = true;
                    }
                }


                currentX = currentX + white_space + rectangle_size;
            }
            currentX = startX;
            currentY = currentY + white_space + rectangle_size;
        }

        repaint();
    }
    private void paint_hint_blocks(Graphics g){
        g.setColor(Color.white);
        int startX = 475;
        int startY = 125;
        int width = 250;
        int height = 388;
        g.fillRect(startX,startY,width,height);
        int startAcrossX = startX + 10;
        int startAcrossY = startY + 25;
        g.setColor(Color.black);
        g.setFont(new Font("TimesRoman",Font.PLAIN,10));
        g.drawLine(startX ,startY + 27, startX + 250, startY + 27);
        g.drawString("ACROSS",startAcrossX,startAcrossY);
        // Insert here the hints for across
        int hintsX = startX;
        int hintsY = startY + 35;
        for(int i = 0; i < htmlparse.hints[1].size(); i++) {
            g.drawString(htmlparse.hints[0].get(i), hintsX + 15, hintsY + 35);
            hintsY = hintsY + 50;

        }
        g.setColor(Color.white);
        int currentX = startX + width + 5;
        g.fillRect(currentX, startY, width, height);
        int startDownX = startX + 250 + 5;
        int startDownY = startY + 25;
        g.setColor(Color.black);
        g.setFont(new Font("TimesRoman",Font.PLAIN,25));
        g.drawString("DOWN",startDownX,startDownY);
        g.drawLine(startDownX ,startDownY + 2, startDownX + 250, startDownY + 2);
        // Insert here the hints for down
        g.setFont(new Font("TimesRoman",Font.PLAIN,10));
        hintsX = startDownX;
        hintsY = startDownY + 10;
        for(int i = 0; i < htmlparse.hints[0].size(); i++) {
            g.drawString(htmlparse.hints[1].get(i), hintsX + 15, hintsY + 35);
            hintsY = hintsY + 50;

        }
        repaint();
    }
    private void paint_amblem(Graphics g){
        g.setColor(Color.black);
        int rectangle_size = 75;
        int startX = 50 + rectangle_size;
        int startY = 15;
        int width = 750;
        int height = 85;
        g.fillRect(startX,startY,width,height);
        repaint();
    }

    private void paint_revealed_puzzle(Graphics g){
        g.setColor(Color.white);
        int startX = 700;
        int currentX = startX;
        int currentY = 525;
        int rectangle_size = 50;
        int white_space = 1;
        for(int i = 0; i < manager.getMatrixSize(); i++){
            for(int j = 0; j < manager.getMatrixSize(); j++){
                g.setColor(Color.white);
                if(htmlparse.puzzle[j][i].getLetterOnCell() == "-1"){
                    g.setColor(Color.black);
                }
                g.fillRect(currentX,currentY,rectangle_size,rectangle_size);
                g.setColor(Color.black);
                g.setFont(new Font("TimesRoman",Font.PLAIN,25));
                g.drawString(htmlparse.puzzle[j][i].getLetterOnCell(), currentX + 20, currentY + 35);
                currentX = currentX + white_space + rectangle_size;
            }
            currentX = startX;
            currentY = currentY + white_space + rectangle_size;
        }
        repaint();
    }
    private void paint_text_box(Graphics g){
        g.setColor(Color.white);
        int startX = 500;
        int startY = 525;
        int currentX = startX;
        int currentY = startY;
        g.fillRect(startX, startY, 200,300);
        g.setColor(Color.black);
        g.setFont(new Font("TimesRoman",Font.PLAIN,25));
        g.drawString("LOG", startX + 20, startY + 35);
        g.setFont(new Font("TimesRoman",Font.PLAIN,15));


        //g.drawString("Opening NYT!", startX + 20, startY + 50);

        //g.drawString("Parsing is complete!", startX + 20, startY + 65);
        if(once) {
            if (puzzle_date == "17-12-2018") {
                log[0] = "Connected to Selenium!";
                log[1] = "Parsing is complete!";
            } else {
                log[0] = "Old date from storage!";
                log[1] = "Parsing old date is complete!";
            }
            once = false;
        }
        for(int i = 0; i < 5; i++){
            g.drawString(log[i] , currentX + 20, currentY + 65);
            currentY = currentY + 20;
        }


    }
    private void paint_buttons(Graphics g){
        g.setColor(Color.black);
        int startX = 89;
        int currentX = startX;
        int currentY = 535;
        int button_width = 150;
        int button_height = 75;

        g.fillRect(currentX,currentY,button_width,button_height);

        g.setColor(Color.white);
        g.setFont(new Font("TimesRoman",Font.PLAIN,25));
        g.drawString("REVEAL",currentX + 15,currentY+40); // Arrange it to middle.

        g.setColor(Color.black);
        currentX = currentX + 165;
        g.fillRect(currentX, currentY, button_width, button_height);

        g.setColor(Color.white);
        g.setFont(new Font("TimesRoman",Font.PLAIN,25));
        g.drawString("HIDE", currentX + 15, currentY+40); // Arranging to middle again.

        g.setColor(Color.black);
        currentY = currentY + button_height + 10;
        currentX = startX;

        g.fillRect(currentX, currentY, button_width, button_height);
        g.setColor(Color.white);
        g.setFont(new Font("TimesRoman",Font.PLAIN,25));
        g.drawString("SOLVE", currentX + 15, currentY+40); // Arranging to middle again.

        currentX = currentX + 165;
        g.setColor(Color.black);

        g.fillRect(currentX,currentY,button_width,button_height);
        g.setColor(Color.white);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 25));
        g.drawString("SPEED", currentX + 15, currentY + 40);
        repaint();

    }
    private void paint_background(Graphics g){
        g.setColor(Color.lightGray);
        int startX = 0;
        int startY = 0;
        int width = 1000;
        int height = 800;
        g.fillRect(startX,startY,width,height);
        repaint();
    }
    private void paint_images(Graphics g){
        int startX= 275;
        int startY = 15;
        g.drawImage(logo,startX,startY,this);
    }

    public class MouseListener extends MouseAdapter{
        private int x;
        private int y;

        public void mousePressed(MouseEvent e){
            x = e.getX();
            y = e.getY();
            if(counterStringLog >= 5){
                counterStringLog = 0;
                counter_log = true;
                for(int i = 0; i < 5; i++){
                    log[i] = "";
                }
            }
            // Check if reveal button is pressed
            if((x >= 89 && y >= 535) && (x <= 239 && y <= 610)){
                isRevealed = true;
                log[counterStringLog] = log[counterStringLog] + "Revealed";
                counterStringLog = counterStringLog + 1;
                //System.out.println("Revealed");
            }
            // Check if hide button is pressed
            if((x >= 89 + 165 && y >= 535) && (x <= 239 + 165 && y <= 610)){
                isRevealed = false;
                log[counterStringLog] = log[counterStringLog] + "Hidden";
                counterStringLog = counterStringLog + 1;
                //System.out.println("Hidden");
            }

            if((x >= 89 + 165 && y >= 535 + 85) && (x <= 239 + 165 && y <= 535 + 85 + 75)){ // Slow and fast
                if(sleep_fast) {
                    manager.sleep_millis = 1000;
                    sleep_fast = false;
                    log[counterStringLog] = log[counterStringLog] + "Single Stepping";
                    counterStringLog = counterStringLog + 1;
                }
                else{
                    log[counterStringLog] = log[counterStringLog] + "Fast Tracking";
                    counterStringLog = counterStringLog + 1;
                    manager.sleep_millis = 100;
                    sleep_fast = true;
                }
            }

            // Check if solve button is pressed
            if((x >= 89 && y >= 535 + 85) && (x <= 239 && y <= 535 + 85 + 75)){
                isSolved = true;
                log[counterStringLog] = log[counterStringLog] + "Solved";
                counterStringLog = counterStringLog + 1;
                System.out.println("Solve is pressed.");
                if(isSolved) {
                    Thread solve = new Thread() {
                        public void run() {
                            for (int i = 0; i < 2; i++) { // Across and down 0.0 -> Across
                                for (int j = 0; j < 5; j++) { // Hints
                                    String answer = "";
                                    if(i == 0) {
                                        for (int k = 0; k < 5; k++) {
                                            if (htmlparse.puzzle[k][j].getLetterOnCell() != "-1") {
                                                answer = answer + htmlparse.puzzle[k][j].getLetterOnCell().toLowerCase();
                                            }
                                        }
                                    }
                                    if(i == 1){
                                        for (int k = 0; k < 5; k++) {
                                            if (htmlparse.puzzle[j][k].getLetterOnCell() != "-1") {
                                                answer = answer + htmlparse.puzzle[j][k].getLetterOnCell().toLowerCase();
                                            }
                                        }
                                    }
                                    //System.out.println("Hints: " + htmlparse.hints[i].get(j));
                                    answers = manager.solveDataMuse(htmlparse.hints[i].get(j), htmlparse.hint_length[i * 5 + j]);
                                    //System.out.println("answers_list[" + (i*5+j) + "] = " + answers);
                                    String[] answers_in_array = new String[answers.size() + 1];
                                    for (int k = 0; k < answers.size(); k++) {
                                        answers_in_array[k] = answers.get(k);
                                    }
                                    answers_in_array[answers.size()] = answer;
                                    answers_list[i * 5 + j] = answers_in_array;
                                    /**for(int k = 0; k < answers.size(); k ++) {
                                     System.out.println(answers.get(k));
                                     }*/
                                }
                            }
                            /**for(int i = 0; i < answers_list.length; i ++){
                             for(int j = 0; j < answers_list[i].length; j++) {
                             System.out.print(answers_list[i][j] + " ");
                             }
                             System.out.println("");
                             }*/

                            suitableAnswers = manager.getSuitableAnswers(answers_list, htmlparse);
                            //System.out.println(puzzle[0][0]+","+puzzle[0][1]+","+puzzle[0][2]);

                        }
                    };
                    solve.start();
                }
            }

            // 25 times puzzles
            if((x >= 50 && y >= 125) && (x <= 125 && y <= 200)){
                currentCell[0] = 0;
                currentCell[1] = 0;
                //System.out.println("Cell number pressed: " + currentCell[0]  +", "  +currentCell[1]);
            }
            if((x >= 128 && y >= 125) && (x <= 203 && y <= 200)){
                currentCell[0] = 0;
                currentCell[1] = 1;
                //System.out.println("Cell number pressed: " + currentCell[0]  +", "  +currentCell[1]);
            }
            if((x >= 206 && y >= 125) && (x <= 281 && y <= 200)){
                currentCell[0] = 0;
                currentCell[1] = 2;
                //System.out.println("Cell number pressed: " + currentCell[0]  +", "  +currentCell[1]);
            }
            if((x >= 284 && y >= 125) && (x <= 359 && y <= 200)){
                currentCell[0] = 0;
                currentCell[1] = 3;
                //System.out.println("Cell number pressed: " + currentCell[0]  +", "  +currentCell[1]);
            }
            if((x >= 362 && y >= 125) && (x <= 437 && y <= 200)){
                currentCell[0] = 0;
                currentCell[1] = 4;
                //System.out.println("Cell number pressed: " + currentCell[0]  +", "  +currentCell[1]);
            }
            if((x >= 50 && y >= 203) && (x <= 125 && y <= 278)){
                currentCell[0] = 1;
                currentCell[1] = 0;
                //System.out.println("Cell number pressed: " + currentCell[0]  +", "  +currentCell[1]);
            }
            if((x >= 128 && y >= 203) && (x <= 203 && y <= 278)){
                currentCell[0] = 1;
                currentCell[1] = 1;
                //System.out.println("Cell number pressed: " + currentCell[0]  +", "  +currentCell[1]);
            }
            if((x >= 206 && y >= 203) && (x <= 281 && y <= 278)){
                currentCell[0] = 1;
                currentCell[1] = 2;
                //System.out.println("Cell number pressed: " + currentCell[0]  +", "  +currentCell[1]);
            }
            if((x >= 284 && y >= 203) && (x <= 359 && y <= 278)){
                currentCell[0] = 1;
                currentCell[1] = 3;
                //System.out.println("Cell number pressed: " + currentCell[0]  +", "  +currentCell[1]);
            }
            if((x >= 362 && y >= 203) && (x <= 437 && y <= 278)){
                currentCell[0] = 1;
                currentCell[1] = 4;
                //System.out.println("Cell number pressed: " + currentCell[0]  +", "  +currentCell[1]);
            }
            if((x >= 50 && y >= 281) && (x <= 125 && y <= 356)){
                currentCell[0] = 2;
                currentCell[1] = 0;
                //System.out.println("Cell number pressed: " + currentCell[0]  +", "  +currentCell[1]);
            }
            if((x >= 128 && y >= 281) && (x <= 203 && y <= 356)){
                currentCell[0] = 2;
                currentCell[1] = 1;
                //System.out.println("Cell number pressed: " + currentCell[0]  +", "  +currentCell[1]);
            }
            if((x >= 206 && y >= 281) && (x <= 281 && y <= 356)){
                currentCell[0] = 2;
                currentCell[1] = 2;
                //System.out.println("Cell number pressed: " + currentCell[0]  +", "  +currentCell[1]);
            }
            if((x >= 284 && y >= 281) && (x <= 359 && y <= 356)){
                currentCell[0] = 2;
                currentCell[1] = 3;
                //System.out.println("Cell number pressed: " + currentCell[0]  +", "  +currentCell[1]);
            }
            if((x >= 362 && y >= 281) && (x <= 437 && y <= 356)){
                currentCell[0] = 2;
                currentCell[1] = 4;
                //System.out.println("Cell number pressed: " + currentCell[0]  +", "  +currentCell[1]);
            }
            if((x >= 50 && y >= 359) && (x <= 125 && y <= 434)){
                currentCell[0] = 3;
                currentCell[1] = 0;
                //System.out.println("Cell number pressed: " + currentCell[0]  +", "  +currentCell[1]);
            }
            if((x >= 128 && y >= 359) && (x <= 203 && y <= 434)){
                currentCell[0] = 3;
                currentCell[1] = 1;
                //System.out.println("Cell number pressed: " + currentCell[0]  +", "  +currentCell[1]);
            }
            if((x >= 206 && y >= 359) && (x <= 281 && y <= 434)){
                currentCell[0] = 3;
                currentCell[1] = 2;
                //System.out.println("Cell number pressed: " + currentCell[0]  +", "  +currentCell[1]);
            }
            if((x >= 284 && y >= 359) && (x <= 359 && y <= 434)){
                currentCell[0] = 3;
                currentCell[1] = 3;
                //System.out.println("Cell number pressed: " + currentCell[0]  +", "  +currentCell[1]);
            }
            if((x >= 362 && y >= 359) && (x <= 437 && y <= 434)){
                currentCell[0] = 3;
                currentCell[1] = 4;
                //System.out.println("Cell number pressed: " + currentCell[0]  +", "  +currentCell[1]);
            }
            if((x >= 50 && y >= 437) && (x <= 125 && y <= 512)){
                currentCell[0] = 4;
                currentCell[1] = 0;
                //System.out.println("Cell number pressed: " + currentCell[0]  +", "  +currentCell[1]);
            }
            if((x >= 128 && y >= 438) && (x <= 203 && y <= 512)){
                currentCell[0] = 4;
                currentCell[1] = 1;
                //System.out.println("Cell number pressed: " + currentCell[0]  +", "  +currentCell[1]);
            }
            if((x >= 206 && y >= 438) && (x <= 281 && y <= 512)){
                currentCell[0] = 4;
                currentCell[1] = 2;
                //System.out.println("Cell number pressed: " + currentCell[0]  +", "  +currentCell[1]);
            }
            if((x >= 284 && y >= 438) && (x <= 359 && y <= 512)){
                currentCell[0] = 4;
                currentCell[1] = 3;
                //System.out.println("Cell number pressed: " + currentCell[0]  +", "  +currentCell[1]);
            }
            if((x >= 362 && y >= 438) && (x <= 437 && y <= 512)){
                currentCell[0] = 4;
                currentCell[1] = 4;
                //System.out.println("Cell number pressed: " + currentCell[0]  +", "  +currentCell[1]);
            }
            //System.out.println("currentCell[0]: " + currentCell[0]);
            //System.out.println("currentCell[1]: " + currentCell[1]);

        }
    }
    public class KeyboardListener implements KeyListener{
        @Override
        public void keyPressed(KeyEvent k){
            char c = k.getKeyChar();
            // Stores the char in c
            chartoprint = c;
            manager.setMatrixElement(currentCell[0],currentCell[1],""+c);
            //System.out.println("" + manager.getMatrixElement(currentCell[0],currentCell[1]));
            //System.out.println("Letter entered: " + c);

        }
        public void keyReleased(KeyEvent k){
        }
        public void keyTyped(KeyEvent k){
        }
    }
}
