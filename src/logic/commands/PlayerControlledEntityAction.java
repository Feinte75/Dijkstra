package logic.commands;

import entities.PlayerControlledEntity;

/**
 * Created by feinte on 02/07/2015.
 * Interface for action destined to PlayerControlledEntity
 */
public interface PlayerControlledEntityAction {

    void execute(PlayerControlledEntity entity);
}
