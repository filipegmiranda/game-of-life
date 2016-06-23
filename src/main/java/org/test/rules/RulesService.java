package org.test.rules;

import org.springframework.stereotype.Service;
import org.test.model.Board;
import org.test.model.Cell;

/**
 * Service interface to be used while applying rules to the Board of game of life
 */
public interface RulesService {

    /**
     * Initiates the board
     * @return
     */
    Board init();


    /**
     * Initializes anew Board, deleting the previous one
     * @param b
     * @return
     */
    Board init(Board b);

    /**
     * Goes to the next pahse of the game applying the rules to the cells, and persists to the Data Base
     * @return
     */
    Board nextGeneration(Rule rule);

    /**
     * Returns the Current State of the Board
     * @return
     */
    Board currentGeneration();

    /**
     * Deletes the board
     */
    void delete();

    /**
     * Function to be applied to the cell
     */
    @FunctionalInterface
    interface Rule{
        Cell apply(Cell c, int nLiveNeib);
    }

    /**
     * Classical Rule
     */
    Rule CLASS_RULE = (c,n) -> {
        if(c.getState() == Cell.State.LIVE && n < 2){
            return Cell.valueOf(Cell.State.DEAD);
        }
        if(c.getState() == Cell.State.LIVE && ( n == 2 || n == 3  )){
            return Cell.valueOf(Cell.State.LIVE);
        }
        if(c.getState() == Cell.State.LIVE && n > 3) {
            return Cell.valueOf(Cell.State.DEAD);
        }
        if (c.getState() == Cell.State.DEAD && n == 3) {
            return Cell.valueOf(Cell.State.LIVE);
        }
        return c;
    };

}
