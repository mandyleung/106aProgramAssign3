import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.util.Random;
import java.awt.Graphics2D;
import java.io.*;
import java.util.Arrays;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Ellipse2D;
import java.lang.*;
import java.awt.geom.Point2D;

class Main {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        f.breakout();
      }
    });
    new Thread(new Runnable() {
      public void run() {
        f.play();
      }
    }).start();
  };
  private static Breakout f = new Breakout();
}

class Breakout extends JFrame {
  public void breakout() {
    JFrame f = new JFrame("Breakout game");
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.add(d);
    f.pack();
    f.setVisible(true);
  }
  public void play(){
    d.play();
  }
  private DrawPanel d = new DrawPanel();
}

class DrawPanel extends JPanel {

  public DrawPanel() {
    init();

    addMouseMotionListener(new MouseMotionAdapter(){
      public void mouseMoved(MouseEvent m){
        int xPaddle = Math.min(Math.max(m.getX() - (PADDLE_WIDTH / 2), 0), WIDTH - PADDLE_WIDTH);
        paddle.setRect(xPaddle, paddle.getY(), PADDLE_WIDTH, PADDLE_HEIGHT);
        repaint();
      }
    });
  }


  private void init() {
    /** Initialize location, color and visibility of bricks at the beginning of a game */
    int xPos, yPos;
    xPos = yPos = 0;
    for (int i = 0; i < NBRICKS_PER_ROW; i++) {
      for (int j = 0; j < NBRICK_ROWS; j++) {
        xPos =  (BRICK_SEP + BRICK_WIDTH) * i;
        yPos = BRICK_Y_OFFSET + ((BRICK_SEP + BRICK_HEIGHT) * j);
        bricks[i][j] = new Rectangle2D.Double(xPos, yPos, BRICK_WIDTH, BRICK_HEIGHT);
        visi[i][j] = true; 
      }
    }

    /** Initialize paddle */
    int xPaddle = (WIDTH / 2) - (PADDLE_WIDTH / 2);
    int yPaddle = HEIGHT - BRICK_Y_OFFSET;
    paddle = new Rectangle2D.Double(xPaddle, yPaddle, PADDLE_WIDTH, PADDLE_HEIGHT);

  }

  public void play() {
    Boolean roundEnd = false;
    Boolean gameWon = false;

    /** Intialize ball location and velocity. Move ball. Check if ball hit screen edges and change velocity accordingly. End round if ball hits bottom edge. **/
    for (int i = 0; i < NTURNS; i++) {
      roundEnd = false;
      initBall();
      repaint();
      while (!roundEnd) {
        /** move ball **/
        updateBall(xBall + xVelBall, yBall + yVelBall);

        /** check if ball is hitting game edges, paddle or bricks and update game elements accordingly **/
        roundEnd = checkBounds();
        hitPaddle();
        gameWon = hitBrick();
        repaint();

        if (gameWon) break;

        /** pause **/
        try {
          // thread to sleep for 100 milliseconds
          Thread.sleep(10);
         } catch (Exception e) {
          System.out.println(e);
         }
      }
      if (gameWon) break;
    }

    System.out.println(gameWon);
  }

  private void updateBall(double x, double y) {
    /** update ball location **/
    xBall = x;
    yBall = y;
    ball.setFrame(x, y, BALL_RADIUS * 2, BALL_RADIUS * 2);
    /** update 4 corners of ball **/
    p1.setLocation(xBall, yBall);
    p2.setLocation(xBall + BALL_RADIUS * 2, yBall);
    p3.setLocation(xBall, yBall + BALL_RADIUS * 2);
    p4.setLocation(xBall + BALL_RADIUS * 2, yBall + BALL_RADIUS * 2);
  }

  private void initBall(){
    /** Set ball to the middle of the screen to start a round **/
    updateBall(WIDTH / 2 - BALL_RADIUS, HEIGHT / 2 - BALL_RADIUS);

    /** Initialize velocity/direction of ball **/
    xVelBall = rand.nextDouble() * 2 + 1;
    if (rand.nextBoolean()) xVelBall = -xVelBall; //set initial x velocity to (-3 to -1) or (1 to 3)
    yVelBall = 3;
  }

  private Boolean checkBounds() {
    if (checkLeftBound()) {
      xVelBall = -xVelBall;
    } else if (checkRightBound()) {
      xVelBall = -xVelBall;
    } else if (checkTopBound()) {
      yVelBall = -yVelBall;
    } else if (checkBottomBound()) {
      yVelBall = -yVelBall;
      return true;
    }
    return false;
  }

  private Boolean checkLeftBound(){
    return xBall <= 0 ? true : false;
  }

  private Boolean checkRightBound(){
    return (xBall + BALL_RADIUS * 2) >= WIDTH ? true : false;
  }

  private Boolean checkTopBound(){
    return yBall <= 0 ? true : false;
  }

  private Boolean checkBottomBound(){
    return (yBall + BALL_RADIUS * 2) >= HEIGHT ? true : false;
  }

  private void hitPaddle() {
    /** check only the bottom of the ball against top of the paddle **/
    Rectangle2D paddleTop = new Rectangle2D.Double(paddle.getX(), paddle.getY(), paddle.getWidth(), Math.abs(yVelBall));
    if (paddleTop.contains(p3) || paddleTop.contains(p4)) {
      yVelBall = -yVelBall;
    }
  }

  private Boolean hitBrick() {
    System.out.println("x = " + xBall + " y = " + yBall + " xVel = " + (int) xVelBall + " yVel = " + (int) yVelBall);
    for (int i = 0; i < NBRICKS_PER_ROW; i++) {
      for (int j = 0; j < NBRICK_ROWS; j++) {
        if (visi[i][j] && (bricks[i][j].contains(p1) || bricks[i][j].contains(p2) || bricks[i][j].contains(p3) || bricks[i][j].contains(p4))){
          visi[i][j] = false;
          bricksLeft --;
          yVelBall = -yVelBall;
          System.out.println("brick removed " + i + ", " + j + " loc = " + bricks[i][j].getX() + ", " + bricks[i][j].getY()+ ", " + BRICK_WIDTH + ", " + BRICK_HEIGHT);
          /** break both loops so that only 1 brick can be removed at a time **/
          return bricksLeft = 0 ? true : false;
        }
      }
    }
    return false;
  }


  public Dimension getPreferredSize() {
    return new Dimension(WIDTH,HEIGHT);
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);  
    Graphics2D g2d = (Graphics2D) g;
    /** Paint Bricks **/
    for (int i = 0; i < NBRICKS_PER_ROW; i++) {
      for (int j = 0; j < NBRICK_ROWS; j++) {
        if (visi[i][j]) {
          g2d.setColor(rowColor[j]);
          g2d.fill(bricks[i][j]);
        }
      }
    }
    /** Paint Paddle **/
    g2d.setColor(Color.BLACK);
    g2d.fill(paddle);

    /** Paint Ball **/
    g2d.setColor(Color.BLACK);
    g2d.fill(ball);
  }  

  /** Width and height of application window in pixels */
  public static final int APPLICATION_WIDTH = 400;
  public static final int APPLICATION_HEIGHT = 600;
  /** Dimensions of the paddle */
  private static final int PADDLE_WIDTH = 60;
  private static final int PADDLE_HEIGHT = 10;
  /** Offset of the paddle up from the bottom */
  private static final int PADDLE_Y_OFFSET = 30;
  /** Number of bricks per row */
  private static final int NBRICKS_PER_ROW = 10;
  /** Number of rows of bricks */
  private static final int NBRICK_ROWS = 10;
  /** Separation between bricks */
  private static final int BRICK_SEP = 4;
  /** Width of a brick */
  private static final int BRICK_WIDTH =
  (APPLICATION_WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;
  /** Height of a brick */
  private static final int BRICK_HEIGHT = 8;
  /** Radius of the ball in pixels */
  private static final int BALL_RADIUS = 10;
  /** Offset of the top brick row from the top */
  private static final int BRICK_Y_OFFSET = 70;
  /** Dimensions of game board (usually the same) */
  private static final int WIDTH = BRICK_WIDTH * NBRICKS_PER_ROW + BRICK_SEP * (NBRICKS_PER_ROW - 1);
  private static final int HEIGHT = APPLICATION_HEIGHT;
  /** Number of turns */
  private static final int NTURNS = 3;
  /** Array of bricks **/
  private Rectangle2D[][] bricks = new Rectangle2D[NBRICKS_PER_ROW][NBRICK_ROWS];
  /** Num of bricks still on the screen **/
  int bricksLeft = NBRICK_ROWS * NBRICK_ROWS;
  /** Color of bricks by row **/
  private Color[] rowColor = new Color[]{Color.RED, Color.RED, Color.ORANGE, Color.ORANGE, Color.YELLOW, Color.YELLOW, Color.GREEN, Color.GREEN, Color.CYAN, Color.CYAN};
  /** Visibility of array of bricks **/
  private Boolean[][] visi = new Boolean[NBRICKS_PER_ROW][NBRICK_ROWS];
  /** Paddle object **/
  private Rectangle2D paddle;
  /** Ball object, velocity and location **/
  private double xBall = 0;
  private double yBall = 0;
  private double xVelBall = 0;
  private double yVelBall = 0;
  private Ellipse2D ball = new Ellipse2D.Double(0, 0, 0, 0);
  /** 4 corners of ball **/
  Point2D p1 = new Point2D.Double(0, 0);
  Point2D p2 = new Point2D.Double(0, 0);
  Point2D p3 = new Point2D.Double(0, 0);
  Point2D p4 = new Point2D.Double(0, 0);
  /** Random object **/
  private Random rand = new Random();
}
