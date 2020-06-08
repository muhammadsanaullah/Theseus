package Puzzle;

public class Cell {
    private String questionNumber;
    private String letterOnCell;
    public Cell(){
        letterOnCell = "";
        questionNumber = "";
    }
    public String getQuestionNumber(){
        return questionNumber;
    }
    public void setQuestionNumber(String number_of_question){
        questionNumber = number_of_question;
    }
    public String getLetterOnCell(){
        return letterOnCell;
    }
    public void setLetterOnCell(String cell_letter){
        letterOnCell = cell_letter;
    }

}
