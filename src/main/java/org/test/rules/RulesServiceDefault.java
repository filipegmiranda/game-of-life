package org.test.rules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.test.model.Board;
import org.test.model.Cell;
import org.test.repository.BoardRepository;

/**
 * Created by filipemiranda on 6/17/16.
 */
@Component
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

        return repository.saveBoard(Board.instanceOf(cells));
    }

    @Override
    public Board nextGeneration(Rule rule) {
        Cell[][] cells = currentGeneration().getCells();
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                Cell c = cells[i][j];
                int nr = countAliveNbs(cells, i, j);
                cells[i][j] = rule.apply(c, nr);
            }
        }
        return repository.saveBoard(Board.instanceOf(cells));
    }

    @Override
    public Board currentGeneration() {
        return repository.readBoard();
    }


    private int countAliveNbs(Cell[][] cells, int x, int y) {
        int count = 0;
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (cells[i][j].getState() == Cell.State.LIVE && (i != x || j != y)) {
                    count++;
                }

            }
        }
        return count;
    }

}

