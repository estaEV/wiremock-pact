package org.organization;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import com.atlassian.ta.wiremockpactgenerator.WireMockPactGenerator;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

import au.com.dius.pact.consumer.junit5.PactTestFor;
import lombok.SneakyThrows;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@PactTestFor(providerName = "CoursesCatalogue")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WiremockConsumerTest {

	@LocalServerPort
	public int PORT;

	private static final String HOST = "0.0.0.0";
	private static final String ENDPOINT = "allCourseDetails";
	private String URL_TO_HIT = "http://" + HOST + ":" + PORT + "/" + ENDPOINT;
	private WireMockServer wm1 = new WireMockServer(PORT);;

	private void createMeAServer(Map<String, String> serverProps) {
		wm1.stubFor(WireMock.get(urlEqualTo("/" +  serverProps.get("testUrl")))
				.willReturn(aResponse()
						.withStatus(Integer.parseInt(serverProps.get("statusCode")))
//						.withHeader("Content-Type", "text/plain")
						.withBody(serverProps.get("body"))
				));
		wm1.addMockServiceRequestListener(
				WireMockPactGenerator
						.builder(serverProps.get("consumerName"), serverProps.get("providerName"))
						.build()
		);

		wm1.start();
	}

	@SneakyThrows
	private Response sendAReq() {
		String baseurl = wm1.baseUrl();
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
				.url(baseurl + "/" + ENDPOINT)
				.build();
		Call call = client.newCall(request);
		return call.execute();
	}

	@Test
	void assertMethod() {
		// Change that immutable map to args from a more precise type, make a new object from that or use some kind of wiremock config
		Map<String, String> serverProps = new HashMap<String, String>(){{
				put("testUrl", ENDPOINT);
				put("statusCode", "200");
//				put("headers", "Content-Type\", \"text/plain");
				put("body", "[{\"course_name\":\"Appium\",\"id\":\"12\",\"price\":120,\"category\":\"mobile\"},{\"course_name\":\"Microservices testing\",\"id\":\"2\",\"price\":23,\"category\":\"api\"},{\"course_name\":\"Selenium\",\"id\":\"3\",\"price\":66,\"category\":\"web\"}]");
				put("consumerName", "BooksCatalogue");
				put("providerName", "CoursesCatalogue");
		}};

		createMeAServer(serverProps);
		Response response =  sendAReq();

		String expectedJson = "{\"booksPrice\":250,\"coursesPrice\":30}";

		// Do some assertions, make sure that you can (un) marshall the received object correctly.
		assertThat(response.code(), is(200));
	}

	@AfterEach
	void tearDown() {
		wm1.stop();
	}

}


