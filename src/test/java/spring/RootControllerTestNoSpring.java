package spring;

import static spring.Constants.ERROR_NULL_REQUEST;
import static spring.Constants.ERROR_NULL_RESPONSE;
import static spring.RootController.makeJson;

import java.io.IOException;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

class RootControllerTestNoSpring {

	@Test
	void testWrongArguments1() throws IOException {
		try {
			new RootController().getIncrementedNumbers(null, null);
		} catch (Exception ex) {
			assert (ex instanceof NullPointerException);
			assert (ex.getMessage().equals(ERROR_NULL_REQUEST));
		}
	}

	@Test
	void testWrongArguments2() throws IOException {
		try {
			new RootController().getIncrementedNumbers(new HashMap<String, String>(), null);
		} catch (Exception ex) {
			assert (ex instanceof NullPointerException);
			assert (ex.getMessage().equals(ERROR_NULL_RESPONSE));
		}
	}

	@Test
	void testMakeJson() {
		String key = "key";
		String value = "value";
		String result = "{\"key\":\"value\"}";
		assert (makeJson(key, value).equals(result));
	}
}
