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
			int scenario_num = 11;
			game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
			int id = 311318612;
			game.login(id);
			init(game);
			game.startGame();
			_win.setTitle("Ex2 - OOP: (NONE trivial Solution) " + game.toString());
			int ind = 0;
			long dt = 100;
			while (game.isRunning()) {
				moveAgants(game, gg);
				try {
					if (ind % 1 == 0) {
						_win.repaint();
					}
					Thread.sleep(dt);
					ind++;
				} catch (Exception e) {
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
