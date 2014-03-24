/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class Quadtree {

    private static Quadtree instance = null;
    private static double xmax = 0, ymax = 0, xmin = 0;
    private final int MAX_OBJECTS = 5000;
    private final int MAX_LEVELS = 2500;

    private final int level;
    private List<Road> objects;
    public final Rectangle bounds;
    public final Quadtree[] nodes;

    public static Quadtree getInstance(int pLevel, Rectangle pBounds) {
        if (instance == null) {
            instance = new Quadtree(pLevel, pBounds);
        }
        return instance;
    }

    private Quadtree(int pLevel, Rectangle pBounds) {
        level = pLevel;
        objects = new ArrayList();
        bounds = pBounds;
        nodes = new Quadtree[4];
    }

    public void clear() {
        objects.clear();

        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] != null) {
                nodes[i].clear();
                nodes[i] = null;
            }
        }
    }

    private void split() {
        int subWidth = (int) (bounds.getWidth() / 2);
        int subHeight = (int) (bounds.getHeight() / 2);
        int x = (int) bounds.getX();
        int y = (int) bounds.getY();

        nodes[0] = new Quadtree(level + 1, new Rectangle(x, y, subWidth, subHeight));
        nodes[1] = new Quadtree(level + 1, new Rectangle(x + subWidth, y, subWidth, subHeight));
        nodes[2] = new Quadtree(level + 1, new Rectangle(x, y + subHeight, subWidth, subHeight));
        nodes[3] = new Quadtree(level + 1, new Rectangle(x + subWidth, y + subHeight, subWidth, subHeight));
    }

    private int getIndex(Rectangle pRect) {
        int index = -1;
        double verticalMidpoint = bounds.getX() + (bounds.getWidth() / 2);
        double horizontalMidpoint = bounds.getY() + (bounds.getHeight() / 2);

        // Object can completely fit within the top quadrants
        boolean topQuadrant = (pRect.getY() < horizontalMidpoint && pRect.getY() + pRect.getHeight() < horizontalMidpoint);
        // Object can completely fit within the bottom quadrants
        boolean bottomQuadrant = (pRect.getY() > horizontalMidpoint);

        // Object can completely fit within the left quadrants
        if (pRect.getX() < verticalMidpoint && pRect.getX() + pRect.getWidth() < verticalMidpoint) {
            if (topQuadrant) {
                index = 0;
            } else if (bottomQuadrant) {
                index = 2;
            }
        } // Object can completely fit within the right quadrants
        else if (pRect.getX() > verticalMidpoint) {
            if (topQuadrant) {
                index = 1;
            } else if (bottomQuadrant) {
                index = 3;
            }
        }

        return index;
    }

    public void insert(Road r) {
        if (nodes[0] != null) {
            int index = getIndex(roadToRect(r));

            if (index != -1) {
                nodes[index].insert(r);

                return;
            }
        }

        objects.add(r);

        if (objects.size() > MAX_OBJECTS && level < MAX_LEVELS) {
            if (nodes[0] == null) {
                split();

                int i = 0;
                while (i < objects.size()) {
                    int index = getIndex(roadToRect(objects.get(i)));
                    if (index != -1) {
                        nodes[index].insert(objects.get(i));
                        objects.remove(i);
                    } else {
                        i++;
                    }
                }
            }
        }
    }

    public List<Road> retrieve(List<Road> returnObjects, Rectangle pRect) {
        System.out.println("" + this.level);
        int index = getIndex(pRect);
        System.out.println("" + index);
        if (index == -1 && nodes[0] != null) {
            returnObjects.addAll(objects);
            for (Quadtree q : nodes) {
                q.retrieve(returnObjects, q.bounds);
            }
        } else if (index != -1 && nodes[0] != null) {
            nodes[index].retrieve(returnObjects, pRect);
        } else {

            for (Road r : objects) {
                calcMax(r);
                CurrentData cd = CurrentData.getInstance();
                cd.setXmax(xmax);
                cd.setYmax(ymax);
                cd.setXmin(xmin);
                returnObjects.add(r);
            }
        }
        //returnObjects.addAll(objects);

        return returnObjects;
    }

    private Rectangle roadToRect(Road r) {
        return new Rectangle((int) r.midX, (int) r.midY, 1, 1);
    }

    private void calcMax(Road r) {
        double xcord;
        double ycord;
        if (r.getFn().getX_COORD() > r.getTn().getX_COORD()) {
            xcord = r.getFn().getX_COORD();
        } else {
            xcord = r.getTn().getX_COORD();
        }

        if (r.getFn().getY_COORD() > r.getTn().getY_COORD()) {
            ycord = r.getFn().getX_COORD();
        } else {
            ycord = r.getTn().getY_COORD();
        }

        if (xcord > xmax) {
            xmax = xcord;
        }
        if (xmin == 0 || xcord < xmin) {
            xmin = xcord;
        }
        if (ycord > ymax) {
            ymax = ycord;
        }
    }
}
