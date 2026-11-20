package com.daria.transactionapi;

import com.daria.transactionapi.model.TransactionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
// Not compatible with Spring Boot 4.1.x. -- import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.http.ResponseEntity;

// Test Security Config
import org.springframework.context.annotation.Import;
// Imports for: Creating authenticated request 
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


// Spring Boot starts the real application on a temporary port.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
// @Import(TestSecurityConfig.class)
@Import({
        TestSecurityConfig.class,
        PostgresTestContainerConfig.class
})
class TransactionApiIntegrationTest {

        @Autowired
        private TestRestTemplate restTemplate;

        @Test
        void shouldCreateAndRetrieveTransaction() {
                
                Map<String, Object> request = Map.of(
                        "amount", new BigDecimal("125.50"),
                        "currency", "USD",
                        "description", "Integration test transaction",
                        "type", TransactionType.DEBIT
                );

                HttpHeaders headers = new HttpHeaders();
                headers.setBearerAuth("integration-test-token");

                HttpEntity<Map<String, Object>> httpEntity =
                        new HttpEntity<>(request, headers);
                
                /* 
                Creating authenticated request rather than using postForEntity() method
                Test sends Authorization: Bearer integration-test-token
                The test decoder recognizes the token and produces an authenticated Jwt.

                Note: Same thing done for GET
                */ 
                ResponseEntity<Map> createResponse =
                        restTemplate.exchange(
                                "/api/v1/transactions",
                                HttpMethod.POST,
                                httpEntity,
                                Map.class
                        );
                /*ResponseEntity<Map> createResponse =
                        restTemplate.postForEntity(
                                "/api/v1/transactions",
                                request,
                                Map.class
                        );*/

                assertEquals(201, createResponse.getStatusCode().value());

                Map<String, Object> createdTransaction = createResponse.getBody();

                assertNotNull(createdTransaction);
                assertNotNull(createdTransaction.get("id"));

                Number id = (Number) createdTransaction.get("id");

                ResponseEntity<Map> getResponse =
                        restTemplate.exchange(
                                "/api/v1/transactions/" + id.longValue(),
                                HttpMethod.GET,
                                httpEntity,
                                Map.class
                        );
                /*ResponseEntity<Map> getResponse =
                        restTemplate.getForEntity(
                                "/api/v1/transactions/" + id.longValue(),
                                Map.class
                        );*/

                assertEquals(200, getResponse.getStatusCode().value());

                Map<String, Object> retrievedTransaction = getResponse.getBody();

                assertNotNull(retrievedTransaction);
                assertEquals(id.longValue(),
                        ((Number) retrievedTransaction.get("id")).longValue());

                assertEquals(
                        0,
                        new BigDecimal("125.50")
                                .compareTo(new BigDecimal(retrievedTransaction.get("amount").toString()))
                );
                assertEquals("USD", retrievedTransaction.get("currency"));
                assertEquals("Integration test transaction",
                        retrievedTransaction.get("description"));
                assertEquals("DEBIT", retrievedTransaction.get("type"));
        }
}
