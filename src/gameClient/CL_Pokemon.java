package gameClient;
import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import org.json.JSONObject;

public class CL_Pokemon {
	private final double EPS = 0.00001;
	private edge_data _edge;
	private double _value;
	private int _type;
	private Point3D _pos;
	private double min_dist;
	private int min_ro;
	
	public CL_Pokemon(Point3D p, int t, double v, double s, edge_data e) {
		_type = t;
	//	_speed = s;
		_value = v;
		set_edge(e);
		_pos = p;
		min_dist = -1;
		min_ro = -1;
	}
	public static CL_Pokemon init_from_json(String json) {
		CL_Pokemon ans = null;
		try {
			JSONObject p = new JSONObject(json);
			int id = p.getInt("id");

		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return ans;
	}
	/*
	public void poke_edge(directed_weighted_graph g)
	{
		for (node_data node:g.getV())
		{
			for (edge_data edge:g.getE(node.getKey()))
			{
				if (on_edge(g,edge))
				{
					_edge = edge;
					break;
				}
			}
		}
	}

	 */
	public boolean on_edge(directed_weighted_graph g, edge_data edge)
	{
		if ((_type < 0) && (edge.getDest() > edge.getSrc()))
			return false;
		if ((_type > 0) && (edge.getSrc() > edge.getDest()))
			return false;
		geo_location src = g.getNode(edge.getSrc()).getLocation();
		geo_location dest = g.getNode(edge.getDest()).getLocation();
		double distance = src.distance(dest);
		double d= src.distance(_pos) + _pos.distance(dest);
		return distance>d-EPS;
	}
	public String toString() {return "F:{v="+_value+", t="+_type+"}";}
	public edge_data get_edge() {
		return _edge;
	}
	public void set_edge(edge_data _edge) {
		this._edge = _edge;
	}
	public Point3D getLocation() {
		return _pos;
	}
	public int getType() {return _type;}
//	public double getSpeed() {return _speed;}
	public double getValue() {return _value;}

	public double getMin_dist() {
		return min_dist;
	}

	public void setMin_dist(double mid_dist) {
		this.min_dist = mid_dist;
	}

	public int getMin_ro() {
		return min_ro;
	}

	public void setMin_ro(int min_ro) {
		this.min_ro = min_ro;
	}
}
