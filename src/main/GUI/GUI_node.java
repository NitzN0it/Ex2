package main.GUI;

import api.*;
import main.GeoLocation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class GUI_node {
    public int x,y,key;
    private int win_h,win_w;
    private HashMap<GUI_node,Double> neighbors = new HashMap<>();
    public GUI_node(geo_location loc,int win_h, int win_w,int key)
    {
        this.win_h = win_h;
        this.win_w = win_w;
        this.key = key;
        loc_to_x_y(loc);
    }
    private void loc_to_x_y(geo_location loc)
    {
        double x=loc.x(),y=loc.y();
         final double   west = -79.974642,      north = 39.647556,
                east = -79.971244,      south = 39.644675;
        x = (int) ((win_w) * (x-east) / (west-east));
        y = (int) (((win_h)) * (y-north)/(south-north));

        /*
        x = (int) ((loc.x() * 100000) % win_w) - 50;
        y = (int) ((loc.y() * 100000) % win_h) - 50;
        if (x<50) x=50;
        if (y<50) y=50;
         */
        /*
        int x_i =0, y_i =0;
        int win_scale_x = win_w, win_scale_y = win_h;
        while (win_scale_x != 0)
        {
            x_i++;
            win_scale_x /=10;
        }
        while (win_scale_y != 0)
        {
            y_i++;
            win_scale_y /=10;
        }

         */
        System.out.println("ID:"+key+" x:"+x+" y:"+y);
    }
    public void add_edge(GUI_node neighbor,double w)
    {
        neighbors.put(neighbor,w);
    }
    public Set<GUI_node> get_neighbors ()
    {
        return neighbors.keySet();
    }
}
