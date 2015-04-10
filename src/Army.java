import org.lwjgl.BufferUtils;

import java.awt.*;
import java.nio.FloatBuffer;
import java.util.ArrayList;

/**
 * Created by feinte on 14/01/2015.
 */
public class Army {

    private ArrayList<Troop> troops;
    private ArrayList<Village> villages;
    private FloatBuffer colorBuffer;
    private final float colorValues[];

    public Army(Color color) {
        troops = new ArrayList<Troop>(50);
        villages = new ArrayList<Village>(10);
        colorBuffer = BufferUtils.createFloatBuffer(4);
        colorValues = color.getRGBComponents(null);
        System.out.println(colorValues[0] + " " + colorValues[1] + " " + colorValues[2] + " " + colorValues[3]);
        colorBuffer.put(colorValues);
    }

    public void buildVillage(float x, float y)
    {
        villages.add(new Village(this, x, y));
    }

    public void spawnTroop(float x, float y)
    {
        troops.add(new Troop(this, x, y));
    }

    public float[] getColor() {
        /*colorBuffer.rewind();
        return colorBuffer.duplicate();*/
        System.out.println("Color :"+ colorValues[0] + " " + colorValues[1] + " " + colorValues[2] + " " + colorValues[3] );
        return colorValues;
    }


    public ArrayList<Troop> getTroops() {
        return troops;
    }

    public ArrayList<Village> getVillages() {
        return villages;
    }
}
