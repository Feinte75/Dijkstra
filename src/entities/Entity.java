package entities;

import graphic.opengl.Primitive;
import graphic.opengl.renderer.Renderable;

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

    public Entity(Color color, ArrayList<Primitive> primitives){
        this.color = color;
        this.primitives = primitives;
    }

    public float[] getColorVector(){
        return color.getColorComponents(null);
    }

    public Color getColor(){
        return color;
    }

    public abstract void initPrimitives(ArrayList<Primitive> primitives);

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

    public ArrayList<Primitive> getPrimitives() {
        return primitives;
    }

}
