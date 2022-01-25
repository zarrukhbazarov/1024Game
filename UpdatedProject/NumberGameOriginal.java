package UpdatedProject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

/*******************************************************************
 * This is Number Game Original Class for Game 1024
 *
 * @author Zarrukh Bazarov
 * @version January 2022
 *
 *******************************************************************/
public class NumberGameOriginal implements NumberSlider {
    /** Instance variable New board for using in this class */
    private int[][] newBoard;

    /** Instance variable for Stack object that holds an arraylist of cells */
    Stack<ArrayList<Cell>> prevBoard;

    /** Instance variable for the desired winning value */
    private int winningValue;

    /******************************************************************
     * Reset the game logic to handle a board of a given dimension
     *
     * @param height
     *            the number of rows in the board
     * @param width
     *            the number of columns in the board
     * @param winningValue
     *            the value that must appear on the board to win the game
     * @throws IllegalArgumentException
     *             when the winning value is not power of two or negative
     *******************************************************************/
    public void resizeBoard(int height, int width, int winningValue) {
        this.winningValue = winningValue;

        // Instantiating the prevBoard as Stack object
        prevBoard = new Stack<ArrayList<Cell>>();
        if (winningValue < 0)
            throw new IllegalArgumentException("Negative Winning Value");
        if (powerOfTwo(winningValue) == false)
            throw new IllegalArgumentException("Winning value is Not power of two");
        this.newBoard = new int[height][width];
    }

    /******************************************************************
     * Special method for using in resizeBoard() method, which returns true if
     * the parameter is the power of two
     *
     * @param n
     *            the value which we want to test if it's power of two or not
     * @return true if parameter n is the power of two, and false if not
     *****************************************************************/
    private static boolean powerOfTwo(int n) {
        // integer number for checking square
        int square = 1;

        // while loop for checking if parameter n is the power of two
        while (n >= square) {
            if (n == square)
                return true;
            square = square * 2;
        }
        return false;
    }

    /******************************************************************
     * Remove all numbered tiles from the board and place TWO non-zero values at
     * random location
     ******************************************************************/
    public void reset() {
        // Removing all numbered tiles from the board
        for (int i = 0; i < newBoard.length; i++) {
            for (int j = 0; j < newBoard[i].length; j++) {
                newBoard[i][j] = 0;
            }
        }
        // Placing two non zero numbers at random location
        placeRandomValue();
        placeRandomValue();

    }

    /*******************************************************************
     * Set the game board to the desired values given in the 2D array. This
     * method should use nested loops to copy each element from the provided
     * array to your own internal array. Do not just assign the entire array
     * object to your internal array object. Otherwise, your internal array may
     * get corrupted by the array used in the JUnit test file.This method is
     * mainly used by the JUnit tester.
     *
     * @param ref
     *            is an Array which we want to setValue
     ******************************************************************/
    public void setValues(final int[][] ref) {
        for (int i = 0; i < newBoard.length; i++) {
            for (int j = 0; j < newBoard[i].length; j++) {
                // going through the whole initial array and storing ref
                this.newBoard[i][j] = ref[i][j];
            }
        }
    }

    /*******************************************************************
     * Insert one random tile into an empty spot on the board.
     *
     * @return a Cell object with its row, column, and value attributes
     *         initialized properly
     *
     * @throws IllegalStateException
     *             when the board has no empty cell
     ******************************************************************/
    public Cell placeRandomValue() {

        Cell c = new Cell();
        boolean flag = false;
        for (int i = 0; i < newBoard.length; i++)
            for (int j = 0; j < newBoard[i].length; j++)
                if (this.newBoard[i][j] == 0)
                    flag = true;
        if (flag == false)
            throw new IllegalStateException();

        // loop until I find empty spot and place 2 there then create a cell and
        // return cell
        if (flag) {
            int x;
            int y;
            do {
                x = (int) (Math.random() * newBoard.length);
                y = (int) (Math.random() * newBoard[0].length);
            } while (newBoard[x][y] != 0);

            // Assigning 2 or 4 to randomly selected spot of newBoard
            newBoard[x][y] = (Math.random()) < 0.5 ? 2 : 4;
            c.row = x;
            c.column = y;
            c.value = newBoard[c.row][c.column];
        }

        return c;
    }

    /*******************************************************************
     * Slide all the tiles in the board in the requested direction
     *
     * @param dir
     *            move direction of the tiles
     *
     * @return false when the board is the same
     ******************************************************************/
    public boolean slide(SlideDirection dir) {

        boolean theSame = true;
        // assigning Stack object with the current (initial) values of the board
        this.prevBoard.push(getNonEmptyTiles());

        switch (dir) {
            case LEFT:
                for (int i = 0; i < newBoard.length; i++) {
                    // creating new array for comparing then if the board has
                    // changed
                    int[] e = newBoard[i];

                    // by merging I move the slide direction to the left
                    newBoard[i] = merge(newBoard[i]);

                    // checking if the initial array and new one has changed
                    if (!Arrays.equals(e, newBoard[i]))
                        theSame = false;
                }
                break;
            case UP:
                for (int i = 0; i < newBoard[0].length; i++) {
                    // creating new array for comparing then if the board has
                    // changed
                    int[] r = getColumns(i);

                    // creating new array for making move to up and then
                    // merging and getting back
                    int[] column1 = getColumns(i);
                    column1 = merge(column1);
                    getBackColumns(column1, i);

                    // checking if the initial array and new one has changed
                    if (!Arrays.equals(r, column1))
                        theSame = false;
                }
                break;
            case RIGHT:
                for (int i = 0; i < newBoard.length; i++) {
                    // creating new array for comparing then if the board has
                    // changed
                    int[] t = newBoard[i];

                    // creating new array for making move to the right and then
                    // merging and getting back
                    int[] w = reverse(newBoard[i]);
                    w = merge(w);
                    newBoard[i] = reverse(w);

                    // checking if the initial array and new one has changed
                    if (!Arrays.equals(t, newBoard[i]))
                        theSame = false;
                }
                break;
            case DOWN:
                for (int i = 0; i < newBoard[0].length; i++) {
                    // creating new array for comparing then if the board has
                    // changed
                    int[] y = getColumns(i);

                    // creating new array for making move to the right and then
                    // merging and then reversing and then getting back
                    int[] column1 = getColumns(i);
                    column1 = reverse(column1);
                    column1 = merge(column1);
                    column1 = reverse(column1);
                    getBackColumns(column1, i);

                    // checking if the initial array and new one has changed
                    if (!Arrays.equals(y, column1))
                        theSame = false;
                }
                break;
        }
        // checking if the board it's changed and then placing random numbers
        if (!theSame)
            placeRandomValue();
        return !theSame;
    }

    /*******************************************************************
     * The special private helper method for getting the array of the requested
     * column
     *
     * @param a
     *            is a requested column index
     * @return newColumn with the values of the requested column
     ******************************************************************/
    private int[] getColumns(int a) {
        int[] newColumn = new int[newBoard.length];
        for (int i = 0; i < newBoard.length; i++)
            newColumn[i] = newBoard[i][a];
        return newColumn;
    }

    /*******************************************************************
     * The special private helper method which is getting back the column to the
     * initial newBoard
     *
     * @param n
     *            is requested array which we want to get back to the newBoard
     * @param c
     *            is a requested index of column
     ******************************************************************/
    private void getBackColumns(int[] n, int c) {

        for (int i = 0; i < newBoard.length; i++)
            newBoard[i][c] = n[i];
    }

    /*******************************************************************
     * The special helper method for reversing values of arrays and returning
     * new array with reversed values
     *
     * @param n
     *            is a requested array which we want to reverse
     * @return q new array with reversed values(value at index = 0 would go to
     *         the end)
     ******************************************************************/
    private int[] reverse(int[] n) {
        int[] q = new int[n.length];
        for (int i = 0; i < n.length; i++)
            q[i] = n[n.length - 1 - i];
        return q;
    }

    /*****************************************************************
     * This is special helper method for merging two values of the array if they
     * are the same, so if two arrays in the row are both 2, it will be merged
     * to one 4 value
     *
     * @param getCell
     *            the requested array which we want to merge
     * @return returning new cell which was merged before
     ******************************************************************/
    private int[] merge(int[] getCell) {
        /* new array with the same size as parameter */
        int[] newCell = new int[getCell.length];
        /* int x is for index number of rows */
        int x = 0;
        /* int y is for index number of next row to x */
        int y = 0;
        /* int z is for index number for the new array */
        int z = 0;

        // making sure it goes through the whole row size
        while (x < getCell.length) {
            // checking for value in the array which is not 0
            while (x < getCell.length && getCell[x] == 0)
                x++;
            // y is the index of the array which is next to x
            y = x + 1;

            // checking for non zero value in the array next to x
            while (y < getCell.length && getCell[y] == 0)
                y++;
            // checking if it goes untill the end
            if (y < getCell.length) {

                /*
                 * if the two values are the same then store new doubled (summed
                 * to each other)cell with index z
                 */
                if (getCell[x] == getCell[y]) {
                    newCell[z] = getCell[x] * 2;
                    z++;
                    x = y + 1;
                }
                // if the two values are not the same the just store
                // value of x to value at index z
                else {
                    newCell[z] = getCell[x];
                    z++;
                    x = y;
                }

            }
            // if the y overrides(several zero values at the)
            // then we need to just store the value at index x to the new index
            // z
            else {
                if (x < getCell.length) {
                    newCell[z] = getCell[x];
                    break;
                }
            }

        }
        return newCell;
    }

    /*******************************************************************
     * The method is checking and storing non zero values of array to the array
     * list of cells
     *
     * @return an Arraylist of Cells. Each cell holds the (row,column) and value
     *         of a tile
     ******************************************************************/
    public ArrayList<Cell> getNonEmptyTiles() {

        ArrayList<Cell> cells = new ArrayList<Cell>();

        // SEARCH new board looking for non zero (with value)
        // save it to arraylist
        for (int i = 0; i < newBoard.length; i++) {
            for (int j = 0; j < newBoard[i].length; j++) {
                if (newBoard[i][j] != 0) {
                    Cell a = new Cell();
                    a.row = i;
                    a.column = j;
                    a.value = newBoard[i][j];
                    cells.add(a);

                }
            }
        }
        return cells;
    }

    /*******************************************************************
     * Return the current state of the game (user_won, in_progress, or
     * user_lost)
     *
     * @return one of the possible values of GameStatus enum
     ******************************************************************/
    public GameStatus getStatus() {
        // user won if the winning value is on the board
        if (winningVal())
            return GameStatus.USER_WON;

        // if user has moves to do, then he is in progress of the game
        if (canMove())
            return GameStatus.IN_PROGRESS;

            // otherwise user lost the game
        else {
            return GameStatus.USER_LOST;
        }
    }

    /*******************************************************************
     * The special helper method used in GameStatus method for finding if the
     * Board has desired winning value we wanted
     *
     * @return true if the board has the desired value of winning
     ******************************************************************/
    private boolean winningVal() {
        for (int i = 0; i < newBoard.length; i++)
            for (int j = 0; j < newBoard[i].length; j++)
                if (newBoard[i][j] >= winningValue)
                    return true;
        return false;
    }

    /********************************************************************
     * The special helper method used for checking if there is still possible
     * moves in the board
     *
     * @return true if there is still possible moves
     *******************************************************************/
    private boolean canMove() {
        // checking if there is still zero spot on the board
        for (int i = 0; i < newBoard.length; i++)
            for (int j = 0; j < newBoard[i].length; j++)
                if (newBoard[i][j] == 0)
                    return true;

        // checking for possible moves in the row
        for (int i = 0; i < newBoard.length - 1; i++)
            for (int j = 0; j < newBoard[i].length; j++)
                if (newBoard[i][j] == newBoard[i + 1][j])
                    return true;

        // checking for possible moves in the column
        for (int i = 0; i < newBoard.length; i++)
            for (int j = 0; j < newBoard[i].length - 1; j++)
                if (newBoard[i][j] == newBoard[i][j + 1])
                    return true;
        return false;
    }

    /*******************************************************************
     * Undo the most recent action, i.e. restore the board to its previous
     * state. Calling this method multiple times will ultimately restore the
     * game to the very first initial state of the board holding two random
     * values. Further attempt to undo beyond this state will throw an
     * IllegalStateException.
     *
     * @throws IllegalStateException
     *             when undo is not possible
     ******************************************************************/
    public void undo() {
        // checking if undo would go untill the end of the started point
        if (prevBoard.isEmpty())
            throw new IllegalStateException();

        // setting the whole array to zero before popping
        for (int i = 0; i < newBoard.length; i++)
            for (int j = 0; j < newBoard[i].length; j++)
                newBoard[i][j] = 0;
        // using 'pop' method to get the most recent board every time
        ArrayList<Cell> previous = prevBoard.pop();
        for (Cell c : previous) {
            newBoard[c.row][c.column] = c.value;
        }

    }

}
