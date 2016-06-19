package org.test.repository;

import org.test.model.Board;

/**
 * Created by filipemiranda on 6/17/16.
 */
public interface BoardRepository {
    Board readBoard();
    Board saveBoard(Board board);
}
