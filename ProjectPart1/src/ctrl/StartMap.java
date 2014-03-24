/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ctrl;

import javax.swing.JFrame;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import krakloader.EdgeData;
import krakloader.KrakLoader;
import krakloader.NodeData;
import model.Quadtree;
import model.Road;
import model.CurrentData;
import view.Canvas;

/**
 *
 * @author KristianMohr
 */
public class StartMap {

    private JFrame frame;
    private CurrentData cd;
    private Quadtree qt = null;

    public StartMap() throws IOException {
        setData();
        setup();
    }

    private void setup() throws IOException {
        frame = new JFrame("Map Draw");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        Canvas c = new Canvas(cd);
        c.addMouseListener(new ML(c));
        c.setQuadtree(qt);
        cd.addObserver(c);
        frame.add(c);
        frame.setVisible(true);
    }

    private void setData() throws IOException {
        final ArrayList<NodeData> nodes = new ArrayList<>();
        final ArrayList<EdgeData> edges = new ArrayList<>();
        final ArrayList<Road> roads = new ArrayList<>();
        String dir = "./data/";

        KrakLoader kl = new KrakLoader() {
            @Override
            public void processNode(NodeData nd) {
                nodes.add(nd);
            }

            @Override
            public void processEdge(EdgeData ed) {
                edges.add(ed);
                Road rd = new Road(ed, nodes.get(ed.FNODE - 1), nodes.get(ed.TNODE - 1));
                roads.add(rd);
            }
        };
        kl.load(dir + "kdv_node_unload.txt", dir + "kdv_unload.txt");

        for(NodeData n : nodes){
            n.recalc(kl.ymax, kl.xmin);
        }

        qt = Quadtree.getInstance(0, new Rectangle((int) (kl.xmax - kl.xmin + 1), (int) (kl.ymax-kl.ymin + 1)));
        cd = CurrentData.getInstance();
        cd.setXmax(kl.xmax - kl.xmin);
        cd.setYmax(kl.ymax - kl.ymin);
        cd.setXmin(kl.xmin);
        for (Road r : roads) {
            qt.insert(r);
        }
        cd.updateArea(new Rectangle((int) (kl.xmax - kl.xmin + 1), (int) (kl.ymax-kl.ymin + 1)));
    }

    public static void main(String[] args) throws IOException {
        StartMap sm = new StartMap();
    }
}
