package entities;

import opengl.Primitive;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Glenn on 18/01/2015.
 * Abstract representation of entities
 */
public abstract class Entity {

    protected Army army;
    protected ArrayList<Primitive> primitives;
    protected float x = 0;
    protected float y = 0;

    public Entity(){
    }

    public Army getArmy() {
        return army;
    }

    public abstract void initPrimitives(ArrayList<Primitive> primitives);

    public void setArmy(Army army) {
        this.army = army;
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

    public ArrayList<Primitive> getPrimitives() {
        return primitives;
    }

}
