/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;
import krakloader.*;
import model.CurrentData;
import model.Quadtree;
import model.Road;

/**
 *
 * @author KristianMohr
 */
public class Canvas extends JPanel implements Observer {

    private List<Road> rd;
    private CurrentData cd;
    private double xmax, ymax, xmin;
    private double scale = 1, offset = 30;
    private Quadtree qt = null;

    public Canvas(CurrentData cd) {
        this.cd = cd;
        xmax = cd.getXmax();
        ymax = cd.getYmax();
        xmin = cd.getXmin();
        rd = cd.getRoads();
    }

    public void setQuadtree(Quadtree qt) {
        this.qt = qt;
    }

    private void drawMap(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        scale = ymax / (double) this.getHeight();
        g2.setColor(Color.yellow);
        drawQT(g2, qt);
        for (Road r : rd) {
            double x1, x2, y1, y2;
            NodeData n1 = r.getFn();
            NodeData n2 = r.getTn();
            x1 = n1.getX_COORD() / scale + offset;
            y1 = n1.getY_COORD() / scale + offset;
            x2 = n2.getX_COORD() / scale + offset;
            y2 = n2.getY_COORD() / scale + offset;
            Shape road = new Line2D.Double(x1, y1, x2, y2);
            //Road colering:
            switch (r.getEd().TYP) {
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

    private void drawQT(Graphics2D g2, Quadtree qt) {
        Rectangle ra = new Rectangle((int)(qt.bounds.x/scale), (int)(qt.bounds.y/scale), (int)(qt.bounds.width/scale), (int)(qt.bounds.height/scale));
        g2.draw(ra);
        if (qt.nodes[0] != null) {
            for (Quadtree q : qt.nodes) {
                drawQT(g2, q);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawMap(g);
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("Here");
        rd = cd.getRoads();
        xmax = cd.getXmax();
        ymax = cd.getYmax();
        xmin = cd.getXmin();
        repaint();
    }

    public double getScale() {
        return scale;
    }

    public double getOffset() {
        return offset;
    }

}
