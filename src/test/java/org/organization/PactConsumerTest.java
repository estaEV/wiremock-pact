package org.organization;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.organization.controller.LibraryController;
import org.organization.controller.ProductsPrices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.organization.controller.SpecificProduct;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonArray;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;

@SpringBootTest
@ExtendWith(PactConsumerTestExt.class)
/*Either add pactVersion = PactSpecVersion.V3 to the @PactTestFor and return type of @Pact as RequestResponsePact with no arg in the toPact method.
Or just make the @Test return type V4Pact and add it as an arg to the previously mentioned method.*/
@PactTestFor(providerName = "CoursesCatalogue")
public class PactConsumerTest {

	@Autowired
	private LibraryController libraryController;

	ObjectMapper obj = new ObjectMapper();

	@Pact(consumer = "BooksCatalogue", provider = "CoursesCatalogue")
	public V4Pact PactAllCoursesDetailsConfig(PactDslWithProvider builder) {
		return builder.given("Courses exist")
				.uponReceiving("Getting all course details")
				.path("/allCourseDetails")
				.willRespondWith()
				.status(200)
				.body(Objects.requireNonNull(PactDslJsonArray.arrayMinLike(3)
						.stringType("course_name")
						.stringType("id")
						.integerType("price", 10)
						.stringType("category").closeObject())).toPact(V4Pact.class);
	}

	@Pact(consumer = "BooksCatalogue", provider = "CoursesCatalogue")
	public V4Pact getCourseByName(PactDslWithProvider builder) {
		return builder.given("Course Appium exist")
				.uponReceiving("Get the Appium course details")
				.path("/getCourseByName/Appium")
				.willRespondWith()
				.status(200)
				.body(new PactDslJsonBody()
						.integerType("price", 13)
						.stringType("category", "mobile")).toPact(V4Pact.class);
	}

	@Pact(consumer = "BooksCatalogue", provider = "CoursesCatalogue")
	public V4Pact getCourseByName404(PactDslWithProvider builder) {
		return builder.given("Course Appium does not exist", "name", "Appium")
				.uponReceiving("Get 404 for Appium course details")
				.path("/getCourseByName/Appium")
				.willRespondWith()
				.status(404)
				.toPact(V4Pact.class);
	}


	@Test
	@PactTestFor(pactMethod = "PactAllCoursesDetailsConfig", port = "9999")
	public void testAllProductSum(MockServer mockServer) throws JsonProcessingException {
		String expectedJson = "{\"booksPrice\":250,\"coursesPrice\":30}";

		libraryController.setBaseUrl(mockServer.getUrl());

		ProductsPrices productsPrices = libraryController.getProductPrices();
		String jsonActual = obj.writeValueAsString(productsPrices);

		assertEquals(expectedJson, jsonActual);

	}

	@Test
	@PactTestFor(pactMethod = "getCourseByName", port = "9999")
	public void testByProductName(MockServer mockServer) throws JsonProcessingException {
		libraryController.setBaseUrl(mockServer.getUrl());

		String expectedJson = "{\"product\":{\"book_name\":\"Appium\",\"id\":\"ttefs36\",\"isbn\":\"ttefs\",\"aisle\":36,\"author\":\"Shetty\"},\"price\":13,\"category\":\"mobile\"}";
		SpecificProduct specificProduct = libraryController.getProductFullDetails("Appium");
		String actualJson = obj.writeValueAsString(specificProduct);

		assertEquals(expectedJson, actualJson);
	}

	@Test
	@PactTestFor(pactMethod = "getCourseByName404", port = "9999")
	public void testByProductName404(MockServer mockServer) throws JsonProcessingException {
		libraryController.setBaseUrl(mockServer.getUrl());

		String expectedJson = "{\"product\":{\"book_name\":\"Appium\",\"id\":\"ttefs36\",\"isbn\":\"ttefs\",\"aisle\":36,\"author\":\"Shetty\"},\"msg\":\"AppiumCategory and price details are not available at this time\"}";
		SpecificProduct specificProduct = libraryController.getProductFullDetails("Appium");
		String actualJson = obj.writeValueAsString(specificProduct);

		assertEquals(expectedJson, actualJson);
	}

}
