/**
 * Created by Glenn on 18/01/2015.
 */
public abstract class Entity {

    protected Army army;
    protected float x = 0;
    protected float y = 0;

    public float[] getVertices() {
        float adaptedVertices[] = getEntityVertices().clone();
        for (int i = 0; i < adaptedVertices.length; i++) {
            if (i % 4 == 0) adaptedVertices[i] += x;
            else if(i % 4 == 1) adaptedVertices[i] += y;
        }
        return adaptedVertices;
    }

    public byte[] getIndices() {
        return getEntityIndices();
    }

    public abstract int getOpenGLDrawingMethod();
    public abstract float[] getEntityVertices();
    public abstract byte[] getEntityIndices();

    public Army getArmy() {
        return army;
    }

    public void setArmy(Army army) {
        this.army = army;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
