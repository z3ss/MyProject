/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 *
 * @author z3ss
 */
public class CurrentData extends Observable {

    private static CurrentData instance = null;
    private final Quadtree qt;
    private double xmax = 0, ymax = 0, xmin = 0;
    private List<Road> roads = new ArrayList<>();

    public static CurrentData getInstance() {
        if (instance == null) {
            instance = new CurrentData();
        }
        return instance;
    }

    private CurrentData() {
        this.qt = Quadtree.getInstance(0, new Rectangle(0, 0));
    }

    public List<Road> getRoads() {
        return roads;
    }

    public void updateArea(Rectangle r) {
        roads.clear();
        qt.retrieve(roads, r);
        setChanged();
        notifyObservers();
    }

    public void setXmax(double xmax) {
        this.xmax = xmax;
    }

    public void setYmax(double ymax) {
        this.ymax = ymax;
    }

    public void setXmin(double xmin) {
        this.xmin = xmin;
    }

    public double getXmax() {
        return xmax;
    }

    public double getYmax() {
        return ymax;
    }

    public double getXmin() {
        return xmin;
    }
    
}
