package tests;
import main.*;
import api.*;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_DSTest {

    DWGraph_DS graph;
    int v,e;
    int [][] edges = new int[1][1];
    @Test
    void getNode() {
        create_graph(1000000);
        assertNotNull(graph.getNode(0));
        assertNotNull(graph.getNode(500));
        assertNotNull(graph.getNode(9999));
        assertNull(graph.getNode(-5));
        assertNull(graph.getNode(10059099));
    }
    @Test
    void getEdge() {
        create_graph(1000000);
        int rnd = random_number(0,1000000*2);
        connect_graph(rnd);
        boolean ans = true;
        for (int i = 0; i < e; i++) {
            ans &= graph.getEdge(edges[i][0],edges[i][1]) != null;
        }
        assertTrue(ans);
        assertNull(graph.getEdge(-500,300));
    }
    @Test
    void getV() {
        create_graph(1000000);
        Collection<node_data> v = graph.getV();
        assertTrue(v.size() == graph.nodeSize());
    }

    @Test
    void getE() {
        create_graph(1000000);
        int rnd = random_number(0,1000000*2);
        connect_graph(rnd);
        boolean ans = true, flag = false;
            Collection<edge_data> edges = graph.getE(this.edges[random_number(0,e-1)][0]);
            for (edge_data ed : edges) {
                for (int i = 0; i < e; i++) {
                    if ((this.edges[i][0] == ed.getSrc()) && (this.edges[i][1] == ed.getDest()))
                        flag=true;
                }
                ans&=flag;
                flag=false;
            }
            assertTrue(ans);
    }

    @org.junit.jupiter.api.Test
    void removeNode() {
        create_graph(1000000);
        int rnd = random_number(0,1000000-1);
        node_data n = graph.getNode(rnd);
        int k = n.getKey();
        assertTrue(k == graph.removeNode(rnd).getKey());
        assertTrue(graph.nodeSize() == 1000000-1);
        graph.removeNode(rnd);
        assertTrue(graph.nodeSize() == 1000000-1);
        graph.removeNode(random_number(0,rnd-1));
        assertTrue(graph.nodeSize() == 1000000-2);
    }

    @org.junit.jupiter.api.Test
    void removeEdge() {
        create_graph(1000000);
        int rnd = random_number(0,100000*2);
        connect_graph(rnd);
        rnd = random_number(0,rnd-1);
        edge_data e = graph.getEdge(edges[rnd][0],edges[rnd][1]);
        graph.removeEdge(e.getSrc(),e.getDest());
        e = graph.getEdge(edges[rnd][0],edges[rnd][1]);
        assertNull(e);
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
        edges = new int[e][2];
        this.e = e;
        for (int i=0;i<e;i++)
        {
            int rnd1 = random_number(0,v-1);
            int rnd2 = random_number(rnd1+1,v-1);
                edges[i][0] = rnd1;
                edges[i][1] = rnd2;
            graph.connect(rnd1,rnd2,5);
        }
    }
    public int random_number(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}