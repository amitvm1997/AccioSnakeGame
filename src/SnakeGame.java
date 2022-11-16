import javax.swing.*;
//
public class SnakeGame {
  JFrame frame; // initialize frame
  SnakeGame(){
        frame=new JFrame(); // create new frame
//
       Board board=new Board(); // new board gets created and board function runs in background
//
        frame.add(board); // added board to frame
        frame.setVisible(true); // set visibility
        frame.setTitle("Snake Game"); // Given title to the frame
        frame.setResizable(false); //set whether the frame is resizable by the user or not "false"
        frame.pack(); // it is used to automatically fit to the preferred size of the window
    }
    public static void main(String[] args)
    {
        SnakeGame snakeGame=new SnakeGame(); // Snakegame is started
    }
}