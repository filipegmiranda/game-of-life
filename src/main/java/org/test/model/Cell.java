package org.test.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by filipemiranda on 6/17/16.
 * - Any live cell with fewer than two live neighbours dies, as if caused by under-population.
 * - Any live cell with two or three live neighbours lives on to the next generation.
 * - Any live cell with more than three live neighbours dies, as if by over-population.
 * - Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
 */
public class Cell implements Serializable{

    private final static Cell LIVE_CELL = new Cell(State.LIVE);
    private final static Cell DEAD_CELL = new Cell(State.DEAD);

    private final State state;

    @JsonCreator
    private Cell(@JsonProperty("state") State state) {
        this.state = state;
    }

    public static Cell valueOf(State state) {
        if(state == null){
            throw new IllegalArgumentException("State cannot be null");
        }
        switch (state){
            case LIVE: return LIVE_CELL;
            case DEAD: return DEAD_CELL;
        }
        throw new AssertionError("Invalid State being used");
    }

    /**
     * Returns the State of the cell - {@link State}
     * @return
     */
    public State getState(){
        return this.state;
    }

    /**
     * State in which the cell is in
     */
    public enum State {
        DEAD,
        LIVE;

        @Override
        public String toString() {
            switch (this){
                case LIVE: return "  X  ";
                case DEAD: return "     ";
                default: throw new AssertionError("Invalid State, only DEAD and LIVE are supported");
            }
        }
    }

    @Override
    public String toString() {
        return ""+getState()+" | ";
    }
}
