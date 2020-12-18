package main;
import api.*;
public class GeoLocation implements geo_location{
    private double x,y,z;
    public GeoLocation(){}
    public GeoLocation(double x_, double y_, double z_)
    {
        x=x_;
        y=y_;
        z=z_;
    }
    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public double z() {
        return z;
    }

    @Override
    public double distance(geo_location g) {
        return Math.sqrt(Math.pow(x-g.x(),2) + Math.pow(y-g.y(),2) + Math.pow(z-g.z(),2));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeoLocation that = (GeoLocation) o;
        if (that.x() != x) return false;
        if (that.y() != y) return false;
        return that.z() == z;
    }

    @Override
    public String toString() {
        return x+","+y+","+z;
    }
}
