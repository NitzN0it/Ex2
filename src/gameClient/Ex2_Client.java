package gameClient;

import Server.Game_Server_Ex2;
import api.directed_weighted_graph;
import api.edge_data;
import api.game_service;
import api.node_data;
import main.DWGraph_Algo;
import main.EdgeData;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class Ex2_Client implements Runnable{
	private static MyFrame _win;
	private static Arena _ar;
	private static directed_weighted_graph gg;
	private static DWGraph_Algo graph_algo = new DWGraph_Algo();
	private static Agents_Algos agents_algos = new Agents_Algos();
	public static void main(String[] a) {
		Thread client = new Thread(new Ex2_Client());
		client.start();
	}
	
	@Override
	public void run() {
		int scenario_num = 0;
		game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
	//	int id = 999;
	//	game.login(id);
		init(game);
		game.startGame();
		_win.setTitle("Ex2 - OOP: (NONE trivial Solution) "+game.toString());
		int ind=0;
		long dt=100;
		while(game.isRunning()) {
			moveAgants(game, gg);
			try {
				if(ind%1==0) {_win.repaint();}
				Thread.sleep(dt);
				ind++;
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		String res = game.toString();

		System.out.println(res);
		System.exit(0);
	}
	/** 
	 * Moves each of the agents along the edge,
	 * in case the agent is on a node the next destination (next edge) is chosen (randomly).
	 * @param game
	 * @param gg
	 * @param
	 */
	private static void moveAgants(game_service game, directed_weighted_graph gg) {
		String lg = game.move();
		List<CL_Agent> agents = Arena.getAgents(lg, gg);
		_ar.setAgents(agents);
		//ArrayList<OOP_Point3D> rs = new ArrayList<OOP_Point3D>();
		String fs =  game.getPokemons();
		List<CL_Pokemon> pokemons = Arena.json2Pokemons(fs);
		_ar.setPokemons(pokemons);
		for(int a = 0;a<pokemons.size();a++) { _ar.updateEdge(pokemons.get(a),gg);}
		agents_algos.updateAlgos(pokemons,agents,gg);
		for(int i=0;i<agents.size();i++) {
			CL_Agent ag = agents.get(i);
			int id = ag.getID();
			int dest = ag.getNextNode();
			int src = ag.getSrcNode();
			double v = ag.getValue();
			if(dest==-1) {
				//dest = nextNode(gg, src);
				dest = agents_algos.agent_NextNode(id,src);
				game.chooseNextEdge(ag.getID(), dest);
				System.out.println("Agent: "+id+", val: "+v+"   turned to node: "+dest);
			}
		}
	}
	/**
	 * a very simple random walk implementation!
	 * @param g
	 * @param src
	 * @return
	 */
	private static boolean has_2way_edge (edge_data e)
	{
		return gg.getEdge(e.getDest(),e.getSrc()) != null;
	}
	private static void fix_direction (CL_Pokemon pokemon)
	{
		if (pokemon.getType() == -1)
		{
			if (pokemon.get_edge().getSrc() < pokemon.get_edge().getDest())
			{
				edge_data temp = new EdgeData(pokemon.get_edge().getDest(),pokemon.get_edge().getSrc(),pokemon.get_edge().getWeight());
				pokemon.set_edge(temp);
			}
		}
		else
		{
			if (pokemon.get_edge().getSrc() > pokemon.get_edge().getDest())
			{
				edge_data temp = new EdgeData(pokemon.get_edge().getDest(),pokemon.get_edge().getSrc(),pokemon.get_edge().getWeight());
				pokemon.set_edge(temp);
			}
		}
	}
	private static int nextNode(directed_weighted_graph g, int src) {
		//return agents_algos.agent_NextNode(_ar.ge)


		List<CL_Pokemon> pokemons = _ar.getPokemons();
		double min = Double.MAX_VALUE;
		double max_value = Double.MIN_VALUE;
		CL_Pokemon pokemon = pokemons.get(0);
		for (CL_Pokemon pok : pokemons) // find the pokemon with the most value
		{
			if (pok.getValue() > max_value)
			{
				max_value = pok.getValue();
				pokemon = pok;
			}
		}
				System.out.println("Agent's node:"+src+" pokemon type:"+pokemon.getType()+" from:"+pokemon.get_edge().getSrc() +"--->" + pokemon.get_edge().getDest());
				System.out.println("pokemon has 2way edge:"+has_2way_edge(pokemon.get_edge()));
		if (has_2way_edge(pokemon.get_edge()))
			fix_direction(pokemon);
		System.out.println(pokemon.get_edge().getSrc() +"--->" + pokemon.get_edge().getDest());

		int dest = pokemon.get_edge().getDest();
		if (pokemon.get_edge().getSrc() == src) return dest;
		if (src == dest) return pokemon.get_edge().getSrc();
		List<node_data> lst = graph_algo.shortestPath(src,dest);
		for (node_data n : lst)
		{
			System.out.print(n.getKey()+"->");
		}
		System.out.println("Go to:"+ lst.get(1).getKey());
		return lst.get(1).getKey();
		/*
		for (CL_Pokemon pokemon:pokemons) {
			System.out.println(pokemon.get_edge().getSrc() +"->" + pokemon.get_edge().getDest());
			int dest = pokemon.get_edge().getDest();
			if (pokemon.get_edge().getSrc() == src) { ans=dest; break;}
			if (dest == src) dest = 0;
			/*
			int dest = 0;
			if (pokemon.getType() < 0)
				dest = pokemon.get_edge().getSrc();
			else
				dest = pokemon.get_edge().getDest();

			if (src == dest)
			{
				dest = pokemon.get_edge().getSrc();
			}


			List<node_data> lst = graph_algo.shortestPath(src,dest);
			double path = lst.size();
			System.out.println(path);
			if (path < min)
			{
				min = path;
				ans = lst.get(1).getKey();
			}
		}
		System.out.println("move to "+ans);
	*/
		//return ans;
	}
	private int find_into_node_edge(int node)
	{
		for (node_data n: gg.getV())
		{

		}
		return 3;
	}

	private void init(game_service game) {
		String g = game.getGraph();
		graph_json_parser graph_parser = new graph_json_parser(g);
		gg = graph_parser.getGraph();
		graph_algo.init(gg);
		String fs = game.getPokemons();
		_ar = new Arena();
		_ar.setGraph(gg);
		_ar.setPokemons(Arena.json2Pokemons(fs));
		_win = new MyFrame("Ex2");
		_win.setSize(1000, 700);
		_win.update(_ar);
		_win.show();
		String info = game.toString();
		JSONObject line;
		try {
			line = new JSONObject(info);
			JSONObject gameServerInfo = line.getJSONObject("GameServer");
			int agents_num = gameServerInfo.getInt("agents");
			System.out.println(info);
			System.out.println(game.getPokemons());
			int src_node = 0;  // arbitrary node, you should start at one of the pokemon
			List<CL_Pokemon> cl_fs = _ar.getPokemons();
			for(int a = 0;a<cl_fs.size();a++) { _ar.updateEdge(cl_fs.get(a),gg);}
			List<CL_Agent> ags = new LinkedList<>();
			for (int i = 0; i < agents_num; i++) { CL_Agent agent = new CL_Agent(gg,0); ags.add(agent);}
			agents_algos.updateAlgos(cl_fs,ags,gg);
			for (int i = 0; i < agents_num; i++) {
				int temp = agents_algos.src_node_for_agent(i);
				System.out.println("Agent first node is:"+temp);
				game.addAgent(temp);
			}
			_ar.setAgents(agents_algos.getAgentList());
		}
		catch (JSONException e) {e.printStackTrace();}
	}
}
