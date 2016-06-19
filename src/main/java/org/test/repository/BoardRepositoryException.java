package org.test.repository;

import org.test.model.Board;

/**
 * Wraps up exceptions that may happend while reading, saving or deletinf the {@link Board} object
 */
public class BoardRepositoryException extends RuntimeException{
    public BoardRepositoryException(String msg, Throwable cause){
        super(msg, cause);
    }
}
