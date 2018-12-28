package Wumpus;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.Percept;

public class Spel implements Agent {

    private boolean alive;
    @Override
    public Action execute(Percept percept) {
        return null;
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
