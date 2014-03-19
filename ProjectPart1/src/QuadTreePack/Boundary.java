/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QuadTreePack;

/**
 *
 * @author Archigo
 */
public class Boundary {

    public double xdim, ydim;
    Center center;

    public Boundary(NSEW direction, Boundary bd) {

        

        switch (direction) {
            case NORTHEAST:
                //System.out.println("ne");
                center.xc = 1.5 * bd.center.xc;
                center.yc = 0.5 * bd.center.yc;
                break;
            case NORTHWEST:
                //System.out.println("nw");
                center.xc = 0.5 * bd.center.xc;
                center.yc = 0.5 * bd.center.yc;
                break;
            case SOUTHEAST:
                //System.out.println("se");
                center.xc = 1.5 * bd.center.xc;
                center.yc = 1.5 * bd.center.xc;
                break;
            case SOUTHWEST:
                //System.out.println("sw");
                center.xc = 0.5 * bd.center.xc;
                center.yc = 1.5 * bd.center.yc;
                break;
            case ROOT:
                center.xc = MapPack.Main.xmax/2;
                center.yc = MapPack.Main.ymax/2;
                xdim = MapPack.Main.xmax;
                ydim = MapPack.Main.ymax;
                break;

        }
    }
        
        public Boundary(NSEW direction) {
                center = new Center();
                center.xc = MapPack.Main.xmax/2;
                center.yc = MapPack.Main.ymax/2;
                xdim = MapPack.Main.xmax;
                ydim = MapPack.Main.ymax;

    }

    public boolean containsPoint(double x, double y) {
        
        if((center.xc - xdim) < x && x > (center.xc + xdim)
                && (center.yc - ydim) < y && (center.yc + ydim) > y)
        {
            return true;
        }
        else return false;
    }

}
