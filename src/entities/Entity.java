package entities;

import graphic.opengl.Primitive;
import graphic.opengl.renderer.Renderable;
import utils.Utils;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Glenn on 18/01/2015.
 * Abstract representation of entities
 */
public abstract class Entity implements Renderable {

    protected ArrayList<Primitive> primitives;
    protected float x = 0;
    protected float y = 0;
    protected Color color;
    protected float[] colorArray;

    public Entity(float x, float y, Color color, ArrayList<Primitive> primitives) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.primitives = primitives;
        colorArray = Utils.getColorArrayFromColor(color, getNbVertices());
    }

    public float[] getColorArray() {
        return colorArray;
    }

    @Override
    public int getNbVertices() {
        return primitives.get(0).getNbVertices();
    }

    @Override
    public float[] getVertices() {
        return primitives.get(0).getEntityVertices(x, y);
    }

    @Override
    public int[] getIndices() {
        return primitives.get(0).getIndices();
    }

    public Color getColor(){
        return color;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void deltaPosition(float dx, float dy) {
        x += dx;
        y += dy;
    }

    public ArrayList<Primitive> getPrimitives() {
        return primitives;
    }

}
