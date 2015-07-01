package graphic.opengl;

import org.lwjgl.opengl.GL11;

/**
 * Created by feinte on 14/04/2015.
 * Reify a triangle
 */
public class Triangle extends Primitive {

    private final static int OPENGLDRAWINGMETHOD = GL11.GL_TRIANGLES;

    private final int[] indices = {
            // Left bottom triangle
            0, 1, 2
    };

    public Triangle(float width, float height){
        vertices = new float[]{
                0f , (height/2), 0f, 1f,
                (width/2), -(height/2), 0f, 1f,
                -(width/2), -(height/2), 0f, 1f
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
    public int getNbVertices() {
        return 3;
    }

    @Override
    public int[] getIndices() {
        return indices;
    }
}
