import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.Ellipse2D;
import java.awt.Color;
import java.util.Random;
import java.awt.Graphics2D;
import java.io.*;
import java.util.ArrayList;

class Main {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        DrawCircles f = new DrawCircles();
        f.drawCircles();
      }
    });
  }
}

class DrawCircles extends JFrame {
  public void drawCircles() {
    JFrame f = new JFrame("Draw Random Circles");
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    DrawPanel d = new DrawPanel();
    f.add(d);
    f.pack();
    f.setVisible(true);

    // for (int i = 0; i < 10; i++) {
    //   c.generateCircle(d.getWidth(), d.getHeight());
    // }
  }
}

class DrawPanel extends JPanel {

  public DrawPanel() {
    for (int i = 0; i < 10; i++) {
      c.generateCircle(getPreferredSize().width, getPreferredSize().height);
    }
  }

  public Dimension getPreferredSize() {
      return new Dimension(250,200);
  }

  public void paintComponent(Graphics g) {
      super.paintComponent(g);  
      c.paintCircle(g);
  }  

  private Circle c = new Circle();

}

class Circle {
  
  public void generateCircle(int panelWidth, int panelHeight) {
    int diameter = rand.nextInt(45) + 5;
    int rightLimit = panelWidth-diameter;
    int bottomLimit = panelHeight-diameter;
    int xLoc = rand.nextInt(rightLimit);
    int yLoc = rand.nextInt(bottomLimit);
    circles.add(new Ellipse2D.Double(xLoc, yLoc, diameter, diameter));
  }

  public void paintCircle(Graphics g) {
    Graphics2D g2d = (Graphics2D) g; 
    for (int i = 0; i < circles.size(); i++) {
      g2d.setPaint(new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()));
      g2d.fill(circles.get(i));
    }
  }

  private Random rand = new Random();  
  private ArrayList<Ellipse2D> circles = new ArrayList<Ellipse2D>();
}