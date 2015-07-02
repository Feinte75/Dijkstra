package logic.commands.game;

import entities.PlayerControlledEntity;
import logic.commands.PlayerControlledEntityAction;

/**
 * Created by feinte on 02/07/2015.
 * Move a PlayerControlledEntity
 */
public class MovementAction implements PlayerControlledEntityAction {

    private float dx, dy;

    public MovementAction(float dx, float dy) {
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public void execute(PlayerControlledEntity entity) {
        entity.deltaPosition(dx, dy);
    }
}
