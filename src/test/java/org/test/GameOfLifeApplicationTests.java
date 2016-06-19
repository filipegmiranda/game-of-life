package org.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;
import org.test.model.Board;
import org.test.model.Cell;
import org.test.repository.BoardRepository;
import org.test.rules.RulesService;
import org.test.rules.RulesServiceDefault;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GameOfLifeApplicationTests {

	@Autowired
	private RulesService service;

	@Before
	public void setUp(){
		service.init();
	}

	@Test
	public void showBoard() throws Exception{
		System.out.println(service.currentGeneration());
		while(true){
			Board b = service.nextGeneration((c,n) -> {
				if(c.getState() == Cell.State.LIVE && n < 2){
					return Cell.valueOf(Cell.State.DEAD);
				}
				if(c.getState() == Cell.State.LIVE && ( n == 2 || n == 3  )){
					return Cell.valueOf(Cell.State.LIVE);
				}
				if(c.getState() == Cell.State.LIVE && n > 3) {
					return Cell.valueOf(Cell.State.DEAD);
				}
				if (c.getState() == Cell.State.DEAD && n == 3) {
					return Cell.valueOf(Cell.State.LIVE);
				}
				return c;
			});
			System.out.println(b);
			Thread.sleep(1000);
		}
	}


}
