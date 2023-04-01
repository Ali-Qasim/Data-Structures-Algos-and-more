package AnimatedShapes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

public class GraphicsToys extends JComponent implements MouseListener, MouseMotionListener, MouseWheelListener {

    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(800,600));

        GraphicsToys gt = new GraphicsToys();
        frame.add(gt);

        frame.pack();
        frame.setVisible(true);

        while (frame.isVisible()){

            gt.tick();

            frame.repaint();
            Thread.sleep(1000 / 60);
        }
    }

    private java.util.List<Shape> shapes;
    private java.util.List<Color> colours;

    private Random random;
    private double scale;
    private double max = 10.0;
    private Color color;
    private double rotate;

    private int sides;

    public GraphicsToys(){
        this.shapes = new ArrayList<>();
        this.colours = new ArrayList<>();
        this.sides = 0;

        this.random = new Random();
        this.color = Color.cyan;

        this.addMouseListener(this);
        this.addMouseWheelListener(this);
    }

    protected void tick(){
        rotate = (rotate - 2) % 360;
    }

    protected void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D)g;

        g2.setColor(Color.darkGray);
        g2.fillRect(0,0, getWidth(), getHeight());

        double sx = getWidth() / 800.0;
        double sy = getHeight() / 600.0;
        g2.scale(sx, sy);

        AffineTransform at = g2.getTransform();

        g2.setColor(color);
        g2.drawRect(0,0, (int) (60*max), 20);
        g2.fillRect(0,0, (int)(scale*60), 20);

        for (int i=0; i<shapes.size(); i++) {

            Shape shape = shapes.get(i);
            Color color = colours.get(i);
            g2.rotate(Math.toRadians(rotate), shape.getBounds2D().getCenterX(), shape.getBounds2D().getCenterY());

            g2.setColor(color);
            g2.fill(shape);

            g2.setTransform(at);
        }


        g2.setTransform(at);
    }

    public static Shape getScaledPolygon(double cx, double cy, double radius, int sides) {
        Path2D shape = new Path2D.Double();
        for (int i = 0; i < sides; i++) {
            double angle = 2 * Math.PI / sides * (i + 0.5);

            double x = cx + radius * Math.cos(angle);
            double y = cy + radius * Math.sin(angle);

            if (i == 0) {
                shape.moveTo( x, y );
            } else {
                shape.lineTo(x, y);
            }
        }
        shape.closePath();
        return shape;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        if (e.getButton() == MouseEvent.BUTTON1) {

            double wx = (e.getX() / (double)getWidth()) * 800;
            double wy = (e.getY() / (double)getHeight()) * 600;

            shapes.add(getScaledPolygon(wx, wy, scale * 50, sides + 3));
            colours.add(color);
            sides = ((sides + 1) % 6);

            repaint();
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            colours.clear();
            shapes.clear();
            repaint();
        } else if (e.getButton() == MouseEvent.BUTTON2) {

            double wx = (e.getX() / (double)getWidth()) * 800;
            double wy = (e.getY() / (double)getHeight()) * 600;
            Point.Double clickScale = new Point2D.Double(wx, wy);

            int index = -1;
            for (int i=0; i<shapes.size(); i++) {
                Shape shape = shapes.get(i);

                if (shape.contains(clickScale)) {
                    index = i;
                }
            }

            if (index == -1) {
                int r = random.nextInt(255);
                int g = random.nextInt(255);
                int b = random.nextInt(255);
                color = new Color(r, g, b);
            } else {
                color = colours.get(index);
            }

            repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {


    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        scale += 0.05 * e.getUnitsToScroll();
        scale = Math.min(scale, max);
        scale = Math.max(scale, 0.01);

        repaint();
    }
}
