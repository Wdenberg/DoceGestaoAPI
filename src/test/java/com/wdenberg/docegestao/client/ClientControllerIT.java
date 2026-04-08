package com.wdenberg.docegestao.client;

import tools.jackson.databind.ObjectMapper;
import com.wdenberg.docegestao.AbstractIntegrationTest;
import com.wdenberg.docegestao.client.dto.ClientRequest;
import com.wdenberg.docegestao.support.AuthTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static
        org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static
        org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static
        org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static
        org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ClientControllerIT extends AbstractIntegrationTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    AuthTestSupport authTestSupport;
    @Test
    void shouldCreateClient() throws Exception {
        String token = authTestSupport.registerAndGetAccessToken(mockMvc,
                "Wdenberg", "client@test.com", "12345678");
        var request = new ClientRequest(
                "Maria da Silva",
                "81999999999",
                "maria@email.com",
                "Rua A, 123",
                "Cliente VIP",
                true
        );
        mockMvc.perform(post("/api/v1/clients")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Maria da Silva"))
                .andExpect(jsonPath("$.email").value("maria@email.com"));
    }
    @Test
    void shouldListClients() throws Exception {
        String token = authTestSupport.registerAndGetAccessToken(mockMvc,
                "Wdenberg", "client-list@test.com", "12345678");
        var request = new ClientRequest(
                "João Santos",
                "81888888888",
                "joao@email.com",
                "Rua B, 456",
                null,
                true
        );
        mockMvc.perform(post("/api/v1/clients")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/api/v1/clients?page=0&size=10")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " +
                                token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("João Santos"));
    }
}