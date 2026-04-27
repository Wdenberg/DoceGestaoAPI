package com.wdenberg.docegestao.ingredient;

import tools.jackson.databind.ObjectMapper;
import com.wdenberg.docegestao.AbstractIntegrationTest;

import com.wdenberg.docegestao.ingredient.dto.IngredientRequest;
import com.wdenberg.docegestao.support.AuthTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static
        org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static
        org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static
        org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static
        org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureJson
public class IngredientControllerIT extends AbstractIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AuthTestSupport authTestSupport;

    @Test
    void shouldCreateIngredient() throws Exception{
        String token = authTestSupport.registerAndGetAccessToken(mockMvc, "wdenberg", "ingredient@test.com", "12345678");
        var request = new IngredientRequest(
                "Leite Condensado",
                new BigDecimal("395.000"),
                "g",
                new BigDecimal("8.90"),
                "Atacadão",
                true
        );

        mockMvc.perform(post("/api/v1/ingredients")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Leite Condensado"))
                .andExpect(jsonPath("$.unitMeasure").value("g"));
    }

    @Test
    void shouldListIngredientsWithPagination() throws Exception {
        String token = authTestSupport.registerAndGetAccessToken(mockMvc,
                "Wdenberg", "ingredient-list@test.com", "12345678");
        var request = new IngredientRequest(
                "Chocolate",
                new BigDecimal("1000.000"),
                "g",
                new BigDecimal("25.00"),
                "Fornecedor X",
                true
        );
        mockMvc.perform(post("/api/v1/ingredients")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/api/v1/ingredients?page=0&size=10&sort=createdAt,desc")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " +
                                token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Chocolate"));
    }

}
