package gameClient;

import api.directed_weighted_graph;
import api.dw_graph_algorithms;
import api.edge_data;
import api.node_data;
import main.DWGraph_Algo;
import main.EdgeData;

import java.util.*;
public class Agents_Algos {
    private PriorityQueue<CL_Pokemon> pokemons = new PriorityQueue<>(new PokemonComparator());
    private List<CL_Agent> agentList;
    private directed_weighted_graph graph;
    private dw_graph_algorithms graph_algo = new DWGraph_Algo();


    public Agents_Algos(){}
    public void updateAlgos(List<CL_Pokemon> pks, List<CL_Agent> agts, directed_weighted_graph g)
    {
        pokemons.clear();
        pokemons.addAll(pks);
        set_agents_to_pokemons(agts);
        graph=g;
        graph_algo.init(g);
    }
    private void set_agents_to_pokemons(List<CL_Agent> agts)
    {
        if (agentList == null)
            agentList=agts;
        else
        {
            List<CL_Agent> temp = agentList;
            agentList = agts;
            for (CL_Agent agent:agentList) {
                CL_Pokemon curr_fruit = temp.get(agent.getID()).get_curr_fruit();
                if ((curr_fruit == null) || (!pokemons.contains(curr_fruit)))
                {
                    curr_fruit=pokemons.remove();
                    agent.set_curr_fruit(curr_fruit);
                }
                else
                {
                    fix_taken_pokemon(agent);

                }
            }
        }
    }
    private void fix_taken_pokemon(CL_Agent age)
    {
        for (CL_Agent agent:agentList) {
            if((age.get_curr_fruit().getValue() == agent.get_curr_fruit().getValue()) && (agent != age))
            {
                while (age.get_curr_fruit() == agent.get_curr_fruit())
                {
                    age.set_curr_fruit(pokemons.remove());
                }
            }
        }
    }
    public int src_node_for_agent(int i)
    {
        List<CL_Pokemon> temp = new LinkedList<>();
        for (int j = 0; j <= i; j++) {
            temp.add(pokemons.remove());
        }
        pokemons.addAll(temp);
        agentList.get(i).set_curr_fruit(temp.get(i));
        int estimated_best_src = temp.get(i).get_edge().getSrc();
        return agentList.get(i).get_curr_fruit().get_edge().getSrc();
    }
    public List<CL_Agent> getAgentList() { return agentList;}
    private boolean has_2way_edge (edge_data e)
    {
        return graph.getEdge(e.getDest(),e.getSrc()) != null;
    }
    private void fix_direction (CL_Pokemon pokemon)
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
    public int agent_NextNode(int agent_id,int src)
    {
        CL_Pokemon pokemon = agentList.get(agent_id).get_curr_fruit();
        System.out.println("Agent's node:"+src+" pokemon type:"+pokemon.getType()+" from:"+pokemon.get_edge().getSrc() +"--->" + pokemon.get_edge().getDest());
        System.out.println("pokemon has 2way edge:"+has_2way_edge(pokemon.get_edge()));
        if (has_2way_edge(pokemon.get_edge()))
            fix_direction(pokemon);
        System.out.println(pokemon.get_edge().getSrc() +"--->" + pokemon.get_edge().getDest());
        int dest = pokemon.get_edge().getSrc();
        if (src == dest) return pokemon.get_edge().getDest();
        List<node_data> lst = graph_algo.shortestPath(src,dest);
        for (node_data n : lst)
        {
            System.out.print(n.getKey()+"->");
        }
        System.out.println("Go to:"+ lst.get(1).getKey());
        return lst.get(1).getKey();
    }
}
class PokemonComparator implements Comparator<CL_Pokemon> {
    @Override
    public int compare(CL_Pokemon o1, CL_Pokemon o2) {
        if (o1.getValue() > o2.getValue())
            return -1;
        if (o1.getValue() < o2.getValue())
            return 1;
        return 0;
    }
}


/*
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
        int temp_counter = 0;
        for (CL_Agent agent:agentList) {
            temp.add(pokemons.poll());
            agent.set_curr_fruit();
            temp_counter++;
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
        //plan_map.put(i,temp.get(i));
        agentList.get(agentList.size()).set_curr_fruit(temp.get(i));
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


 */