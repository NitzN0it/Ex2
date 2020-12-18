package main.GUI;

import api.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.HashMap;
import java.util.LinkedList;

public class Ex2_GUI extends JFrame {
    private int win_h = 1000;
    private int win_w = 2500;
    private int CIRCLE_SIZE = 20;
    private directed_weighted_graph graph;
    HashMap<Integer,GUI_node> nodes;
    public Ex2_GUI(directed_weighted_graph g) {
        graph=g;
        init_GUI();
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent ev) {
                win_h=ev.getComponent().getHeight();
                win_w=ev.getComponent().getWidth();
                super.componentResized(ev);
            }
        });
    }
    private void init_GUI() {
        this.setSize(win_w, win_h);
    }
    private void paint_graph(Graphics g)
    {
        nodes = new HashMap<>();
        for (node_data n : graph.getV())
        {
            GUI_node node = new GUI_node(n.getLocation(),win_h,win_w,n.getKey());
            nodes.put(node.key,node);

        }
        for (GUI_node n : nodes.values())
        {
            paint_node(n,g);
            for (edge_data e : graph.getE(n.key))
            {
                GUI_node dest = nodes.get(e.getDest());
                n.add_edge(dest,e.getWeight());
                g.drawLine(n.x+CIRCLE_SIZE/2,n.y+CIRCLE_SIZE/2,dest.x+CIRCLE_SIZE/2,dest.y+CIRCLE_SIZE/2);
            }
        }
        g.drawString("Total Nodes: "+String.valueOf(nodes.size()),10,50);
    }
    private void paint_node(GUI_node node, Graphics g)
    {
        g.setColor(Color.blue);
        g.drawOval(node.x, node.y,(int) CIRCLE_SIZE,(int) CIRCLE_SIZE);
        g.fillOval(node.x, node.y,(int) CIRCLE_SIZE,(int) CIRCLE_SIZE);
        g.drawString(String.valueOf(node.key),node.x,node.y);
    }
    public void paint(Graphics g)
    {
        paint_graph(g);
    }
}
