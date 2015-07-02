package logic;

import entities.EntityFactory;
import entities.EntityType;
import entities.Hero;
import graphic.opengl.Primitive;
import graphic.opengl.Triangle;

import java.util.ArrayList;

/**
 * Created by feinte on 03/06/2015.
 * Reify the Player
 */
public class Player {

    private Army army;
    private Hero hero;
    private EntityFactory factory;

    public Player(){

        factory = EntityFactory.getEntityFactory();
        ArrayList<Primitive> primitives = new ArrayList<Primitive>(1);
        primitives.add(new Triangle(48, 48));
        factory.updateTemplate(EntityType.HERO, primitives);
    }

    public Hero getHero() {
        return hero;
    }

    public void chooseArmy(Army army){
        this.army = army;
    }

    public void chooseHero(Hero hero) {
        this.hero = (Hero) factory.createPlayerControlledEntity(EntityType.HERO, army, 150f, 150f);
    }
}
