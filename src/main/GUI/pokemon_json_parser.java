package main.GUI;
import api.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import main.GeoLocation;

public class pokemon_json_parser {
    pokemon Pokemon;
    public pokemon_json_parser(String s)
    {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Pokemon =gson.fromJson(s, pokemon.class);
    }

    private class pokemon
    {
        double value;
        int type;
        String pos;
        geo_location loc;
        public pokemon(double val, int t, String pos)
        {
            value = val;
            type = t ;
            this.pos = pos;
            loc = str_to_loc(pos);
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
}
