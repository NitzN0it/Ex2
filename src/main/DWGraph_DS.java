package main;
import api.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class DWGraph_DS implements directed_weighted_graph {

    private HashMap<Integer,HashMap<Integer,edge_data>> edges = new HashMap<>();
    private HashMap<Integer,node_data> nodes_map = new HashMap<>();
    private int v_counter = 0;
    private int MC = 0;

    @Override
    public node_data getNode(int key) {
        if (nodes_map.containsKey(key))
            return nodes_map.get(key);
        return null;
    }

    @Override
    public edge_data getEdge(int src, int dest) {
        if (edges.containsKey(src))
        {
            if (edges.get(src).containsKey(dest))
            {
                return edges.get(src).get(dest);
            }
        }
        return null;
    }

    @Override
    public void addNode(node_data n) {
        if (n != null)
        {
            if (!nodes_map.containsKey(n.getKey()))
            {
                nodes_map.put(n.getKey(),n);
            }
        }
    }

    @Override
    public void connect(int src, int dest, double w) {
        if ((nodes_map.containsKey(src)) && (nodes_map.containsKey(dest)))
        {
            edge_data e = new EdgeData(src,dest,w);
            if (edges.containsKey(src))
            {
                edges.get(src).put(dest,e);
            }
            else
            {
                HashMap<Integer,edge_data> temp_hashMap=new HashMap<>();
                temp_hashMap.put(dest,e);
                edges.put(src,temp_hashMap);
            }
        }
    }

    @Override
    public Collection<node_data> getV() {
        return nodes_map.values();
    }

    @Override
    public Collection<edge_data> getE(int node_id) {
        LinkedList<edge_data> node_edges = new LinkedList<>();
        if (edges.containsKey(node_id))
        {
            for (edge_data e:edges.get(node_id).values()) {
                node_edges.add(e);
            }
        }
        return node_edges;
    }

    @Override
    public node_data removeNode(int key) {
        if (nodes_map.containsKey(key)) {
            if (edges.containsKey(key)) {
                for (int node : edges.get(key).keySet()) {
                    edges.get(node).remove(key);
                }
                v_counter -= edges.remove(key).values().size();
            }
            MC++;
            return nodes_map.remove(key);
        }
        return null;
    }

    @Override
    public edge_data removeEdge(int src, int dest) {
        if ((nodes_map.containsKey(src)) && (nodes_map.containsKey(dest)))
        {
            if ((edges.containsKey(src)) && (edges.get(src).containsKey(dest)))
            {
                v_counter--;
                MC++;
                return edges.get(src).remove(dest);

            }
        }
        return null;
    }

    @Override
    public int nodeSize() {
        return nodes_map.size();
    }

    @Override
    public int edgeSize() {
        return v_counter;
    }

    @Override
    public int getMC() {
        return MC;
    }
}
