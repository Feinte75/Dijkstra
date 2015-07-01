package entities;

import graphic.opengl.Primitive;
import graphic.opengl.Square;
import logic.Army;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by feinte on 30/06/2015.
 * Factory pattern. Create entities from this Singleton Class
 * Accessible from everywhere, simplifies entity creation process
 */
public class EntityFactory {

    private static EntityFactory entityFactory;
    private Map<Integer, ArrayList<Primitive>> entityTemplates;
    private int templateIndex = -1;

    private EntityFactory() {
        entityTemplates = new HashMap<Integer, ArrayList<Primitive>>(3);

        ArrayList<Primitive> primitives = new ArrayList<Primitive>(1);
        primitives.add(new Square(24));
        entityTemplates.put(EntityType.TILE, primitives);

    }

    public static EntityFactory getEntityFactory() {
        if (entityFactory == null)
            entityFactory = new EntityFactory();

        return entityFactory;
    }

    public int addTemplate(ArrayList<Primitive> primitives) {
        templateIndex++;
        entityTemplates.put(templateIndex, primitives);
        return templateIndex;
    }

    public void removeTemplate(int templateID) {
        entityTemplates.remove(templateID);
    }

    public void updateTemplate(int templateID, ArrayList<Primitive> primitives) {
        entityTemplates.put(templateID, primitives);
    }

    public Entity createEntity(int templateID, Color color, float x, float y) {

        if (templateID == EntityType.TILE)
            return new Tile(color, x, y, (ArrayList<Primitive>) entityTemplates.get(templateID).clone());
        else
            return null;
    }

    public PlayerControlledEntity createPlayerControlledEntity(int templateID, Army army, float x, float y) {

        if (templateID == EntityType.TROOP)
            return new Troop(army, x, y, (ArrayList<Primitive>) entityTemplates.get(templateID).clone());
        else if (templateID == EntityType.VILLAGE)
            return new Village(army, x, y, (ArrayList<Primitive>) entityTemplates.get(templateID).clone());
        else
            return null;
    }
}
