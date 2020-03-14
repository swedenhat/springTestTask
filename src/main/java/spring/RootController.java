package spring;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ResponseBody
	public void index(@RequestBody Map<String, String> request, HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		String value = request.get("request");
		List<Integer> list = null;

		try {
			list = Arrays.stream(value.split(" ")).map(s -> Integer.valueOf(s)).collect(Collectors.toList());
		} catch (NumberFormatException ex) {
			response.setStatus(502);
			PrintWriter out = response.getWriter();
			out.write("bad, very bad!");
			out.flush();
			return;
		}

		String result = list != null ? list.stream().map(i -> ++i + "").reduce((s1, s2) -> s1 + " " + s2).orElse("")
				: "";
		response.setStatus(200);
		PrintWriter out = response.getWriter();
		out.write(makeJson("response", result));
		out.flush();
	}

	static String makeJson(String key, String value) {
		return new StringBuilder("{\"").append(key).append("\": \"").append(value).append("\"}").toString();
	}

}
