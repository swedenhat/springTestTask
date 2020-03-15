package spring;

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

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public void index(@RequestBody Map<String, String> request, HttpServletResponse response) throws IOException {
		Objects.requireNonNull(request);
		Objects.requireNonNull(response);

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();

		String value = request.get("request");
		if (value == null) {
			configureResponse(response, writer, HttpServletResponse.SC_BAD_GATEWAY,
					"wrong json format: \"request\" expected\n");
			return;
		}
		List<Integer> list = null;

		try {
			list = Arrays.stream(value.split(" ")).filter(s -> !s.trim().equals("")).map(s -> Integer.valueOf(s))
					.collect(Collectors.toList());
		} catch (NumberFormatException ex) {
			configureResponse(response, writer, HttpServletResponse.SC_BAD_GATEWAY, "в запросе не все числа\n");
			return;
		}

		String result = list != null ? list.stream().map(i -> ++i + "").reduce((s1, s2) -> s1 + " " + s2).orElse("")
				: "";
		configureResponse(response, writer, HttpServletResponse.SC_OK, makeJson("response", result));
	}

	/**
	 * Makes a json string from key and value
	 * 
	 * @param key
	 * @param value
	 * @return {"key":"value"}
	 */
	static String makeJson(String key, String value) {
		return new StringBuilder("{\"").append(key).append("\": \"").append(value).append("\"}").toString();
	}

	/**
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
