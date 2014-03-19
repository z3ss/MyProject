/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MapPack;


import static QuadTreePack.NSEW.ROOT;
import QuadTreePack.QuadTree;
import QuadTreePack.Road;
import java.awt.Dimension;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.ArrayList;
import krakloader.EdgeData;
import krakloader.KrakLoader;
import krakloader.NodeData;

/**
 *
 * @author KristianMohr
 */
public class Main {

    public static final double xmax = 892658.21706;
    public static final double ymax = 6402050.98297;
    
    public static ArrayList<NodeData> nodes = new ArrayList<>();
    public static ArrayList<EdgeData> edges = new ArrayList<>();
    //public static QuadTree qt = new QuadTree(ROOT);
    public static void main(String[] args) throws IOException {
        String dir = "./data/";

        // For this example, we'll simply load the raw data into
        // ArrayLists.
        // For that, we need to inherit from KrakLoader and override
        // processNode and processEdge. We do that with an 
        // anonymous class. 
        KrakLoader loader = new KrakLoader() {
            @Override
            public void
                    processNode(NodeData nd) {
                nodes.add(nd);
            }

            @Override
            public void
                    processEdge(EdgeData ed) {
                edges.add(ed);

                //Road rd = new Road(ed, nodes.get(ed.FNODE-1), nodes.get(ed.TNODE-1));
                //qt.insert(rd);
            }
        };

        // If your machine slows to a crawl doing inputting, try
        // uncommenting this. 
        // Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        // Invoke the loader class.
        loader.load(dir + "kdv_node_unload.txt",
                dir + "kdv_unload.txt");

        // Check the results.
        System.out.printf("Loaded %d nodes, %d edges\n",
                nodes.size(), edges.size());
        MemoryMXBean mxbean = ManagementFactory.getMemoryMXBean();
        System.out.printf("Heap memory usage: %d MB%n",
                mxbean.getHeapMemoryUsage().getUsed() / (1000000));

        final Canvas c = new Canvas();

        c.setPreferredSize(new Dimension(500, 500));

        c.store(nodes, edges);

        c.jf.getContentPane().add(c);

//        double x = 0, y = 0;
//        for(NodeData n: nodes)
//        {
//          if(n.X_COORD > x) x= n.X_COORD;
//          if(n.Y_COORD > y) y= n.Y_COORD;
//        }
//        System.out.println("x coor:" + x);
//        System.out.println("y coor:" + y);
        /*
         PrintStream out = new PrintStream(dir + "nodes.txt");
         for (NodeData node : nodes) out.println(node);
         out.close();
         out = new PrintStream(dir + "edges.txt");
         for (EdgeData edge : edges) out.println(edge);
         out.close();
         */
    }
}
