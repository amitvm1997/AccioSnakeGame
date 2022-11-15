import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


// Creating the Board

public class Board extends JPanel implements ActionListener{ //behaves like Jpanel and get inherted so that we can override the method
    int height = 400; // initialize the height and width of the board so that the snake should not go out of the board
    int width = 400;

    int dot; // initialize the dots of the snake
    int dotSize = 10; // initialized every dot size
    int allDots = 40 * 40; // snakes can have maximum size of 40*40 (rows and columns)
    int DELAY = 150; // initialized the delay in the timer in mili seconds

    int x[] = new int[allDots]; // this array will contain the (x,y) of all the dots
    int y[] = new int[allDots];

    int apple_x;     // this will store apple's location
    int apple_y;

    boolean leftDirection = true;           // this will contain the direction of the snake going at starting
    boolean rightDirection = false;   //  for snake moving in left direction remaining must be false
    boolean upDirection = false;
    boolean downDirection = false;

    boolean inGame=true; // to check whether in the game or not


    Timer timer;   // this will contain the time ever second snake moves or gets collided


    Image head; // this will keep images of head, body and apple in resources folder
    Image apple;
    Image body;

    //Initializing the panel attributes
    Board(){                                //board constructor
        addKeyListener(new TAdapter());  // calls keyadapter function to check which key is pressed
        setBackground(Color.BLACK); // background of the board
        setFocusable(true);  // it is used to set focus to listen the key and help to check which key is pressed  in keyadapter
        setPreferredSize(new Dimension(height,width)); //size of the board
        loadImages(); // to get the image of snake head, apple and normal dots
        initGame(); // initialized the game

    }

    //Initialize the game
    public void initGame(){                 // to start the game
        locateApple();
        dot=3; //initial dots snake will contain
        for(int z=0;z<dot;z++){ //for every dot there will be x and y coordinate
            y[z]=350;    // initially dots will be at y=350 and snake moves to left with sae y axis
            x[z]=350+(z*dotSize); // x coordinate varies if snake keep moving in left direct with change of (y+(z*dot size i.e. 10))
        }
        timer=new Timer(DELAY,this); // new timer as it will have some delay from the starting point
        timer.start();      // timer starts


    }
    public void loadImages(){    // function to load the images
        ImageIcon a = new ImageIcon("src/resources/apple.png");  // image for apple
        apple = a.getImage();
        ImageIcon b = new ImageIcon("src/resources/head.png");   // image for head of snake
        head = b.getImage();
        ImageIcon c = new ImageIcon("src/resources/dot.png");    // image for body
        body = c.getImage();
    }
    @Override                                   // we override the action performed in action listener class
    public void paintComponent(Graphics g){  // to paint something in graphics
        super.paintComponent(g);             // keyword to call the paint component

        doDrawings(g);          // calling drawing function using g graphics
    }
    public void doDrawings(Graphics g){     // to draw the images on the board
        if(inGame){
            g.drawImage(apple,apple_x,apple_y,this); // draw apple with x and y coordinate
            g.drawImage(head,x[0],y[0],this);  // draw head with x and y coordinate
            for(int z =1;z<dot;z++){
                g.drawImage(body,x[z],y[z],this);  // draw body with x and y coordinate for remaining dots
            }
            Toolkit.getDefaultToolkit().sync(); //syncing the timer with the coordinates of snake
        }
        else{
            gameOver(g);
        }
    }
    public void checkCollision(){  // to check collison of snake or touch the boundary of the board
        for(int z=0;z<dot;z++){
            if((z>3)&&(x[0]==x[z])&&(y[0]==y[z])){  // z>3 because we need more than 3 dots to gets collided with itself
                inGame=false;  // as soon as it collides in game gets false which was true initially and we are out of the game
            }
        }

        if(x[0]<0){              // these  conditions is to check if snake collides with the boundry of board
            inGame=false;
        }
        if(x[0]>=width){        // these  conditions is to check if snake collides with the boundry of board
            inGame=false;
        }
        if(y[0]<0){             // these  conditions is to check if snake collides with the boundry of board
            inGame=false;
        }
        if(y[0]>=height){       // these  conditions is to check if snake collides with the boundry of board
            inGame=false;
        }
        if(!inGame){
            timer.stop();
        }
    }
    public void gameOver(Graphics g){  // method to pop up message "game over" if collison happens
        String msg="GAME OVER";
        Font font=new Font("Helvetica",Font.BOLD,20);
        FontMetrics fontMetrics=getFontMetrics(font);

        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawString(msg,(width-fontMetrics.stringWidth(msg))/2,height/2);
    }
    public void checkApple(){  // function checks if head is collided with apple or not
        if(x[0]==apple_x&&y[0]==apple_y){
            dot++; // if collison happens a dot is added on the snake
            locateApple(); //randomize the apple location again
        }
    }
    public void locateApple(){       // to randomize the apple position every time
        int x= (int)(Math.random()*39); //randomize location of apple in x axis within board bounds
        apple_x=x*10;
        int y= (int)(Math.random()*39);   //randomize location of apple in y axis within board bounds
        apple_y=y*10;   // *10 because snake will always be in multiple of 10 as dot size is 10, so for collison
    }
    public void move(){
        for(int z=dot-1;z>0;z--){  //start from last-1 as head is also counted
            x[z]=x[z-1];   // as last dot is moving to left so original position minus will be new position
            y[z]=y[z-1];   // as last dot is moving to left so original position minus will be new position
        }
        if(leftDirection){  // snake is moving in left direction so it will  have the x axis minus from original position
            x[0]-=dotSize;
        }
        if(rightDirection){ // snake is moving in right direction so it will  have the x axis plus from original position
            x[0]+=dotSize;
        }
        if(upDirection){  // snake is moving in up direction so it will  have the y axis minus from original position
            y[0]-=dotSize;
        }
        if(downDirection){  // snake is moving in down direction so it will  have the y axis minus from original position
            y[0]+=dotSize;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            move(); //move function is called
            checkApple();
            checkCollision();
        }
        repaint(); // for time syncing method
    }

    public class TAdapter extends KeyAdapter{  // class for checking which ke is pressed
        @Override
        public void keyPressed(KeyEvent e){
            int key= e.getKeyCode(); // according to this it depends what happens next

            if(key==KeyEvent.VK_LEFT && !rightDirection){  // key pressed is left and direction of snake is not right
                leftDirection=true;
                upDirection=false;
                downDirection=false;
            }
            if(key==KeyEvent.VK_RIGHT && !leftDirection){  // key pressed is right and direction of snake is not left
                rightDirection=true;
                upDirection=false;
                downDirection=false;
            }
            if(key==KeyEvent.VK_UP && !downDirection){   // key pressed is up and direction of snake is not down
                upDirection=true;
                leftDirection=false;
                rightDirection=false;
            }
            if(key==KeyEvent.VK_DOWN && !upDirection){   // key pressed is down and direction of snake is not up
                downDirection=true;
                rightDirection=false;
                leftDirection=false;
            }
        }
    }
}