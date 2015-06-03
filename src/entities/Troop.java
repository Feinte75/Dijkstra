package entities;

import graphic.opengl.Primitive;
import logic.Army;

import java.util.ArrayList;

/**
 * Created by feinte on 23/12/2014.
 * Troop entity
 */
public class Troop extends Entity {

    private Army army;

    public Troop(Army army, float x, float y, ArrayList<Primitive> primitives){
        super(army.getColor(), primitives);
        this.army = army;
        this.x = x;
        this.y = y;
        //initPrimitives(primitives);
    }

    public void move(float dx, float dy){
        this.x += dx;
        this.y += dy;
    }

    @Override
    public void initPrimitives(ArrayList<Primitive> primitives) {
        this.primitives = primitives;
    }

    public Army getArmy() {
        return army;
    }

    public void setArmy(Army army) {
        this.army = army;
    }

}
