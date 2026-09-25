package com.example.finance2;

import com.example.finance2.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static com.example.finance2.config.DemoDataLoader.DEMO_PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Kör hela appen (säkerhet, controllers, service, JPA och schema.sql) mot H2.
// @Transactional rullar tillbaka varje test så att demodatan är orörd.
@SpringBootTest
@Transactional
class BankApiIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    void accountEndpointsRequireLogin() throws Exception {
        mvc.perform(get("/api/me"))
                .andExpect(status().isUnauthorized())
                .andExpect(header().doesNotExist("WWW-Authenticate"));
    }

    @Test
    void wrongPasswordIsRejected() throws Exception {
        mvc.perform(get("/api/me").with(httpBasic("Darin", "fel-lösenord")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loginReturnsAccountWithoutPassword() throws Exception {
        mvc.perform(get("/api/me").with(httpBasic("Darin", DEMO_PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Darin"))
                .andExpect(jsonPath("$.balance").value(100.0))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void passwordsAreStoredAsBcryptHashes() {
        String stored = userRepository.findByName("Darin").orElseThrow().getPassword();
        assertThat(stored).startsWith("$2").doesNotContain(DEMO_PASSWORD);
    }

    @Test
    void depositUpdatesBalance() throws Exception {
        mvc.perform(asDarin(post("/api/me/deposit"), "{\"amount\": 25.50}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(125.5));

        mvc.perform(get("/api/me").with(httpBasic("Darin", DEMO_PASSWORD)))
                .andExpect(jsonPath("$.balance").value(125.5));
    }

    @Test
    void withdrawingMoreThanBalanceIsRejected() throws Exception {
        mvc.perform(asDarin(post("/api/me/withdraw"), "{\"amount\": 100.01}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").exists());

        mvc.perform(get("/api/me").with(httpBasic("Darin", DEMO_PASSWORD)))
                .andExpect(jsonPath("$.balance").value(100.0));
    }

    @Test
    void negativeAndMalformedAmountsAreRejected() throws Exception {
        mvc.perform(asDarin(post("/api/me/deposit"), "{\"amount\": -500}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Beloppet måste vara större än 0."));

        mvc.perform(asDarin(post("/api/me/deposit"), "{\"amount\": \"mycket\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void regularUserCannotSeeAllTransactions() throws Exception {
        mvc.perform(get("/api/admin/transactions").with(httpBasic("Darin", DEMO_PASSWORD)))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminSeesAllTransactions() throws Exception {
        mvc.perform(asDarin(post("/api/me/deposit"), "{\"amount\": 10}"))
                .andExpect(status().isOk());

        Long darinId = userRepository.findByName("Darin").orElseThrow().getId();
        mvc.perform(get("/api/admin/transactions").with(httpBasic("William", DEMO_PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(darinId))
                .andExpect(jsonPath("$[0].type").value("DEPOSIT"))
                .andExpect(jsonPath("$[0].amount").value(10.0))
                .andExpect(jsonPath("$[0].createdAt").isString());
    }

    @Test
    void frontendIsServedWithoutLogin() throws Exception {
        mvc.perform(get("/index.html")).andExpect(status().isOk());
    }

    private static MockHttpServletRequestBuilder asDarin(MockHttpServletRequestBuilder request, String json) {
        return request.with(httpBasic("Darin", DEMO_PASSWORD))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
    }
}
