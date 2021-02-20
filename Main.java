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

class Main {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        Breakout f = new Breakout();
        f.breakout();
      }
    });
  }
}

class Breakout extends JFrame {
  public void breakout() {
    JFrame f = new JFrame("Breakout game");
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    DrawPanel d = new DrawPanel();
    f.add(d);
    f.pack();
    f.setVisible(true);
  }
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