package MapPack;

import krakloader.NodeData;
import krakloader.EdgeData;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JFrame;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 *
 */
public class Canvas extends JComponent implements MouseListener, MouseMotionListener
{

    ArrayList<NodeData> n;
    ArrayList<EdgeData> e;

    public JFrame jf;
    Dimension currentWindowSize, originalWindowSize;
    AffineTransform transformer;

    double scale, originalX, originalY, ratio;
    boolean mouseDragged, mousePressed;
    Point mouseStart, mouseEnd, currentMouse;

    public Canvas()
    {
        jf = new JFrame();
        originalWindowSize = new Dimension(700, 500);
        currentWindowSize = originalWindowSize;
        jf.addMouseListener(this);
        jf.addMouseMotionListener(this);
        jf.setPreferredSize(originalWindowSize);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.pack();

        scale = 1.0;

        ratio = originalWindowSize.getWidth() / originalWindowSize.getHeight();

        mouseStart = new Point(1, 1);
        mouseEnd = new Point(10000, 10000);

        transformer = new AffineTransform();

        originalX = transformer.getTranslateX();
        originalY = transformer.getTranslateY();

    }

    /**
     * Takes lists of NodeData and EdgeData and saves in the canvas class
     *
     * @param nt
     * @param et
     */
    public void store(ArrayList<NodeData> nt, ArrayList<EdgeData> et)
    {
        n = nt;
        e = et;
        repaint();
    }

    /**
     * Calculates the scale modifier used to set the correct size of the map
     */
    public void calScale()
    {
        currentWindowSize = jf.getSize();
        scale = (currentWindowSize.getHeight() / originalWindowSize.height);
    }

    /**
     * Calculates the ratio between the windows heigh and width
     */
    public void calRatio()
    {
        ratio = currentWindowSize.getWidth() / currentWindowSize.getHeight();
    }

    public void mouseZoom()
    {
        currentWindowSize = jf.getSize();

        int width = (int) currentWindowSize.getWidth();

        Point tmpStart = new Point(mouseStart);
        Point tmpEnd = new Point(mouseEnd);

        if (mouseStart.getX() > mouseEnd.getX())
        {
            mouseStart.setLocation(tmpEnd.getX(), tmpStart.getY());
            mouseEnd.setLocation(tmpStart.getX(), tmpEnd.getY());
        }
        if (mouseStart.getY() > mouseEnd.getY())
        {
            mouseStart.setLocation(mouseStart.getX(), tmpEnd.getY());
            mouseEnd.setLocation(mouseEnd.getX(), tmpStart.getY());
        }

        if (mouseStart.equals(mouseEnd))
        {
            mouseStart.setLocation(1, 1);
            mouseEnd.setLocation(100000, 100000);
            transformer.setToTranslation(originalX, originalY);
            transformer.scale(1, 1);
        } else
        {
            transformer.scale((width / (mouseEnd.getX() - mouseStart.getX())), (width / (mouseEnd.getX() - mouseStart.getX())));
            transformer.translate(-mouseStart.getX(), -mouseStart.getY());
        }

        repaint();
    }

    /**
     * Paints the map. Called by repaint
     *
     * @param g
     */
    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;

        g2.transform(transformer);

        calScale();
        calRatio();

        for (EdgeData ed : e)
        {
            double x1, x2, y1, y2;
            x1 = (n.get(ed.FNODE - 1).X_COORD - 442254.35659) / 800;
            y1 = (-n.get(ed.FNODE - 1).Y_COORD + 6402050.98297) / 800;
            x2 = (n.get(ed.TNODE - 1).X_COORD - 442254.35659) / 800;
            y2 = (-n.get(ed.TNODE - 1).Y_COORD + 6402050.98297) / 800;

            //checks whether the whole map or a dragged rectangle should be showed.            
            if (x1 * scale > mouseStart.getX() && x2 * scale < mouseEnd.getX() && y1 * scale > mouseStart.getY() && y2 * scale < mouseEnd.getY())
            {
                Shape road = new Line2D.Double(x1 * scale, y1 * scale, x2 * scale, y2 * scale);
               
                //Road colering:
                switch (ed.TYP)
                {
                    case 1:
                        g2.setColor(Color.RED); //Highway
                        break;
                    case 3:
                        g2.setColor(Color.BLUE); //Main Roads
                        break;
                    case 8:
                        g2.setColor(Color.GREEN); //Path
                        break;
                    default:
                        g2.setColor(Color.BLACK); //Other
                        break;
                }
                g2.draw(road);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent me)
    {
        //s
    }

    /**
     * Sets the top left values of a dragged rectangle
     *
     * @param me
     */
    @Override
    public void mousePressed(MouseEvent me)
    {
        mouseStart = me.getPoint();
        mousePressed = true;
    }

    /**
     * set the bottom left corner of a dragged rectangle
     *
     * @param me
     */
    @Override
    public void mouseReleased(MouseEvent me)
    {
        mouseEnd = me.getPoint();
        mousePressed = false;
        this.mouseZoom();
    }

    @Override
    public void mouseEntered(MouseEvent me)
    {
        //s
    }

    @Override
    public void mouseExited(MouseEvent me)
    {
        //s
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        currentMouse = e.getPoint();
        mouseDragged = true;
        drawZoomArea();
        e.consume();//Stops the event when not in use, makes program run faster
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        currentMouse = e.getPoint();
        mouseDragged = false;
        //System.out.println("X-coordinate: " + e.getX() + ", Y-coodinate: " + e.getY());
        e.consume();//Stops the event when not in use, makes program run faster
    }

    public void drawZoomArea()
    {
        Graphics g = getGraphics();
        if (mousePressed)
        {
            int width = (int) currentMouse.getX() - (int) mouseStart.getX();
            g.drawRect((int) mouseStart.getX(), (int) mouseStart.getY(), width, width / (int) ratio); // Tager ikke Double.. Laves om til java2d???????????????????????????????????
        }
        repaint();
    }
}
