package org.test.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.test.model.Board;
import org.test.rules.RulesService;
import static org.test.rules.RulesService.CLASS_RULE;

/**
 * Created by filipemiranda on 6/17/16.
 *
 * Controller to Publish the Board State
 */
@RestController
@RequestMapping("/game-of-life")
public class GameController {

    @Autowired
    private RulesService service;

    /**
     * Deletes previous board and initializes a new one according to the Cells given
     * @param board
     * @return
     */
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "/init")
    public Board init(@RequestBody Board board) {
        service.delete();
        return service.init(board);
    }

    /**
     * Inits the Board with a default hardcoded pattern
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/init")
    public @ResponseBody Board game() {
        return service.init();
    }

    /**
     * Deletes the running Board
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.DELETE, value = "/remove")
    public void remove() {
        service.delete();
    }

    /**
     * Requests the next generation of life
     * @return
     */
    @RequestMapping(method=RequestMethod.GET, value = "/next")
    public Board next(){
        return service.nextGeneration(CLASS_RULE);
    }

    /**
     * retrieves the current generation
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/current")
    public Board current() {
        return service.currentGeneration();
    }


}
