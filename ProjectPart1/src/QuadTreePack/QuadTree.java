package QuadTreePack;


import java.util.ArrayList;
import krakloader.NodeData;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Archigo
 */
public class QuadTree {

    public QuadTree northeast, northwest, southeast, southwest;
    public Boundary boundary;
    private final int sizeLimit = 100;
    public Road[] roadList;
    public int currentRoads = -1;

    //test
    public QuadTree(NSEW direction) {
        if(direction == NSEW.ROOT) {
            boundary = new Boundary(direction);
            roadList = new Road[sizeLimit];
            
        } else{
        boundary = new Boundary(direction, boundary);
        roadList = new Road[sizeLimit];
        }
    }

    public void insert(Road rd) {
        if (checkBounds(rd)) {
            if (!(northeast == null) || !(northwest == null) || !(southeast == null) || !(southwest == null)) {
                if (checkBounds(rd)) {
                    northeast.insert(rd);
                } else if (checkBounds(rd)) {
                    northwest.insert(rd);
                } else if (checkBounds(rd)) {
                    southeast.insert(rd);
                } else if (checkBounds(rd)) {
                    southwest.insert(rd);
                }
                
            } else if (currentRoads == sizeLimit - 1) {
                divide();
            } else if (currentRoads < sizeLimit) {
                roadList[++currentRoads] = rd;
            }

        }
    }
    //todo
//    public ArrayList<Road> getRoads(double x1, double x2, double y1, double y2){
//        ArrayList<Road> roads = new ArrayList<>();
//        if (!(northeast == null) || !(northwest == null) || !(southeast == null) || !(southwest == null)){
//            if (checkBounds(x1, y1, x2, y2)) {
//                    northeast.getRoads(x1, x2, y1, y2);
//                }
//            if (checkBounds(x1, y1, x2, y2)) {
//                    northwest.getRoads(x1, x2, y1, y2);
//                }
//            if (checkBounds(x1, y1, x2, y2)) {
//                    southeast.getRoads(x1, x2, y1, y2);
//                }
//            if (checkBounds(x1, y1, x2, y2)) {
//                    southwest.getRoads(x1, x2, y1, y2);
//                }
//        } else{
//            for(Road rd: roadList){
//                roads.add(rd);
//            }
//        }
//        
//        return roads;
//    }

    private void divide() {
        northeast = new QuadTree(NSEW.NORTHEAST);
        northwest = new QuadTree(NSEW.NORTHWEST);
        southeast = new QuadTree(NSEW.SOUTHEAST);
        southwest = new QuadTree(NSEW.SOUTHWEST);

        for (Road rd : roadList) {
            insert(rd);
        }
    }
    
    private boolean checkBounds(Road rd){        
        return boundary.containsPoint(rd.midX, rd.midY); 
    }
    
    private boolean checkBounds(double x1, double x2, double y1, double y2){
            return boundary.containsPoint(x1, y1) || boundary.containsPoint(x2, y2);
    }

}
