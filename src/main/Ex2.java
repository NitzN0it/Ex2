package main;
import Server.Game_Server_Ex2;
import api.game_service;
import main.GUI.Ex2_GUI;
import gameClient.graph_json_parser;

public class Ex2 {
    public static void main(String[] args) {
        int scenario_num = 0;
        game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
        //	int id = 999;
        //	game.login(id);
        String g = game.getGraph();
        String pkm = game.getPokemons();
        System.out.println(pkm);
        graph_json_parser parser = new graph_json_parser(g);
        Ex2_GUI gui = new Ex2_GUI(parser.getGraph());
        gui.setVisible(true);
        gui.repaint();
    }
}
