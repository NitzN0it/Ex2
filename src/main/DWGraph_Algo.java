package main;
import api.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import com.google.gson.*;
import com.google.gson.annotations.Expose;

public class DWGraph_Algo implements dw_graph_algorithms{

    private directed_weighted_graph graph;
    public DWGraph_Algo(){}

    @Override
    public void init(directed_weighted_graph g) {
        graph = g;
    }

    @Override
    public directed_weighted_graph getGraph() {
        return graph;
    }

    @Override
    public directed_weighted_graph copy()
    {
        directed_weighted_graph new_graph = new DWGraph_DS();
        for (node_data node:graph.getV()) {
            node_data new_node = new NodeData(node.getKey());
            new_node.setLocation(node.getLocation());
            new_node.setWeight(node.getWeight());
            new_node.setTag(node.getTag());
            new_node.setInfo(node.getInfo());
            new_graph.addNode(new_node);
        }
        for (node_data node : graph.getV()) {
            for (edge_data edge : graph.getE(node.getKey()))
            {
                new_graph.connect(edge.getSrc(),edge.getDest(),edge.getWeight());
            }
        }
        return new_graph;
    }
    /**
    ** BFS algorithm using tag "2" for visited.
    * @param  i - the number of the node checking if its connected to node "0" (or for node 0 - connected to all nodes),
    *           g is the graph itself (the graph is parameter because we invert the edges).
    * @return true/false if the node is connected to all nodes (case it's node "0") or  connected to "0".
    **/
    public boolean node_connected(int i,directed_weighted_graph g) {
        int dest = i;
        Collection<node_data> v = g.getV();
        if (v.size() == 0) return true;
        Iterator<node_data> it = v.iterator();
        node_data first_node = new NodeData();
        while (i != -1)
        {
            first_node = it.next();
            i--;
        }
        first_node.setTag(2);
        LinkedList<node_data> queue = new LinkedList<>();
        queue.add(first_node);
        i=1;
        while (!(queue.isEmpty())) {
            for (edge_data edg : g.getE(queue.remove().getKey())) {
                if ((dest != 0) && (0 == edg.getDest())) return true;
                if (g.getNode(edg.getDest()).getTag() != 2) {
                    i++; // count number of nodes visited.
                    g.getNode(edg.getDest()).setTag(2); // tag node as visited.
                    queue.add(g.getNode(edg.getDest()));
                }
            }
        }
        tag_reset();
        return i == v.size();
    }

    /**
     * inverting the direction of the edges.
     * @return deep copy of the inverted graph.
     */
    public directed_weighted_graph invert_edges() {
        directed_weighted_graph inv_graph = new DWGraph_DS();
        for (node_data node:graph.getV()) {
            inv_graph.addNode(node);
        }
        for (node_data node:graph.getV()) {
            for (edge_data edge : graph.getE(node.getKey()))
            {
                inv_graph.connect(edge.getDest(),edge.getSrc(),edge.getWeight());
            }
        }
        return inv_graph;
    }
        @Override
        /**
         * Running on the first node checking if it's connected to all nodes.
         * if it is, Inverting the edges direction and checking if all the other nodes are connected to "0".
         **/
    public boolean isConnected() {
        boolean ans = node_connected(0,graph);
        if (ans)
        {
            directed_weighted_graph inv_graph = invert_edges();
            for (int i = 1; i < inv_graph.nodeSize(); i++) {
                ans &= node_connected(i,inv_graph);
                if (!ans)
                    return false;
            }
            return true;
        }
        return false;
    }

    /**
     * Using dijkstra algorithm, using tag "1" as visited, Every node saves it's "path" in weight parameter.
     * @param src - start node
     * @param dest - end (target) node
     * @return shortest path distance
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        PriorityQueue<node_data> queue = new PriorityQueue<>(new node_data_comparator());
        double path = 0;
        weight_inf();
        node_data current_node = graph.getNode(src);
        queue.add(current_node);
        current_node.setWeight(0);
        while (!(queue.isEmpty()))
        {
            current_node = queue.poll();
            if (current_node.getTag() == 0)
            {
                current_node.setTag(1);
                if (current_node.getKey() == dest)
                    return current_node.getWeight();
                for (edge_data edge: graph.getE(current_node.getKey()))
                {
                    path = current_node.getWeight() + edge.getWeight();
                    if (path < graph.getNode(edge.getDest()).getWeight())
                    {
                        graph.getNode(edge.getDest()).setWeight(path);
                        queue.add(graph.getNode(edge.getDest()));
                    }
                }
            }
        }
        return -1;
    }

    @Override
    public List<node_data> shortestPath(int src, int dest) {
        PriorityQueue<node_data> queue = new PriorityQueue<>(new node_data_comparator()); // priority queue for traveling the shortest path nodes.
        HashMap<node_data,node_data> parents = new HashMap<>(); // HashMap collects the traveled nodes and their "parents" - for creating the path itself.
        LinkedList<node_data> lst = new LinkedList<>(); // list contains the shortest path - for return.
        if (src == dest)
        {
            lst.add(graph.getNode(src));
            return lst;
        }
        if ( (graph.getNode(src) == null) || (graph.getNode(dest) == null) )
            return lst;
        double path = 0;
        tag_reset();
        weight_inf(); // set all tags of the nodes to infinity (as the path from the current node).
        node_data current_node = graph.getNode(src); // set the current node as the source node
        queue.add(current_node);
        current_node.setWeight(0); // set the source node distance 0.
        parents.put(current_node,current_node); // add the source node to the path collector, with "no parent".
        while (!(queue.isEmpty()))
        {
            current_node = queue.poll();
            if (current_node.getTag() == 0)
            {
                current_node.setTag(1);
                if (current_node.getKey() == dest)
                    break;
                for (edge_data edge: graph.getE(current_node.getKey()))
                {
                    path = current_node.getWeight() + edge.getWeight();
                    if (path <= graph.getNode(edge.getDest()).getWeight())
                    {
                        graph.getNode(edge.getDest()).setWeight(path);
                        queue.add(graph.getNode(edge.getDest()));
                        if (parents.containsKey(edge.getDest()))
                        {
                            parents.remove(edge.getDest());
                        }
                        parents.put(graph.getNode(edge.getDest()),current_node);
                    }
                }
            }
        }
        if (graph.getNode(dest).getTag() == 0) //if i haven't got to the destination - return empty list.
        {
            return lst;
        }
        // turn the hashmap to linked list with the shortest path.
        node_data temp = graph.getNode(dest);
        lst = new LinkedList<>();
        while (parents.get(temp) != temp)
        {
            lst.add(temp);
            temp = parents.get(temp);
        }
        lst.add(graph.getNode(src));
        Collections.reverse(lst);
        return lst;    }

    /**
     * Creates new graph (JSON_graph), edges (JSON_edge) and nodes (JSON_node) classes for saving json format.
     * Using Google's Gson as serializer and deserializer for saving the graph.
     * @param file - the file name (may include a relative path).
     * @return
     */
    @Override
    public boolean save(String file) {
        JSON_graph test = new JSON_graph();
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = builder.create();
        Path esh = Paths.get(file+".json");
        try {
            Files.write(esh, Collections.singletonList(gson.toJson(test)), StandardCharsets.UTF_8);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean load(String file) {

        String s = "";
        try
        {
            s = new String((Files.readAllBytes(Paths.get(file))));

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        System.out.println(s);
        JSON_graph g =gson.fromJson(s,JSON_graph.class);
        init(g.load());
        return true;
    }
    private class JSON_edge {
        @Expose
        int src;
        @Expose
        double w;
        @Expose
        int dest;
        public JSON_edge(int s,double _w,int d)
        {
            src = s;
            w = _w;
            dest = d;
        }
    }
    private class JSON_node {
        @Expose
        String pos;
        @Expose
        int id;
        geo_location geoLocation;
        public JSON_node(geo_location p, int _id)
        {
            pos = p.toString();
            geoLocation = p;
            id = _id;
        }
    }
    private class JSON_graph {
        @Expose
        ArrayList<JSON_edge> Edges = new ArrayList<>();
        @Expose
        ArrayList<JSON_node> Nodes = new ArrayList<>();
        public JSON_graph(){
            for  (node_data node:graph.getV())
            {
                JSON_node n = new JSON_node(node.getLocation(),node.getKey());
                Nodes.add(n);
                for (edge_data edge:graph.getE(node.getKey())) {
                    JSON_edge e = new JSON_edge(edge.getSrc(),edge.getWeight(),edge.getDest());
                    Edges.add(e);
                }
            }
        }

        /**
         * Loading the graph from json format to init as "directed_weighted_graph".
         * @return
         */
        public directed_weighted_graph load() {
            directed_weighted_graph json_graph = new DWGraph_DS();
            for (JSON_node n:Nodes)
            {
                node_data new_node = new NodeData(n.id);
                new_node.setLocation(str_to_loc(n.pos));
                json_graph.addNode(new_node);
            }
                for (JSON_edge e : Edges) {
                    json_graph.connect(e.src,e.dest,e.w);
                }
                return json_graph;
        }
        private geo_location str_to_loc (String s)
        {
            double x = Double.parseDouble(s.substring(0,s.indexOf(",")));
            s = s.substring(s.indexOf(",")+1);
            double y = Double.parseDouble(s.substring(0,s.indexOf(",")));
            s = s.substring(s.indexOf(",")+1);
            double z = Double.parseDouble(s);
            geo_location loc = new GeoLocation(x,y,z);
            return loc;
        }
    }

    private void tag_reset ()
    {
        for (node_data node:graph.getV()) {
            node.setTag(0);
        }
    }
    /*
     ** Sets all the node's tags to infinity - for dijkstra algorithm.
     */
    private void weight_inf()
    {
        for (node_data node:graph.getV())
        {
            node.setWeight(Double.MAX_VALUE);
            node.setInfo("");
        }
    }
}
