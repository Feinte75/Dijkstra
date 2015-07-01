package entities;

import graphic.opengl.Primitive;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by feinte on 02/06/2015.
 *
 */
public class Tile extends Entity{

    public Tile() {

    }

    public Tile(Color color, float x, float y, ArrayList<Primitive> primitives) {
        super(x, y, color, primitives);
    }
}
