package org.test.model;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by filipemiranda on 6/17/16.
 * Board of Cells representing the Game Of Life
 */
public class Board  {

    private final Cell[][] cells;

    private Board(Cell[][] cells) {
        this.cells = cells;
    }

    public static Board instanceOf(Cell[][] cells) {
        if(cells == null){
            throw new NullPointerException("cells cannot be null");
        }
        return new Board(cells);
    }

    public Cell[][] getCells() {
        return this.cells;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < cells.length; i++) {
            sb.append(Arrays.toString(cells[i]) + " \n");
        }
        return sb.toString().replace(",","");
    }
}
