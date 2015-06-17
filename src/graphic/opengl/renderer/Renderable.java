package graphic.opengl.renderer;

import graphic.opengl.Primitive;

import java.util.ArrayList;

/**
 * Created by feinte on 03/06/2015.
 * Interface for objects who can be rendered
 */
public interface Renderable {

    ArrayList<Primitive> getPrimitives();
    float[] getColorVector();
}
