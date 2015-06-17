package logic.commands;

/**
 * Created by feinte on 05/06/2015.
 *
 */
public abstract class MouseCommand implements Cloneable {


    public MouseCommand(){

    }

    public abstract void execute(int xPos, int yPos);

    public MouseCommand clone(){
        try {
            return (MouseCommand) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
