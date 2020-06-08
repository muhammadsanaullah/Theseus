package Puzzle;

public class Matrix {
    private static final int PUZZLESIZE = 5;
    private Cell[][] matrice;
    public Matrix(){
        matrice = new Cell[PUZZLESIZE][PUZZLESIZE];
        for(int i = 0; i < PUZZLESIZE; i++){
            for(int j = 0; j < PUZZLESIZE; j++){
                matrice[i][j] = new Cell();
            }
        }
    }
    public int getCellSize(){
        return PUZZLESIZE;
    }
    public String returnChar(int i,int j){
        return matrice[i][j].getLetterOnCell();
    }
    public void setChar(int i, int j, String c){
        matrice[i][j].setLetterOnCell(c);
    }
}
