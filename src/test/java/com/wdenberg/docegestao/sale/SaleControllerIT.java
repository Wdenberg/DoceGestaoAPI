package com.wdenberg.docegestao.sale;

import com.wdenberg.docegestao.AbstractIntegrationTest;

import tools.jackson.databind.ObjectMapper;
import com.wdenberg.docegestao.client.dto.ClientRequest;
import com.wdenberg.docegestao.ingredient.dto.IngredientRequest;
import com.wdenberg.docegestao.recipe.dto.RecipeRequest;
import com.wdenberg.docegestao.sale.dto.SaleItemRequest;
import com.wdenberg.docegestao.sale.dto.SaleRequest;
import com.wdenberg.docegestao.support.AuthTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import static
        org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static
        org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static
        org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@AutoConfigureMockMvc
public class SaleControllerIT extends AbstractIntegrationTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    AuthTestSupport authTestSupport;

    @Test
    void shouldCreateSaleAndAddItem() throws Exception {
        String token = authTestSupport.registerAndGetAccessToken(mockMvc,
                "Wdenberg", "sale@test.com", "12345678");
        String clientResponse = mockMvc.perform(post("/api/v1/clients")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new
                                ClientRequest(
                                "Cliente Teste",
                                "81999999999",
                                "cliente@test.com",
                                "Rua X",
                                null,
                                true
                        ))))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        UUID clientId =
                UUID.fromString(objectMapper.readTree(clientResponse).get("id").asText());
        mockMvc.perform(post("/api/v1/ingredients")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new
                                IngredientRequest(
                                "Leite",
                                new BigDecimal("1000.000"),
                                "ml",
                                new BigDecimal("6.00"),
                                "Mercado",
                                true
                        ))))
                .andExpect(status().isCreated());
        String recipeResponse = mockMvc.perform(post("/api/v1/recipes")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new
                                RecipeRequest(
                                "Pudim",
                                "Receita doce",
                                null,
                                new BigDecimal("10.00"),
                                "fatias",
                                "Sobremesa",
                                new BigDecimal("40.00"),
                                BigDecimal.ZERO,
                                BigDecimal.ZERO,
                                BigDecimal.ZERO,
                                BigDecimal.ZERO,
                                true
                        ))))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        UUID recipeId =
                UUID.fromString(objectMapper.readTree(recipeResponse).get("id").asText());
        String saleResponse = mockMvc.perform(post("/api/v1/sales")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new
                                SaleRequest(
                                clientId,
                                LocalDate.now(),
                                new BigDecimal("2.00"),
                                "PIX",
                                "Entrega amanhã"
                        ))))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        UUID saleId =
                UUID.fromString(objectMapper.readTree(saleResponse).get("id").asText());
        mockMvc.perform(post("/api/v1/sales/{id}/items", saleId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new
                                SaleItemRequest(
                                recipeId,
                                new BigDecimal("2.00"),
                                new BigDecimal("15.00")
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].recipeId").value(recipeId.toString()))
                .andExpect(jsonPath("$.subTotal").value(30.00))
                .andExpect(jsonPath("$.totalAmount").value(28.00));
    }

}
