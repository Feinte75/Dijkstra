package graphic.opengl;

import graphic.opengl.Primitive;
import org.lwjgl.opengl.GL11;

/**
 * Created by feinte on 14/04/2015.
 * Reify a square
 */
public class Square extends Primitive {

    public final static int OPENGLDRAWINGMETHOD = GL11.GL_TRIANGLES;

    private final byte[] indices = {
            // Left bottom triangle
            0, 1, 2,
            // Right top triangle
            2, 3, 0
    };

    public Square(float width){
        vertices = new float[]{
                -(width/2), (width/2), 0f, 1f,
                -(width/2), -(width/2), 0f, 1f,
                (width/2), -(width/2), 0f, 1f,
                (width/2), (width/2), 0f, 1f
        };
    }

    @Override
    public int getOpenGLDrawingMethod() {
        return OPENGLDRAWINGMETHOD;
    }

    @Override
    public float[] getEntityVertices(float x, float y) {

        float adaptedVertices[] = vertices.clone();
        for (int i = 0; i < adaptedVertices.length; i++) {
            if (i % 4 == 0) adaptedVertices[i] += x;
            else if(i % 4 == 1) adaptedVertices[i] += y;
        }
        return adaptedVertices;
    }

    @Override
    public byte[] getIndices() {
        return indices;
    }
}
