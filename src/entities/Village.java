package entities;

import graphic.opengl.Primitive;
import logic.Army;

import java.util.ArrayList;

/**
 * Created by feinte on 14/01/2015.
 * Village entity
 */
public class Village extends Entity {

    private Army army;
    private int tick;

    public Village(Army army, float x, float y, ArrayList<Primitive> primitives){
        super(army.getColor(), primitives);
        this.army = army;
        this.x = x;
        this.y = y;
        //initPrimitives(primitives);
    }

    public void update(){
        tick++;
        if(tick > 10){
            army.spawnTroop(x, y);
            tick = 0;
        }
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
