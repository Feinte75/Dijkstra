package graphic.opengl;

import org.lwjgl.opengl.GL11;

/**
 * Created by feinte on 14/04/2015.
 * Reify a line
 */
public class Line extends Primitive {

    private final static int OPENGLDRAWINGMETHOD = GL11.GL_LINES;

    private final byte[] indices = {
        0, 1
    };

    public Line(float x1, float y1, float x2, float y2){
        vertices = new float[]{
                x1, y1, 0f, 1f,
                x2, y2, 0f, 1f
        };
    }

    @Override
    public int getOpenGLDrawingMethod() {
        return OPENGLDRAWINGMETHOD;
    }

    @Override
    public float[] getEntityVertices(float x, float y) {
        return vertices;
    }

    @Override
    public byte[] getIndices() {
        return indices;
    }
}
