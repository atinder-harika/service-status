package com.atinder.service_status_backend.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.atinder.service_status_backend.config.SecurityConfig;
import com.atinder.service_status_backend.model.MonitoredService;
import com.atinder.service_status_backend.repository.ServiceRepository;
import com.jayway.jsonpath.JsonPath;

@WebMvcTest({ServiceController.class, AuthController.class})
@Import({SecurityConfig.class, SecurityTest.Mocks.class})
@TestPropertySource(properties = {
    "security.jwt.secret=test-secret-that-is-at-least-32-bytes-long",
    "security.admin.username=admin",
    "security.admin.password=correct-horse"
})
class SecurityTest {

    @Autowired MockMvc mvc;
    @Autowired ServiceRepository repo;

    @TestConfiguration
    static class Mocks {
        @Bean ServiceRepository serviceRepository() { return Mockito.mock(ServiceRepository.class); }
    }

    private static final String NEW_SERVICE = "{\"name\":\"Example\",\"url\":\"https://example.com\"}";

    @Test
    void readsArePublic() throws Exception {
        when(repo.findAll()).thenReturn(List.of());
        mvc.perform(get("/api/services")).andExpect(status().isOk());
    }

    @Test
    void writesWithoutTokenAreRejected() throws Exception {
        mvc.perform(post("/api/services").contentType(MediaType.APPLICATION_JSON).content(NEW_SERVICE))
            .andExpect(status().isUnauthorized());
        mvc.perform(delete("/api/services/1")).andExpect(status().isUnauthorized());
    }

    @Test
    void wrongPasswordGetsNoToken() throws Exception {
        mvc.perform(post("/api/auth/token").contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"admin\",\"password\":\"wrong\"}"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void forgedTokenIsRejected() throws Exception {
        String forged = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInNjb3BlIjoiYWRtaW4ifQ.c2lnbmF0dXJl";
        mvc.perform(post("/api/services").header("Authorization", "Bearer " + forged)
                .contentType(MediaType.APPLICATION_JSON).content(NEW_SERVICE))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void tokenFromLoginAllowsWrites() throws Exception {
        String body = mvc.perform(post("/api/auth/token").contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"admin\",\"password\":\"correct-horse\"}"))
            .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        String token = JsonPath.read(body, "$.access_token");

        when(repo.findByName("Example")).thenReturn(Optional.empty());
        when(repo.save(any(MonitoredService.class))).thenAnswer(i -> i.getArgument(0));
        mvc.perform(post("/api/services").header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON).content(NEW_SERVICE))
            .andExpect(status().isCreated());

        mvc.perform(post("/api/services").header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Bad\",\"url\":\"file:///etc/passwd\"}"))
            .andExpect(status().isBadRequest());
    }
}
