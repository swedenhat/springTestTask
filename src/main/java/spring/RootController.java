package spring;

import static spring.Constants.ERROR_NOT_ALL_NUMBERS;
import static spring.Constants.ERROR_NULL_REQUEST;
import static spring.Constants.ERROR_NULL_RESPONSE;
import static spring.Constants.ERROR_WRONG_JSON;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

	/**
	 * Gets a json with numbers, puts incremented numbers in
	 * {@link HttpServletResponse}.
	 *
	 * @param request  a map with json key-values
	 * @param response loaded with answer (502 if numbers weren't correctly parsed)
	 * @throws IOException
	 */
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public void getIncrementedNumbers(@RequestBody Map<String, String> request, HttpServletResponse response)
			throws IOException {
		Objects.requireNonNull(request, ERROR_NULL_REQUEST);
		Objects.requireNonNull(response, ERROR_NULL_RESPONSE);

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();

		String value = request.get(Constants.REQUEST);
		if (value == null) {
			configureResponse(response, writer, HttpServletResponse.SC_BAD_GATEWAY, ERROR_WRONG_JSON);
			return;
		}

		List<Integer> list = null;
		try {
			list = Arrays.stream(value.split(" ")).filter(s -> !s.trim().equals("")).map(s -> Integer.valueOf(s))
					.collect(Collectors.toList());
		} catch (NumberFormatException ex) {
			configureResponse(response, writer, HttpServletResponse.SC_BAD_GATEWAY, ERROR_NOT_ALL_NUMBERS);
			return;
		}

		String result = list.stream().map(i -> ++i + " ").reduce((s1, s2) -> s1 + s2).orElse("").trim();
		configureResponse(response, writer, HttpServletResponse.SC_OK, makeJson(Constants.RESPONSE, result));
	}

	/**
	 * Makes a json string from key and value
	 *
	 * @param key
	 * @param value
	 * @return {"key":"value"}
	 */
	static String makeJson(String key, String value) {
		return new StringBuilder("{\"").append(key).append("\":\"").append(value).append("\"}").toString();
	}

	/**
	 * Writes a status and message to {@link HttpServletResponse}. Just for
	 * convenience - to reduce code.
	 *
	 * @param response
	 * @param writer
	 * @param status
	 * @param message
	 */
	static void configureResponse(HttpServletResponse response, PrintWriter writer, int status, String message) {
		response.setStatus(status);
		writer.write(message);
		writer.flush();
	}

}
