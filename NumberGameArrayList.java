package Project2;

//Imports classes used throughout the project
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Stack;

/*************************************************************************

    This class used the NumberSlider Class, and most of the essential code
    is here
    @author Zarrukh Bazarov
    @version August 2019
 *************************************************************************/
public class NumberGameArrayList implements NumberSlider {
    private int gameScore = 0;

    //This is the game board that will be used throughout the whole project.
    private int board[][];
    //This can be set to end the game once a certain value is reached.
    private int winningValue;
    //Uses the random math class to create random numbers.
    Random rand = new Random();
    //This is used in the undo method to push and pop from the stack.
    private Stack<ArrayList<Cell>> stack = new Stack<ArrayList<Cell>>();

    /*********************************************************************
    This class has parameters of integers height, width and a winning value.
    This method doesn't return anything.
    There is a try and catch Illegal Argument Exception that catch if the
     winning value isn't a power of 2.  This is needed because otherwise
     the exact winning value will never be reached.
    **********************************************************************/
    public void resizeBoard(int height, int width, int winningValue){
        board = new int[height][width];
        double testVal = (double) winningValue;
        while(testVal > 1){
            testVal = testVal / 2;
        }
        try{
            this.winningValue = winningValue;
            if(this.winningValue < 0 || testVal != 1){
                throw new IllegalArgumentException();
            }
        }
        catch(IllegalArgumentException e){
            System.out.println("Enter a winning value with the power " +
                    "of 2!");
        }
    }

    /*********************************************************************
    This method resets the board and then inputs two values(either 2 or 4)
    into the board in 2 random locations.
    *********************************************************************/
    public void reset(){
        //This double for loop clears the board to have all values of the
        // array  equal zero.
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[0].length; j++){
                board[i][j] = 0;
            }
        }
        //These are the random values that will be used to decide where
        // the two  new tiles are inserted.
        int r1 = rand.nextInt(board.length);
        int r2 = rand.nextInt(board[0].length);
        int r3 = rand.nextInt(board.length);
        int r4 = rand.nextInt(board[0].length);

        //This while loop makes sure that the random tiles are not placed
        // in the same spot.
        while(r1 == r3 && r2 == r4){
            r3 = rand.nextInt(board.length);
            r4 = rand.nextInt(board[0].length);
        }

        //These two random values are used to decide if the new tile value
        // is a two or a four.
        int r5 = rand.nextInt(10);
        int r6 = rand.nextInt(10);

        //There is a 20% chance of a 4 and a 80% chance of a four.
        if(r5 == 0 || r5 == 1){
            board[r1][r2] = 4;
        }
        else{
            board[r1][r2] = 2;
        }

        if(r6 == 0 || r6 == 1){
            board[r3][r4] = 4;
        }
        else{
            board[r3][r4] = 2;
        }

        stack.push(getNonEmptyTiles());
    }

    /**********************************************************************
    This method has parameters of a final integer array called ref.
    This method sets the Values of the board to the ref array.
     *********************************************************************/

    public void setValues(final int[][] ref){
        board = new int[ref.length][ref[0].length];
        for(int i = 0; i < ref.length; i++){
            for(int a = 0; a < ref[0].length; a++){
                board[i][a] = ref[i][a];
            }
        }
        stack.push(getNonEmptyTiles());
    }

    /**********************************************************************
      This method is used to place the next random value after each move.
      This method returns a Cell if the board is not full.
    **********************************************************************/
    public Cell placeRandomValue(){
        //This part of the method checks to see if the board has any open
        // cells
        int area = board.length * board[0].length;
        int count = 0;
        for(int i = 0; i < board.length; i++){
            for(int a = 0; a < board[0].length; a++){
                if(board[i][a] != 0){
                    count++;
                }
            }
        }
        //If there are no open cells then an Illegal State Exception is
        // thrown and the game is over.
        if(area == count){
            throw new IllegalStateException();
        }
        //If the board isn't full then a random cell that is open is
        // selected to hold the next value.
        int r1 = rand.nextInt(board.length);
        int r2 = rand.nextInt(board[0].length);
        while(board[r1][r2] != 0){
            r1 = rand.nextInt(board.length);
            r2 = rand.nextInt(board[0].length);
        }
        //This has the same logic from the reset method where there is a
        // 20% chance of the new value being a 4 and a 80% chance of the
        // value being a 2.
        int r3 = rand.nextInt(10);
        if(r3 == 0 || r3 == 1){
            board[r1][r2] = 4;
        }
        else{
            board[r1][r2] = 2;
        }

        Cell value = new Cell(r1, r2, 2);
        return value;
    }

    /**********************************************************************
    This method is used to slide a cell.  A boolean is returned.

     *********************************************************************/
    public boolean slide (SlideDirection dir){
        //This array is used to stores the previous board to be used.
        int[][] lastBoard = new int[board.length][board[0].length];
        //This double loop copies the board into the lastBoard array.
        for(int i = 0; i < board.length; i++){
            for(int a = 0; a < board[0].length; a++){
                lastBoard[i][a] = board[i][a];
            }
        }
        stack.push(getNonEmptyTiles());
        //This if statement is used to determine if the direction is left
        // or right.  If it is up or down then it moves onto the next if
        // statement.
        int[] tempRow = new int[0];
        if(dir == SlideDirection.RIGHT || dir == SlideDirection.LEFT){
            tempRow = new int[board[0].length];
            //This loop is used to slide each row individually right or
            // left.
            for(int i = 0; i < board.length; i++){
                if(dir == SlideDirection.RIGHT){
                    for(int a = 0; a < board[0].length; a++){
                        tempRow[a] = board[i][board[0].length - 1 - a];
                    }
                    //The helper method is called here to actually slide
                    // the row.
                    tempRow = slideRow(tempRow);
                    //The array is then flipped back because in the helper
                    // method the array is flipped to then slide left.
                    tempRow = flip(tempRow);
                }
                if(dir == SlideDirection.LEFT){
                    for(int a = 0; a < board[0].length; a++){
                        tempRow[a] = board[i][a];
                    }
                    //Because the direction is left, the flip method
                    // doesn't have to be called because the flip method
                    // slides left.
                    tempRow = slideRow(tempRow);
                }
                //This loop is setting the rows to the tempRow[] because
                // the slideRow method slides left.  This allows for one
                // method to be called.
                for(int b = 0; b < board[0].length; b++){
                    board[i][b] = tempRow[b];
                }
            }
        }
        if(dir == SlideDirection.UP || dir == SlideDirection.DOWN){
            tempRow = new int[board.length];
            for(int i = 0; i < board[0].length; i++){
                if(dir == SlideDirection.UP){
                    for(int a = 0; a < board.length; a++){
                        tempRow[a] = board[a][i];
                    }
                    tempRow = slideRow(tempRow);
                }
                if(dir == SlideDirection.DOWN){
                    for(int a = 0; a < board.length; a++){
                        tempRow[a] = board[board.length - 1 - a][i];
                    }
                    //Just like the slide right, we have to flip the board
                    // back.
                    tempRow = slideRow(tempRow);
                    tempRow = flip(tempRow);
                }
                for(int b = 0; b < board.length; b++){
                    board[b][i] = tempRow[b];
                }
            }
        }
        //This if checks to make sure the board isn't full or that a tile
        // can slide in the direction the user wants it to.
        if(Arrays.deepEquals(lastBoard, board)){
            stack.pop();
            return false;
        }
        placeRandomValue();
        return true;
    }

    /**********************************************************************
    This is the helper method discussed in the slide method.
     The method has an integer array as parameters and returns an integer
     array
    **********************************************************************/
    private int[] slideRow(int[] row){
        int tempCount;
        for(int i = 0; i < row.length - 1; i++){
            tempCount = 0;
            do{
                tempCount++;
                //This statement checks if it is at the end of the row.
                if(i + tempCount == row.length){
                    break;
                }
                //This gets out if the value is a zero because there is no
                // calculation  if it is a zero.
                if(row[i] == 0){
                    break;
                }
                //This loop combines terms that need to be combined.
                if(row[i] == row[i + tempCount]){
                    row[i] *= 2;
                    row[i + tempCount] = 0;
                    gameScore += row[i];
                    break;
                }
            }
            //This executes the do loop while the next value is a zero.
            while(row[i + tempCount] == 0);
        }
        //This part slides the cells to be next be next to each other
        // without combination.
        for(int a = 0; a  < row.length; a++){
            //First for loop makes sure each space can slide the full length.
            for(int b = row.length - 1; b > 0; b--){
                if(row[b-1] == 0){
                    row[b - 1] = row[b];
                    row[b] = 0;
                }
            }
        }
        return row;
    }

    /**********************************************************************
    This method is used to flip the array as a helper method
    to make sliding the row easier.
    An integer array row is the parameters and it is also what
    is returned.
    **********************************************************************/
    private int[] flip(int[] row){
        //This is the loop that flips the array.
        for(int i = 0; i < row.length/2; i++){
            int temp = row[i];
            row[i] = row[row.length - 1 - i];
            row[row.length - 1 - i] = temp;
        }
        return row;
    }

    /**********************************************************************
    This method gets a list of tiles that are not empty.
    The method returns a cell array.
    **********************************************************************/
    public ArrayList<Cell> getNonEmptyTiles(){
        ArrayList<Cell> list = new ArrayList<Cell>();
        for(int i = 0; i < board.length; i++){
            for(int a = 0; a < board[0].length; a++){
                if(board[i][a] != 0){
                    list.add(new Cell(i,a,board[i][a]));
                }
            }
        }
        return list;
    }

    /**********************************************************************
    This method returns the Game Status.

    The method is used to tell if the game is over or not.
    **********************************************************************/
    public GameStatus getStatus(){
        boolean lost = true;
        //This double loop is checking to see if there is an available
        //combination of tiles that can be made.
        for(int i = 0; i < board.length - 1; i++){
            for(int a = 0; a < board[0].length - 1; a++){
                if(board[i][a] == board[i][a + 1] || board[i][a] ==
                        board[i + 1] [a] || board[i][a] == 0 ||
                        board[i + 1][a] == 0 || board[i][a + 1]
                        == winningValue){
                    lost = false;
                }
                //This if is checking to see if the user has reached the
                // winning value.
                if(board[i][a] == winningValue || board[i + 1][a]
                        == winningValue || board[i][a + 1] == winningValue
                        || board[i + 1][a + 1] == winningValue){
                    return GameStatus.USER_WON;
                }
            }
        }
        if(lost){
            return GameStatus.USER_LOST;
        }
        return GameStatus.IN_PROGRESS;
    }

    /**********************************************************************
    This method is used to return the board to the previous boards state.
     *********************************************************************/
    public void undo(){
        //This loop is setting the board to all zeros.
        for(int i = 0; i < board.length; i++){
            for(int a = 0; a < board[0].length; a++){
                board[i][a] = 0;
            }
        }
        //This part of the method is looking at the stack to see what the
        // previous board was and then clones that board into the board
        // with all zeros.
        for(Cell c: stack.peek()){
            board.clone()[c.row][c.column] = c.value;
        }
        //The board is then popped off the stack and undo has been
        // accomplished.
        stack.pop();
    }

    /**********************************************************************
    This method is used to print the board.
     *********************************************************************/
    public void printBoard(){
        //This double loop prints out the board for use.
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[0].length; j++){
                System.out.println(" " + board[i][j] + " ");
            }
            System.out.println("");
        }
    }

    /**********************************************************************
    This is the main method that is used for testing to call the other
    methods to create the board with the winning value.
    **********************************************************************/
    public static void main(String[] args){
        NumberGameArrayList test = new NumberGameArrayList();
        int[][] ary = {{2,2,2},{4,4,4},{0,0,0}};
        test.setValues(ary);
        test.printBoard();
    }

    /**********************************************************************
        Returning Score of the game
     **********************************************************************/
    public int getScore(){
        return gameScore;
    }
}
