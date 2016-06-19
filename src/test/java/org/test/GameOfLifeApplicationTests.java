package org.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.*;
import org.junit.internal.MethodSorter;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.test.model.Board;
import org.test.model.Cell;
import org.test.rules.RulesService;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.test.model.Cell.State.DEAD;
import static org.test.model.Cell.State.LIVE;
import static org.test.rules.RulesService.CLASS_RULE;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@FixMethodOrder(MethodSorters.JVM)
public class GameOfLifeApplicationTests {

	private static final String USER_HOME = System.getProperty("user.home");

	private MockMvc mockMvc;

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(),
			Charset.forName("utf8"));

	@Autowired
	private WebApplicationContext webApplicationContext;

	private HttpMessageConverter mappingJackson2HttpMessageConverter;

	@Autowired
	private RulesService service;


	@Autowired
	void setConverters(HttpMessageConverter<?>[] converters) {
		this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
				hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();
		Assert.assertNotNull("the JSON message converter must not be null",
				this.mappingJackson2HttpMessageConverter);
	}

	@Before
	public void setUp() throws Exception{
		File f = new File(USER_HOME);
		if(!f.canRead()){
			throw new RuntimeException("Cannot read from local user home folder. Impossible to write and read Boards");
		}
		if(!f.canWrite()){
			throw new RuntimeException("Cannot write from local user home folder. Impossible to write and read Boards");
		}
		this.mockMvc = webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void initBoard3LiveCells() throws Exception{
		Files.deleteIfExists(Paths.get(USER_HOME + "/board"));
		Cell[][] cells = new Cell[10][10];
		for(int i = 0; i < cells.length; i++){
			for (int j = 0; j < cells[i].length; j++){
				cells[i][j] = Cell.valueOf(DEAD);
			}
		}
		cells[1][1] = Cell.valueOf(LIVE);
		cells[1][2] = Cell.valueOf(LIVE);
		cells[1][3] = Cell.valueOf(LIVE);
		String jsonBoard = objectToJson(Board.instanceOf(cells));
		this.mockMvc.perform(post("/game-of-life/init")
				.contentType(contentType)
				.content(jsonBoard))
				.andExpect(status().isCreated());
	}

	@Test
	public void currentGeneration3LiveCells() throws Exception{
		String json = this.mockMvc.perform(get("/game-of-life/current"))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		Board b = jsonToObject(json, Board.class);
		System.out.println(b);
		Cell[][] cells = b.getCells();
		Assert.assertTrue(cells[1][1].getState() == LIVE);
		Assert.assertTrue(cells[1][2].getState() == LIVE);
		Assert.assertTrue(cells[1][3].getState() == LIVE);
	}

	@Test
	public void nextGeneration3LiveCells() throws Exception{
		String json = this.mockMvc.perform(get("/game-of-life/next"))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		Board b = jsonToObject(json, Board.class);
		System.out.println(b);
		Cell[][] cells = b.getCells();
		Assert.assertTrue(cells[0][2].getState() == LIVE);
		Assert.assertTrue(cells[1][2].getState() == LIVE);
		Assert.assertTrue(cells[2][2].getState() == LIVE);
	}

	@Test
	public void showBoard() throws Exception{
		service.init();
		System.out.println(service.currentGeneration());
		int generations = 10;
		while(generations > 0){
			Board b = service.nextGeneration(CLASS_RULE);
			System.out.println(b);
			Thread.sleep(200);
			generations --;
		}
	}

	@Test
	public void deleteBoardThroughEndpoint() throws Exception{
		this.mockMvc.perform(delete("/game-of-life/remove")).andExpect(status().isOk());
		Assert.assertTrue(Files.notExists(Paths.get(USER_HOME + "board")));
	}

	private <T> T jsonToObject(String json, Class<T> clazz){
		ObjectMapper oM = new ObjectMapper();
		try {
			return oM.readValue(json, clazz);
		} catch (Exception e){
			throw new RuntimeException("Error deserializing json");
		}
	}

	private String objectToJson(Object o) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.mappingJackson2HttpMessageConverter.write(
				o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}

	@AfterClass
	public static void deleteBoard() throws Exception{
		Files.deleteIfExists(Paths.get(USER_HOME + "/board"));
	}

}
