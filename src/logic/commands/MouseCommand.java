package logic.commands;

/**
 * Created by feinte on 05/06/2015.
 *
 */
public abstract class MouseCommand {


    public MouseCommand(){

    }

    public abstract void execute(int xPos, int yPos);
}
