package org.test;

import org.springframework.stereotype.Component;
import org.test.model.Board;
import org.test.repository.BoardRepository;

/**
 * Created by filipemiranda on 6/19/16.
 */
//@Component
public class MockedCachedBoardRepository implements BoardRepository {
    private static Board b;

    @Override
    public Board saveBoard(Board board) {
        b = board;
        return board;
    }

    @Override
    public Board readBoard() {
        return b;
    }

    @Override
    public void delete() {
        b = null;
    }
}
