package tests;

import api.node_data;
import main.DWGraph_Algo;
import main.DWGraph_DS;
import main.NodeData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_AlgoTest {
    DWGraph_DS graph;
    DWGraph_Algo graph_algo = new DWGraph_Algo();
    int v,e;
    int [][] edges = new int[1][1];
    @Test
    void init() {
    }

    @Test
    void getGraph() {
    }

    @Test
    void copy() {
    }

    @Test
    void isConnected() {
        create_graph(1000);
        connect_graph(2000);
        graph_algo.init(graph);
        assertTrue(graph_algo.isConnected());
    }

    @Test
    void shortestPathDist() {
        create_graph(1000);
        connect_graph(200);
        graph_algo.init(graph);
        System.out.println(graph_algo.shortestPathDist(edges[180][0],edges[180][1]));
    }

    @Test
    void shortestPath() {
        create_graph(1000);
        connect_graph(2000);
        graph.connect(1,100,1);
        graph_algo.init(graph);
        assertTrue(graph_algo.shortestPath(0,100).size() == 3);
    }

    @Test
    void save() {
        create_graph(10);
        connect_graph(20);
        graph_algo.init(graph);
        assertTrue(graph_algo.save("ESH"));
    }

    @Test
    void load() {
        graph_algo.load("data/A0");
        System.out.println(graph_algo.getGraph().getEdge(7,3) == null);
        System.out.println(graph_algo.shortestPath(7,3).size());

    }

    public void create_graph(int v)
    {
        this.v = v;
        graph = new DWGraph_DS();
        for (int i = 0; i <v; i++) {
            node_data n = new NodeData(i);
            graph.addNode(n);
        }
    }
    public void connect_graph (int e)
    {
        if (e == v*2)
        {
            for (int i = 0; i < v; i++) {
                graph.connect(0,i,i);
                graph.connect(i,0,i);
            }
        }
        else {
            edges = new int[e][2];
            this.e = e;
            for (int i = 0; i < e; i++) {
                int rnd1 = random_number(0, v - 1);
                int rnd2 = random_number(rnd1 + 1, v - 1);
                edges[i][0] = rnd1;
                edges[i][1] = rnd2;
                graph.connect(rnd1, rnd2, 5);
            }
        }
    }
    public int random_number(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}