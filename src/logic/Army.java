package logic;

import entities.EntityFactory;
import entities.EntityType;
import entities.Troop;
import entities.Village;
import graphic.opengl.Primitive;
import graphic.opengl.Square;
import graphic.opengl.Triangle;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by feinte on 14/01/2015.
 * Represents either a player or a bot
 * Stores units
 */
public class Army {

    private ArrayList<Troop> troops;
    private ArrayList<Primitive> troopsPrimitives;
    private ArrayList<Village> villages;
    private ArrayList<Primitive> villagePrimitives;

    private EntityFactory entityFactory;

    private Color color;

    public Army(Color color) {

        villages = new ArrayList<Village>(10);
        villagePrimitives = new ArrayList<Primitive>(2);
        villagePrimitives.add(new Triangle(20, 20));

        this.color = color;

        troops = new ArrayList<Troop>(50);
        troopsPrimitives = new ArrayList<Primitive>(1);
        troopsPrimitives.add(new Square(20));
        entityFactory.updateTemplate(EntityType.TROOP, troopsPrimitives);

        villagePrimitives = new ArrayList<Primitive>(1);
        villagePrimitives.add(new Triangle(20, 20));
        entityFactory.updateTemplate(EntityType.VILLAGE, villagePrimitives);


        entityFactory = EntityFactory.getEntityFactory();
    }

    public void buildVillage(float x, float y)
    {
        villages.add((Village) entityFactory.createPlayerControlledEntity(EntityType.VILLAGE, this, x, y));
    }

    public void spawnTroop(float x, float y)
    {
        troops.add((Troop) entityFactory.createPlayerControlledEntity(EntityType.TROOP, this, x, y));
    }

    public Color getColor(){
        return color;
    }

    public ArrayList<Troop> getTroops() {
        return troops;
    }

    public ArrayList<Village> getVillages() {
        return villages;
    }
}
