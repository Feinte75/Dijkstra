package entities;

import graphic.opengl.Primitive;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by feinte on 02/06/2015.
 *
 */
public class Tile extends Entity{

    public Tile(Color color, float x, float y, ArrayList<Primitive> primitives) {
        super(color, primitives);
        this.x = x;
        this.y = y;
        //initPrimitives(primitives);
    }

    @Override
    public void initPrimitives(ArrayList<Primitive> primitives) {

    }
}
