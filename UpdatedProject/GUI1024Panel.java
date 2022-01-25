package UpdatedProject;

import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;

/***********************************************************************
 * GUI part of the game 1024
 *
 * @author Zarrukh Bazarov
 *
 * @version 2.0 (January 2022)
 *
 **********************************************************************/
public class GUI1024Panel extends JFrame implements KeyListener, ActionListener {

    /** JLabel for the board of the game */
    private JLabel[][] myBoard;

    /** JPanel for the main panel of the game */
    private JPanel mainPanel;

    /** JLabel for the score of the game */
    private JLabel gameScore = new JLabel("Score:");

    /** JPanel for the resetDialog of the game */
    JPanel resetDialog = new JPanel(new GridLayout(3,2));

    /** JMenuBar for the menu of the game */
    private JMenuBar menus;

    /** JMenu for the file (menu) of the game */
    private JMenu fileMenu;

    /** JMenuItem for the reset item (in menu) of the game */
    private JMenuItem resetItem;

    /** JMenuItem for the quit item (in menu) of the game */
    private JMenuItem quitItem;


    /** JTextField for height when picking the size of the game
     * in the beginning of the game */
    private JTextField heightOfBoard = new JTextField("4", 3);

    /** JTextField for width when picking the size of the game
     * in the beginning of the game */
    private JTextField widthOfBoard = new JTextField("4", 3);

    /** JTextField for width when picking the size of the game
     * in the beginning of the game */
    private JTextField winningValueBox = new JTextField("2048", 3);


    /** Font for using in the game */
    Font font = new Font("Time", Font.PLAIN, 30);

    /** Number game for the real game */
    private NumberGameArrayList myGame;


    /******************************************************************
     * Constructor for the main game
     *****************************************************************/
    public GUI1024Panel(){
        myGame = new NumberGameArrayList();
        resetDialog();
    }


    /******************************************************************
     * This constructor makes all the GUI
     *
     * @param height for the determining height of the game
     * @param width for the determining for width of the game
     * @param winningValue for the setting vinning value for the game
     *****************************************************************/
    public void GUI (int height, int width, int winningValue){

        this.setTitle("Welcome to 2048 Game");
        this.setSize(1024,800);
        myGame.resizeBoard(height, width, winningValue);
        myGame.reset();
        addKeyListener(this);
        gameScore.setFont(font);

        mainPanel = new JPanel(new FlowLayout());

        /** JPanel for the grids of the game */
        JPanel gridPanel = new JPanel(new GridLayout(height,width));

        /**Border between grids for the game */
        Border borderOfGrid = BorderFactory.createLineBorder(Color.GRAY, 12);

        /** Creating grid(my board) for the game */
        myBoard = new JLabel[height][width];
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                myBoard[i][j] = new JLabel("", JLabel.CENTER);
                myBoard[i][j].setOpaque(true);
                myBoard[i][j].setBackground(Color.YELLOW);
                myBoard[i][j].setBorder(borderOfGrid);
                myBoard[i][j].setFont(font);
                myBoard[i][j].setPreferredSize(new Dimension(100,100));
                gridPanel.add(myBoard[i][j]);
            }
        }

        /** JPanel for the score of the game */
        JPanel scorePanel = new JPanel(new GridLayout());
        scorePanel.add(gameScore);

        //Menu creation
        fileMenu = new JMenu("File");
        resetItem = new JMenuItem("Reset");
        quitItem = new JMenuItem("Quit");

        //adding the menus
        fileMenu.add(resetItem);
        fileMenu.add(quitItem);
        menus = new JMenuBar();

        menus.add(fileMenu);
        setJMenuBar(menus);
        quitItem.addActionListener(this);
        resetItem.addActionListener(this);
        fileMenu.addActionListener(this);
        clearBoard();

        //setting grids ()
        gridPanel.setBackground(Color.black);
        gridPanel.setSize(1280,730);
        gridPanel.setVisible(true);

        mainPanel.add(gridPanel);
        mainPanel.add(scorePanel);
        this.setVisible(true);
        this.add(mainPanel);
    }


    /******************************************************************
     * Private helper method for clearing the board
     *
     *****************************************************************/
    private void clearBoard(){
        //Clears board
        for(int i = 0; i < myBoard.length; i++){
            for(int j = 0; j < myBoard[0].length; j++){
                myBoard[i][j].setText("");
            }
        }

        ArrayList<Cell> Cells = myGame.getNonEmptyTiles();
        for(Cell c: Cells){
            myBoard[c.row][c.column].setText("" + c.value);
        }
    }

    /******************************************************************
     * The method that updates the score of the game.
     *****************************************************************/
    public void updateScore(){
        gameScore.setText("Score: " + myGame.getScore());
    }


    /******************************************************************
     * KeyEvent class for determining what happens if particular keys
     * pressed
     *
     * @param e for using as a parameter for determining
     * the key pressed
     *
     *****************************************************************/
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyChar() == 'u' || e.getKeyChar() == 'U'){
            myGame.undo();
            clearBoard();
            updateScore();
        }
        if(e.getKeyChar() == 'a' || e.getKeyChar() == 'A'){
            myGame.slide(SlideDirection.LEFT);
            clearBoard();
            updateScore();
        }
        if(e.getKeyChar() == 'd' || e.getKeyChar() == 'D'){
            myGame.slide(SlideDirection.RIGHT);
            clearBoard();
            updateScore();
        }
        if(e.getKeyChar() == 'w' || e.getKeyChar() == 'W'){
            myGame.slide(SlideDirection.UP);
            clearBoard();
            updateScore();
        }
        if(e.getKeyChar() == 's' || e.getKeyChar() == 'S'){
            myGame.slide(SlideDirection.DOWN);
            clearBoard();
            updateScore();
        }

    }

    /******************************************************************
     * ActionEvent class for determining what happens if particular keys
     * (from menu) pressed
     *
     * @param  e for using as a parameter for determining
     * the key (from menu) pressed
     *
     *****************************************************************/
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == quitItem){
            System.exit(1);
        }
        if(e.getSource() == resetItem){
            resetDialog();
            updateScore();
        }

    }

    /******************************************************************
     * Method for resetting the dialog for the beginning of the game
     *****************************************************************/
    public void resetDialog(){

        resetDialog.add(new JLabel("Set Height: "));
        resetDialog.add(heightOfBoard);
        resetDialog.add(new JLabel("Set Width: "));
        resetDialog.add(widthOfBoard);
        resetDialog.add(new JLabel("Set Winning Value: "));
        resetDialog.add(winningValueBox);

        int height = 0, width = 0, winningValue = 0;
        try{
            int i = JOptionPane.showConfirmDialog(null, resetDialog, "Enter Board Dimesions and Winning Value", JOptionPane.OK_CANCEL_OPTION);
            if(i == JOptionPane.OK_OPTION){
                height = Integer.parseInt(heightOfBoard.getText());
                width = Integer.parseInt(widthOfBoard.getText());
                winningValue = Integer.parseInt(winningValueBox.getText());
                GUI(height, width, winningValue);
                clearBoard();
            }
        }
        catch(Exception ex){}

    }


    //Unused
    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    //Unused
    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub

    }

}
