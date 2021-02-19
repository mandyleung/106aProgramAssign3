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
    f.add(new DrawPanel());
    f.pack();
    f.setVisible(true);
  }
}

class DrawPanel extends JPanel {

    public Dimension getPreferredSize() {
        return new Dimension(250,200);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);  
        Graphics2D g2d = (Graphics2D) g; 

        for (int i = 0; i < 10; i++) {
          g2d.setPaint(new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()));
          g2d.fill(c.generateCircle(getWidth(), getHeight()));
        }
    }  

    private Circle c = new Circle();
    private Random rand = new Random();  
}

class Circle {
  
  public Ellipse2D generateCircle(int panelWidth, int panelHeight) {

    Random rand = new Random();
    int diameter = rand.nextInt(45) + 5;
    int rightLimit = panelWidth-diameter;
    int bottomLimit = panelHeight-diameter;
    int xLoc = rand.nextInt(rightLimit);
    int yLoc = rand.nextInt(bottomLimit);

    return new Ellipse2D.Double(xLoc, yLoc, diameter, diameter);
  }

}