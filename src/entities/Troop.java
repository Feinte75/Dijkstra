package entities;

import graphic.opengl.Primitive;
import logic.Army;

import java.util.ArrayList;

/**
 * Created by feinte on 23/12/2014.
 * Troop entity
 */
public class Troop extends PlayerControlledEntity {

    public Troop(Army army, float x, float y, ArrayList<Primitive> primitives){
        super(x, y, army, primitives);
    }

    public void move(float dx, float dy){
        this.x += dx;
        this.y += dy;
    }
}
