package entities;

import graphic.opengl.Primitive;
import logic.Army;

import java.util.ArrayList;

/**
 * Created by feinte on 14/01/2015.
 * Village entity
 */
public class Village extends PlayerControlledEntity {

    private int tick;
    private int troopSpawned = 0;
    public Village(Army army, float x, float y, ArrayList<Primitive> primitives){
        super(x, y, army, primitives);
    }

    public void update(){
        tick++;
        if (tick > 100) {
            if (troopSpawned < 10) {
                army.spawnTroop(x, y);
                troopSpawned++;
            }
            tick = 0;
        }
    }

    public Army getArmy() {
        return army;
    }


    public void setArmy(Army army) {
        this.army = army;
    }
}
