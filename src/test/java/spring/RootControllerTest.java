package spring;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static spring.Constants.ERROR_NOT_ALL_NUMBERS;
import static spring.Constants.ERROR_WRONG_JSON;
import static spring.RootController.makeJson;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(RootController.class)
class RootControllerTest {

	@Autowired
	private MockMvc mvc;

	@Test
	void testCorrectResult() throws Exception {
		mvc.perform(post("/").contentType(MediaType.APPLICATION_JSON).content(makeJson(Constants.REQUEST, "12 17 85")))
				.andExpect(status().isOk()).andExpect(jsonPath(Constants.JP_RESPONSE).value("13 18 86"));
	}

	@Test
	void testWrongRequestNumbers() throws Exception {
		mvc.perform(post("/").contentType(MediaType.APPLICATION_JSON).content(makeJson(Constants.REQUEST, "12d 17 85")))
				.andExpect(status().is5xxServerError()).andExpect(content().string(ERROR_NOT_ALL_NUMBERS));
	}

	@Test
	void testWrongJson() throws Exception {
		mvc.perform(post("/").contentType(MediaType.APPLICATION_JSON).content(makeJson("smth", "12d 17 85")))
				.andExpect(status().is5xxServerError()).andExpect(content().string(ERROR_WRONG_JSON));
	}

	@Test
	void testEmptyJson() throws Exception {
		mvc.perform(post("/").contentType(MediaType.APPLICATION_JSON).content(makeJson(Constants.REQUEST, "")))
				.andExpect(status().isOk()).andExpect(jsonPath(Constants.JP_RESPONSE).value(""));
	}

	@Test
	void testCorrectResult100Range() throws Exception {
		List<Integer> initial = IntStream.range(-100, 100).mapToObj(i -> Integer.valueOf(i))
				.collect(Collectors.toList());
		String reqBody = initial.stream().map(i -> i + " ").reduce((s1, s2) -> s1 + s2).orElse("").trim();
		String resBody = initial.stream().map(i -> ++i + " ").reduce((s1, s2) -> s1 + s2).orElse("").trim();

		mvc.perform(post("/").contentType(MediaType.APPLICATION_JSON).content(makeJson(Constants.REQUEST, reqBody)))
				.andExpect(status().isOk()).andExpect(jsonPath(Constants.JP_RESPONSE).value(resBody));
	}

	@Test
	void testCorrectResult1000Range() throws Exception {
		List<Integer> initial = IntStream.range(-1000, 1000).mapToObj(i -> Integer.valueOf(i))
				.collect(Collectors.toList());
		String reqBody = initial.stream().map(i -> i + " ").reduce((s1, s2) -> s1 + s2).orElse("").trim();
		String resBody = initial.stream().map(i -> ++i + " ").reduce((s1, s2) -> s1 + s2).orElse("").trim();

		mvc.perform(post("/").contentType(MediaType.APPLICATION_JSON).content(makeJson(Constants.REQUEST, reqBody)))
				.andExpect(status().isOk()).andExpect(jsonPath(Constants.JP_RESPONSE).value(resBody));
	}
}
