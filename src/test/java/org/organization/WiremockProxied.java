/*
package org.organization;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;

import wiremock.org.apache.hc.client5.http.classic.methods.HttpGet;
import wiremock.org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import wiremock.org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import wiremock.org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import wiremock.org.apache.hc.core5.http.io.entity.EntityUtils;

//WireMock's JUnit Jupiter Extension
public class WiremockProxied {


//Creating wiremock stubs
@RegisterExtension
static WireMockExtension wm = WireMockExtension.newInstance()
		.proxyMode(true)
		.build();

	CloseableHttpClient client;
	static String HOST = "127.0.0.1";
	static String PORT = "8181";
	static String ENDPOINT = "allCourseDetails";
	static String URL_TO_HIT = "http://" + HOST + ":" + PORT + "/" + ENDPOINT;

	@BeforeEach
	void init() {
		client = HttpClientBuilder.create()
				.useSystemProperties() // This must be enabled for auto proxy config
				.build();
	}

	@Test
	void configures_jvm_proxy_and_enables_browser_proxying() throws Exception {
		String bodyToReturn = "[{\"course_name\":\"Appium\",\"id\":\"120\",\"price\":120,\"category\":\"mobile\"},{\"course_name\":\"Microservices testing\",\"id\":\"2\",\"price\":23,\"category\":\"api\"},{\"course_name\":\"Selenium\",\"id\":\"3\",\"price\":66,\"category\":\"web\"}]";
		String expectedBody = "[{\"course_name\":\"Appium\",\"id\":\"12\",\"price\":120,\"category\":\"mobile\"},{\"course_name\":\"Microservices testing\",\"id\":\"2\",\"price\":23,\"category\":\"api\"},{\"course_name\":\"Selenium\",\"id\":\"3\",\"price\":66,\"category\":\"web\"}]";

		wm.stubFor(WireMock.get(ENDPOINT)
				.withHost(equalTo(HOST + ":" + PORT))
				.willReturn(ok(bodyToReturn)));

		assertThat(getContent(URL_TO_HIT), is(expectedBody));
	}

	private String getContent(String url) throws Exception {
		try (CloseableHttpResponse response = client.execute(new HttpGet(url))) {
			return EntityUtils.toString(response.getEntity());
		}
	}

}

*/
