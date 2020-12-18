package main;
import api.*;
public class EdgeData implements edge_data{
    private int src,dest,tag;
    private double weight;
    String info;

    public EdgeData(int src_,int dest_,double w)
    {
        src = src_;
        dest = dest_;
        weight = w;
    }
    @Override
    public int getSrc() {
        return src;
    }

    @Override
    public int getDest() {
        return dest;
    }

    @Override
    public double getWeight() {
        return weight;
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
        tag=t;
    }
}
