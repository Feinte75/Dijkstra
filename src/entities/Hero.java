package entities;

import graphic.opengl.Primitive;
import logic.Army;

import java.util.ArrayList;

/**
 * Created by feinte on 02/07/2015.
 */
public class Hero extends PlayerControlledEntity {

    public Hero(Army army, float x, float y, ArrayList<Primitive> primitives) {
        super(x, y, army, primitives);
    }


}
