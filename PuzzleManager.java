package Puzzle;

import Answer.DataMuseSolver;
import Parser.HTMLParser;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PuzzleManager {
    private Matrix puzzle;
    private static final int matrixSize = 5;
    private Hint[][] hints;
    private DataMuseSolver solver;
    public ArrayList<String> scores_locations;
    public boolean isFinished;
    public int maxIndex;
    public int sleep_millis;

    public PuzzleManager(){
        puzzle = new Matrix();
        hints = new Hint[2][matrixSize]; /** 0 index -> ACROSS hints + 1 index -> DOWN hints */
        solver = new DataMuseSolver();
        scores_locations = new ArrayList<String>();
        sleep_millis = 100;
    }
    public int getMatrixSize(){
        return matrixSize;
    }
    public String getMatrixElement(int i, int j){
        return puzzle.returnChar(i,j);
    }
    public String getHint(int i, int j){
        return hints[i][j].getHint();
    }
    public void setMatrixElement(int i, int j, String c){
        puzzle.setChar(i,j,c);
    }

    public ArrayList<String> solveDataMuse(String hint, int size){
        solver.checkDatamuse(hint, size);
        //System.out.println("Hint: " + hint);
        int arraySize = solver.results.size();
        String[] ansArr = new String[arraySize];
        for(int i = 0; i < arraySize; i++){
            ansArr[i] = solver.results.get(i);
            //System.out.println("Answers: " + ansArr[i]);
        }
        return solver.results;
    }

    public String getSuitableAnswers(String[][] answers_list, HTMLParser htmlparse) {

        ArrayList<String> suitableAnswers;
        suitableAnswers = new ArrayList<String>();

        ArrayList<Integer> scores;


        scores = new ArrayList<Integer>();
        char[][] locations = new char[5][5];


        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                locations[i][j] = '0';
            }
        }

        for(int i = 0; i < answers_list.length; i++){
            if(answers_list[i].length == 0){
                String[] empty = new String[1];
                empty[0] = "empty";
                answers_list[i] = empty;
            }
            for(int j = 0; j < answers_list[i].length; j++){
                //System.out.print(answers_list[i][j] + " ");
            }
            //System.out.println();
        }

        boolean isAcross = false;
        int startX = 0;
        int startY = 0;

        for (int z = 0; z < 2; z++) {
            for (int y = 0; y < 5; y++) {
                for (int a = 0; a < answers_list[z*5+y].length; a++) {

                    int letter_count = 0;
                    for (int b = 0; b < 5; b++) {
                        if(z == 0) { // Across filled first
                            if (!(htmlparse.isBlank(b, y))) {
                                locations = fitLocation(answers_list[z*5+y][a], y,b, true, locations);
                                break;
                            }
                        }
                        else{ // Down filled first
                            if (!(htmlparse.isBlank(y, b))) {

                                locations = fitLocation(answers_list[z*5+y][a], b,y, false, locations);

                                break;
                            }
                        }
                    }

                    for (int i = 0; i < 2; i++) { // Across / Down
                        for (int k = 0; k < 5; k++) { //  5 hints for each
                            if ((k+5*i) != (z*5+y)) { // Should not enter when both is 0, already determined
                                for (int j = 0; j < answers_list[i * 5 + k].length; j++) { // Traverse possible answers
                                    // Check if the string is suitable by calling isFitting.
                                    // Find start X and Y for each answer:
                                    startX = 0;
                                    startY = 0;
                                    if (i == 0) {
                                        isAcross = true;
                                        startX = k;
                                        while (htmlparse.isBlank(startX, startY)) {
                                            startY = startY + 1;
                                        }
                                    } else {
                                        isAcross = false;
                                        startY = k;
                                        while (htmlparse.isBlank(startX, startY)) {
                                            startX = startX + 1;
                                        }
                                    }
                                    if (isFitting(answers_list[i * 5 + k][j], startX, startY, isAcross, locations)) {

                                        /**for(int erim = 0; erim < 5; erim++){
                                            for(int erim1 = 0; erim1 < 5; erim1++) {
                                                System.out.print(locations[erim][erim1] + " ");
                                            }
                                            System.out.println();
                                        }
                                        System.out.println("-------");
                                        System.out.println("Check if fits " + a + "'th time: ");*/

                                        locations = fitLocation(answers_list[i * 5 + k][j], startX, startY, isAcross, locations);

                                        /**for(int erim = 0; erim < 5; erim++){
                                            for(int erim1 = 0; erim1 < 5; erim1++) {
                                                System.out.print(locations[erim][erim1] + " ");
                                            }
                                            System.out.println();
                                        }
                                        System.out.println("-------");*/
                                    }
                                }
                            }
                        }
                    }
                    // Here we need a scoring and a reset possibly.
                    // Scoring is done here.
                    // Encode locations into a 25 digit string.
                    String encoded = "";
                    for(int encode = 0; encode < 5; encode++){
                        for(int encode2 = 0; encode2 <5; encode2++){
                            encoded = encoded + locations[encode][encode2];
                        }
                    }

                    try
                    {
                        Thread.sleep(sleep_millis);
                    }
                    catch(InterruptedException ex)
                    {
                        Thread.currentThread().interrupt();
                    }

                    scores_locations.add(encoded);
                    scores.add(checkScores(locations, htmlparse));


                    // Just make the reset now:
                    for (int reset = 0; reset < 5; reset++) {
                        for (int reset2 = 0; reset2 < 5; reset2++) {
                            locations[reset][reset2] = '0';
                        }
                    }
                }
            }
        }

        /**
        for(int i = 0; i < scores.size(); i++){
            System.out.println("Score of this solution is: " + scores.get(i));
            for (int x = 0; x < 5; x++) {
                for (int y = 0; y < 5; y++) {
                    System.out.print(locations[x][y] + " ");
                }
                System.out.println();
            }
        }*/

        int maxScore = scores.get(0);
        int maxIndex = 0;
        for(int i = 0; i < scores.size(); i++){
            if(scores.get(i) > maxScore){
                maxScore = scores.get(i);
                maxIndex = i;
            }
        }

        System.out.println("Maximum score = " + maxScore);
        System.out.println("Maximum layout = ");
        //System.out.println("String form = " + scores_locations.get(maxIndex));
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                System.out.print(scores_locations.get(maxIndex).charAt(i*5+j) + " ");
                //System.out.println(htmlparse.puzzle[i][j].getLetterOnCell());
            }
            System.out.println();
        }

        scores_locations.add(scores_locations.get(maxIndex));
        return scores_locations.get(maxIndex);
    }

    public boolean isFitting(String answer, int startX, int startY, boolean isAcross, char[][] locations){
        int currentX;
        int currentY;
        currentX = 0;
        currentY = 0;
        for(int i = 0; i < answer.length(); i++){
            if(!isAcross){
                currentX = startX + i;
                currentY = startY;
                if(locations[currentX][currentY] != '0') {
                    if (locations[currentX][currentY] != answer.charAt(i)) {
                        //System.out.println(answer + " is not fitting by char " + answer.charAt(i) + " in location: " + currentX + " " + currentY);
                        return false;
                    }
                }
            }
            else{
                currentX = startX;
                currentY = startY + i;
                if(locations[currentX][currentY] != '0') {
                    if (locations[currentX][currentY] != answer.charAt(i)) {
                        //System.out.println(answer + " is not fitting by char " + answer.charAt(i) + " in location: " + currentX + " " + currentY);
                        return false;
                    }
                }
            }
        }

        return true;
    }
    public char[][] fitLocation(String answer, int startX, int startY, boolean isAcross, char[][] locations){
        //System.out.println(answer + " is fitting  in location: " + startX + " " + startY);
        int currentX = startX;
        int currentY = startY;
        for(int i = 0; i < answer.length(); i++){
            if(!isAcross) {
                locations[currentX][currentY] = answer.charAt(i);
                currentX = currentX + 1;
            }
            else{
                locations[currentX][currentY] = answer.charAt(i);
                currentY = currentY + 1;
            }
        }
        return locations;
    }

    public int checkScores(char[][] locations, HTMLParser htmlparse){
        // Traverse array and check if answers are correct answers.
        int score = 0;
        // For each correct word add +100 score
        // For each wrong/missing word substract -25
        for(int i = 0; i < 2; i++){ // Across and down
            for(int j = 0; j < 5; j++){ // 5 hints
                boolean isWordTrue = true;
                //String hint = htmlparse.hints[i].get(j); // That specific hint.
                //int hint_length = htmlparse.hint_length[i*5+j]; // Length of the hint
                // if i = 0, across hint, if i = 1, down hint.
                // if i = 0 & j = 0,1,2,3,4 -> it should be checked across.
                // if i = 1 & j = 0,1,2,3,4 -> it should be checked down.
                if(i == 0){ // Across
                    for(int k = 0; k < 5; k++) { // Each hint has to traverse 5 letters.
                        // Puzzle in htmlparse is reversed, so j/k is the correct order
                        // j -> columns / k -> rows. rows are growing faster, across = columns growing faster.
                        if (htmlparse.puzzle[k][j].getLetterOnCell() != "-1") {
                            //System.out.print("Puzzle letter: " + htmlparse.puzzle[k][j].getLetterOnCell());
                            //System.out.print(" Locations letter: " + Character.toUpperCase(locations[j][k]));
                            //System.out.println();
                            if(!(htmlparse.puzzle[k][j].getLetterOnCell().equals("" + Character.toUpperCase(locations[j][k])))){
                                isWordTrue = false;
                            }
                        }
                    }
                }
                else{ // Down
                    for(int k = 0; k < 5; k++) {
                        if (htmlparse.puzzle[j][k].getLetterOnCell() != "-1") {
                            //System.out.print("Puzzle letter: " + htmlparse.puzzle[j][k].getLetterOnCell());
                            //System.out.print(" Locations letter: " + Character.toUpperCase(locations[k][j]));
                            //System.out.println();
                            if (!(htmlparse.puzzle[j][k].getLetterOnCell().equals("" + Character.toUpperCase(locations[k][j])))) {
                                isWordTrue = false;
                            }
                        }
                    }

                }
                if(isWordTrue){
                    int score_weight = (int)(Math.random() * 25000 + 1);
                    score = score + score_weight;
                }
                else{
                    int score_weight = (int)(Math.random() * 5000 + 1);
                    score = score + score_weight;
                }
            }
        }
        System.out.println("Calculating score of candidate: ");
        System.out.println("Score of this solution is: " + score);
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                System.out.print(locations[x][y] + " ");
            }
            System.out.println();
        }
        System.out.println("--------");

        /**
        for(int x = 0; x < 5; x ++){
            for(int y =0; y < 5; y++){
                System.out.print(htmlparse.puzzle[x][y].getLetterOnCell() + " ");
            }
            System.out.println();
        }
         */
        return score;
    }
}
