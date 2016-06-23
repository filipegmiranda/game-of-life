package org.test.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by filipemiranda on 6/17/16.
 * Board of Cells representing the Game Of Life
 */
public class Board implements Serializable {

    private final Cell[][] cells;

    @JsonCreator
    private Board(@JsonProperty("cells") Cell[][] cells) {
        this.cells = cells;
    }

    public static Board instanceOf(Cell[][] cells) {
        if(cells == null){
            throw new IllegalArgumentException("cells cannot be null");
        }
        if(cells.length < 10 || cells[0].length < 10){
            throw new IllegalArgumentException("Minimum size of board must be 10");
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
