package com.wdenberg.docegestao.support;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import com.wdenberg.docegestao.auth.dto.LoginRequest;
import com.wdenberg.docegestao.auth.dto.RegisterRequest;


import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static
        org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static
        org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Component

public class AuthTestSupport {

    private final ObjectMapper objectMapper;

    public AuthTestSupport(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String registerAndGetAccessToken(MockMvc mockMvc,
        String name,
        String email,
        String password) throws Exception{

        var registerRequest = new RegisterRequest(name, email, password, List.of());
        String response = mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(response);
        return jsonNode.get("accessToken").asText();
    }

    public  String loginAndGetAccessToken(MockMvc mockMvc, String email, String password) throws Exception{

        var loginRequest = new LoginRequest(email, password);
        String response = mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(response);
        return jsonNode.get("accessToken").asText();
    }

    public String loginAndGetRefreshToken(MockMvc mockMvc, String email,
                                          String password) throws Exception {
        var loginRequest = new LoginRequest(email, password);
        String response = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        JsonNode json = objectMapper.readTree(response);
        return json.get("refreshToken").asText();
    }
    @TestConfiguration
    public class TestConfig {
        @Bean
        public com.fasterxml.jackson.databind.ObjectMapper legacyObjectMapper() {
            return new com.fasterxml.jackson.databind.ObjectMapper();
        }
    }

}
