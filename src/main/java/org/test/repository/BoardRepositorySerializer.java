package org.test.repository;

import org.springframework.stereotype.Component;
import org.test.model.Board;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Default implementation which allows the user of the game to save th current state to disk, in home user folder
 * needs permission to write to the file
 */
@Component
public class BoardRepositorySerializer implements BoardRepository {

    private static final String USER_HOME = System.getProperty("user.home");

    @Override
    public Board saveBoard(Board board) throws BoardRepositoryException {
        FileOutputStream fos = null;
        try {
            fos =  new FileOutputStream(Paths.get(USER_HOME + "/board").toFile());
        } catch (IOException e ){
            throw new BoardRepositoryException("Exception while writing to dir, check permissions and if directory exists", e);
        }

        try (ObjectOutput o = new ObjectOutputStream(fos)){
            o.writeObject(board);
        } catch (IOException e){
            throw new BoardRepositoryException("Exception while writing to dir", e);
        }
        return board;
    }

    @Override
    public Board readBoard() throws BoardRepositoryException {
        try(FileInputStream fis = new FileInputStream(Paths.get(USER_HOME + "/board").toFile());
            ObjectInput o = new ObjectInputStream(fis)){
            try {
                return (Board) o.readObject();
            } catch (ClassNotFoundException | ClassCastException e) {
                throw new BoardRepositoryException("Exception while reading object Board", e);
            }
        } catch (IOException e){
            throw new BoardRepositoryException("Failed to read Board with IO error",e);
        }
    }

    @Override
    public void delete() throws BoardRepositoryException  {
        try {
            Files.deleteIfExists(Paths.get(USER_HOME + "/board"));
        } catch (IOException e) {
            throw new BoardRepositoryException("Error happened while deleting the Board", e);
        }
    }

}