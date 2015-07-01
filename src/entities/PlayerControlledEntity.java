package entities;

import graphic.opengl.Primitive;
import logic.Army;

import java.util.ArrayList;

/**
 * Created by feinte on 30/06/2015.
 * Entity controlled by an Army
 */
public abstract class PlayerControlledEntity extends Entity {

    protected Army army;

    public PlayerControlledEntity() {

    }

    public PlayerControlledEntity(float x, float y, Army army, ArrayList<Primitive> primitives) {
        super(x, y, army.getColor(), primitives);
        this.army = army;
    }

    public Army getArmy() {
        return army;
    }

    public void setArmy(Army army) {
        this.army = army;
    }
}
