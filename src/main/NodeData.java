package main;
import api.*;
import org.w3c.dom.Node;

import java.util.Comparator;

public class NodeData implements node_data {

    private static int keyCounter = 0;
    private int key;
    private String info = "";
    private geo_location location;
    private int tag;
    private double weight = 0;

    public NodeData() {
        key = keyCounter;
        keyCounter++;
    }

    public NodeData(int k)
    {
        key=k;
    }
    @Override
    public int getKey() {
        return key;
    }

    @Override
    public geo_location getLocation() {
        if (location == null) {location = new GeoLocation(1,2,3);;}
        return location;
    }

    @Override
    public void setLocation(geo_location p) {
        location = p;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public void setWeight(double w) {
        weight=w;
    }

    @Override
    public String getInfo() {
        return info;
    }

    @Override
    public void setInfo(String s) {
        info = s;
    }

    @Override
    public int getTag() {
        return tag;
    }

    @Override
    public void setTag(int t) {
        tag = t;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeData nodeData = (NodeData) o;
        if (key != nodeData.key) return false;
        if (tag != nodeData.tag) return false;
        if (Double.compare(nodeData.weight, weight) != 0) return false;
        if (nodeData.info != info) return false;
        return nodeData.location.equals(this.location);
    }
}
class node_data_comparator implements Comparator<node_data>
{
    @Override
    public int compare(node_data o1, node_data o2) {
        if (o1.getWeight() < o2.getWeight())
            return -1;
        if (o1.getWeight() > o2.getWeight())
            return 1;
        return 0;
    }
}
