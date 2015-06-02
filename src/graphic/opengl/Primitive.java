package graphic.opengl;

/**
 * Created by feinte on 14/04/2015.
 * Abstraction of primitives
 *
 */
public abstract class Primitive {

   /* public float[] getVertices() {
        float adaptedVertices[] = getEntityVertices().clone();
        for (int i = 0; i < adaptedVertices.length; i++) {
            if (i % 4 == 0) adaptedVertices[i] += x;
            else if(i % 4 == 1) adaptedVertices[i] += y;
        }
        return adaptedVertices;
    }*/
    protected float[] vertices;
    protected byte[] indices;

    public abstract int getOpenGLDrawingMethod();
    public abstract float[] getEntityVertices(float x, float y);
    public abstract byte[] getIndices();

}
