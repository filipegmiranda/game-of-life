package org.test.rules;

import org.test.model.Board;
import org.test.model.Cell;

/**
 * Created by filipemiranda on 6/17/16.
 */
public interface RulesService {

    /**
     * Initiates the board
     * @return
     */
    Board init();

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
     * Function to be applied to teh cell
     */
    @FunctionalInterface
    interface Rule{
        Cell apply(Cell c, int nLiveNeib);
    }

}
