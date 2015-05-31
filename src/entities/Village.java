package entities;

import opengl.Primitive;
import opengl.Triangle;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

/**
 * Created by feinte on 14/01/2015.
 * Village entity
 */
public class Village extends Entity {

    private int tick;

    public Village(Army army, float x, float y, ArrayList<Primitive> primitives){
        super();
        this.army = army;
        this.x = x;
        this.y = y;
        initPrimitives(primitives);
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
}
