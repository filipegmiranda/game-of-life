package org.test.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.test.model.Board;
import org.test.model.Cell;
import static org.test.model.Cell.State;
import org.test.rules.RulesService;

/**
 * Created by filipemiranda on 6/17/16.
 *
 * Controller to Publish the Board State
 */
@RestController
@RequestMapping("/game")
public class GameController {

    @Autowired
    private RulesService service;

    @RequestMapping(method = RequestMethod.GET, value = "/init")
    public @ResponseBody Board game() {
        //Gives an initial State and persists it in the Data Base, overriding any previous state
        return service.init();
    }

    @RequestMapping(method=RequestMethod.GET, value = "/next")
    public Board next(){
        return service.nextGeneration((c,n)-> {
            if (c.getState() == State.LIVE && n >= 2 && n <= 3) {
                return Cell.valueOf(State.LIVE);
            }
            if (c.getState() == Cell.State.DEAD && n == 3) {
                return Cell.valueOf(State.LIVE);
            }
            return Cell.valueOf(State.DEAD);
        });
    }

    @RequestMapping(method = RequestMethod.GET, value = "/current")
    public Board current() {
        return service.currentGeneration();
    }


}
