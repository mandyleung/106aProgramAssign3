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

    /** Intialize ball location and velocity. Move ball. Check if ball hit screen edges and change velocity accordingly. End round if ball hits bottom edge. **/
    for (int i = 0; i < NTURNS; i++) {
      roundEnd = false;
      initBall();
      repaint();
      while (!roundEnd) {
        /** move ball **/
        updateBall(xBall + (int) xVelBall, yBall + (int) yVelBall);

        /** check if ball is hitting game edges, paddle or bricks and update game elements accordingly **/
        checkBounds();
        // hitPaddle();
        // hitBrick();
        
        repaint();
        /** pause **/
        try {
          // thread to sleep for 100 milliseconds
          Thread.sleep(10);
         } catch (Exception e) {
          System.out.println(e);
         }
         System.out.println("loop");
      }
    }
  }

  private void updateBall(int x, int y) {
    /** update ball location **/
    xBall = x;
    yBall = y;
    ball.setFrame(x, y, BALL_RADIUS, BALL_RADIUS);
    /** update 4 corners of ball **/
    p1.setLocation(xBall, yBall);
    p2.setLocation(xBall + BALL_RADIUS, yBall);
    p3.setLocation(xBall, yBall + BALL_RADIUS);
    p4.setLocation(xBall + BALL_RADIUS, yBall + BALL_RADIUS);
  }

  private void initBall(){
    /** Set ball to the middle of the screen to start a round **/
    updateBall(WIDTH / 2 - BALL_RADIUS, HEIGHT / 2 - BALL_RADIUS);

    /** Initialize velocity/direction of ball **/
    xVelBall = rand.nextDouble() * 2 + 1;
    if (rand.nextBoolean()) xVelBall = -xVelBall; //set initial x velocity to (-3 to -1) or (1 to 3)
    yVelBall = 3;
  }

  private void checkBounds() {
    if (checkLeftBound()) {
      xBall = -xBall;
      xVelBall = -xVelBall;
    } else if (checkRightBound()) {
      xBall = WIDTH - (xBall - WIDTH);
      xVelBall = -xVelBall;
    } else if (checkTopBound()) {
      yBall = -yBall;
      yVelBall = -yVelBall;
    } else if (checkBottomBound()) {
      yBall = HEIGHT - (yBall - HEIGHT);
      yVelBall = -yVelBall;
    }
  }

  private Boolean checkLeftBound(){
    return xBall <= 0 ? true : false;
  }

  private Boolean checkRightBound(){
    return xBall >= WIDTH ? true : false;
  }

  private Boolean checkTopBound(){
    return yBall <= 0 ? true : false;
  }

  private Boolean checkBottomBound(){
    return yBall >= HEIGHT ? true : false;
  }

  private void hitPaddle() {

    if (paddle.contains(p1) || paddle.contains(p2) || paddle.contains(p3) || paddle.contains(p4)) {
      yVelBall = -yVelBall;
    }
  }

  private void hitBrick() {

    for (int i = 0; i < NBRICKS_PER_ROW; i++) {
      for (int j = 0; j < NBRICK_ROWS; j++) {
        if (visi[i][j] && (bricks[i][j].contains(p1) || bricks[i][j].contains(p2) || bricks[i][j].contains(p3) || bricks[i][j].contains(p4))){
          visi[i][j] = false;
          yVelBall = -yVelBall;
        }
      }
    }

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
        g2d.setColor(rowColor[j]);
        g2d.fill(bricks[i][j]);
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
  /** Color of bricks by row **/
  private Color[] rowColor = new Color[]{Color.RED, Color.RED, Color.ORANGE, Color.ORANGE, Color.YELLOW, Color.YELLOW, Color.GREEN, Color.GREEN, Color.CYAN, Color.CYAN};
  /** Visibility of array of bricks **/
  private Boolean[][] visi = new Boolean[NBRICKS_PER_ROW][NBRICK_ROWS];
  /** Paddle object **/
  private Rectangle2D paddle;
  /** Ball object, velocity and location **/
  private int xBall = 0;
  private int yBall = 0;
  private double xVelBall = 0;
  private double yVelBall = 0;
  private Ellipse2D ball = new Ellipse2D.Double(xBall, yBall, BALL_RADIUS, BALL_RADIUS);
  /** 4 corners of ball **/
  Point2D p1 = new Point2D.Double(0, 0);
  Point2D p2 = new Point2D.Double(0, 0);
  Point2D p3 = new Point2D.Double(0, 0);
  Point2D p4 = new Point2D.Double(0, 0);
  /** Random object **/
  private Random rand = new Random();
}

// class Circle {
  
//   public void generateCircle(int panelWidth, int panelHeight) {
//     int diameter = rand.nextInt(45) + 5;
//     int rightLimit = panelWidth-diameter;
//     int bottomLimit = panelHeight-diameter;
//     int xLoc = rand.nextInt(rightLimit);
//     int yLoc = rand.nextInt(bottomLimit);
//     circles.add(new Ellipse2D.Double(xLoc, yLoc, diameter, diameter));
//   }

//   public void paintCircle(Graphics g) {
//     Graphics2D g2d = (Graphics2D) g; 
//     for (int i = 0; i < circles.size(); i++) {
//       g2d.setPaint(new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()));
//       g2d.fill(circles.get(i));
//     }
//   }

//   private Random rand = new Random();  
//   private ArrayList<Ellipse2D> circles = new ArrayList<Ellipse2D>()