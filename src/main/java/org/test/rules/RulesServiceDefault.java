package org.test.rules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.test.model.Board;
import org.test.model.Cell;
import org.test.repository.BoardRepository;

/**
 * Created by filipemiranda on 6/17/16.
 */
@Service
public class RulesServiceDefault implements RulesService {

    private BoardRepository repository;

    @Autowired
    public RulesServiceDefault(BoardRepository repository) {
        this.repository = repository;
    }


    @Override
    public Board init() {
        Cell[][] cells = new Cell[10][10];
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                cells[i][j] = Cell.valueOf(Cell.State.DEAD);
            }
        }
        cells[1][1] = Cell.valueOf(Cell.State.LIVE);
        cells[2][1] = Cell.valueOf(Cell.State.LIVE);
        cells[3][1] = Cell.valueOf(Cell.State.LIVE);
        cells[4][1] = Cell.valueOf(Cell.State.LIVE);
        cells[5][1] = Cell.valueOf(Cell.State.LIVE);
        cells[6][1] = Cell.valueOf(Cell.State.LIVE);
        cells[4][6] = Cell.valueOf(Cell.State.LIVE);
        cells[4][7] = Cell.valueOf(Cell.State.LIVE);
        cells[4][8] = Cell.valueOf(Cell.State.LIVE);
        cells[4][9] = Cell.valueOf(Cell.State.LIVE);
        return repository.saveBoard(Board.instanceOf(cells));
    }

    @Override
    public Board init(Board b) {
        return repository.saveBoard(b);
    }

    @Override
    public Board nextGeneration(Rule rule) {
        Cell[][] cells = currentGeneration().getCells();
        Cell[][] nextCells = new Cell[cells.length][cells.length];
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                Cell c = cells[i][j];
                int nr = countAliveNbs(cells, i, j);
                nextCells[i][j] = rule.apply(c, nr);
            }
        }
        return repository.saveBoard(Board.instanceOf(nextCells));
    }

    @Override
    public Board currentGeneration() {
        return repository.readBoard();
    }


    @Override
    public void delete() {
        repository.delete();
    }

    private int countAliveNbs(Cell[][] cells, int x, int y) {
        int count = 0;
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if( i < 0 || j < 0 || i > cells.length -1 || j > cells.length -1) continue;
                if (cells[i][j].getState() == Cell.State.LIVE && (i != x || j != y)) {
                    count++;
                }
            }
        }
        return count;
    }



}

