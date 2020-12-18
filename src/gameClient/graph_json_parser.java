package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import main.DWGraph_Algo;
import main.DWGraph_DS;
import main.GeoLocation;
import main.NodeData;

import java.util.ArrayList;


public class graph_json_parser {
    private  JSON_graph g;
    private class JSON_graph {
        @Expose
        ArrayList<JSON_edge> Edges = new ArrayList<>();
        @Expose
        ArrayList<JSON_node> Nodes = new ArrayList<>();
        public JSON_graph() {
        }
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
    public graph_json_parser(String s)
    {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        g =gson.fromJson(s, JSON_graph.class);
    }
    public directed_weighted_graph getGraph()
    {
        return g.load();
    }

}
