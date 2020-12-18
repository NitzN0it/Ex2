package gameClient;

import api.directed_weighted_graph;
import api.dw_graph_algorithms;
import api.node_data;
import main.DWGraph_Algo;

import java.util.*;

public class Agents_Algos {
    private PriorityQueue<CL_Pokemon> pokemons = new PriorityQueue<>(new PokemonComparator());
    private List<CL_Agent> agentList;
    private HashMap<Integer,CL_Pokemon> plan_map = new HashMap<>();
    private directed_weighted_graph graph;
    private dw_graph_algorithms graph_algo = new DWGraph_Algo();
    public Agents_Algos(){}
    public void updateAlgos(List<CL_Pokemon> pks, List<CL_Agent> agts, directed_weighted_graph g)
    {
        pokemons.addAll(pks);
        agentList = agts;
        graph = g;
        plan_map.clear();
        List<CL_Pokemon> temp = new LinkedList<>();
        for (CL_Agent agent:agentList) {
            temp.add(pokemons.poll());
            plan_map.put(agent.getID(),temp.get(temp.size()-1));
        }
        graph_algo.init(graph);
    }
    public int src_node_for_agent(int i)
    {
        List<CL_Pokemon> temp = new LinkedList<>();
        for (int j = 0; j <= i; j++) {
            temp.add(pokemons.remove());
        }
        pokemons.addAll(temp);
        plan_map.put(agentList.get(i).getID(),temp.get(i));
        if (temp.get(i).getType() == -1)
            return temp.get(i).get_edge().getSrc();
        return temp.get(i).get_edge().getDest();
    }
    public int agent_NextNode(int agent_id,int src)
    {
        int dest;
        dest = plan_map.get(agent_id).get_edge().getDest();
        CL_Pokemon pokemon = plan_map.get(agent_id);
        if (pokemon.get_edge().getSrc() == src) return dest;
        if (src == dest) return pokemon.get_edge().getSrc();
        List<node_data> lst = graph_algo.shortestPath(src,dest);
        for (node_data n : lst)
        {
            System.out.print(n.getKey()+"->");
        }
        System.out.println("Go to:"+ lst.get(1).getKey());
        return lst.get(1).getKey();
    }
}
class PokemonComparator implements Comparator<CL_Pokemon>
{
    @Override
    public int compare(CL_Pokemon o1, CL_Pokemon o2) {
        if (o1.getValue() < o2.getValue())
            return -1;
        if (o1.getValue() > o2.getValue())
            return 1;
        return 0;
    }
}
