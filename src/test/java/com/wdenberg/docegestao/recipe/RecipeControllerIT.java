package com.wdenberg.docegestao.recipe;


import org.springframework.test.annotation.DirtiesContext;
import tools.jackson.databind.ObjectMapper;
import com.wdenberg.docegestao.AbstractIntegrationTest;
import com.wdenberg.docegestao.ingredient.dto.IngredientRequest;
import com.wdenberg.docegestao.recipe.dto.RecipeItemRequest;
import com.wdenberg.docegestao.recipe.dto.RecipeRequest;
import com.wdenberg.docegestao.support.AuthTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.util.UUID;
import static
        org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static
        org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static
        org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@DirtiesContext
public class RecipeControllerIT extends AbstractIntegrationTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    AuthTestSupport authTestSupport;

    @Test
    void shouldCreateRecipeAndAddItem() throws Exception {
        String token = authTestSupport.registerAndGetAccessToken(mockMvc,
                "Wdenberg", "recipe@test.com", "12345678");
        var ingredientRequest = new IngredientRequest(
                "Creme de leite",
                new BigDecimal("200.000"),
                "g",
                new BigDecimal("4.50"),
                "Mercado",
                true
        );
        String ingredientResponse = mockMvc.perform(post("/api/v1/ingredients")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ingredientRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        UUID ingredientId =
                UUID.fromString(objectMapper.readTree(ingredientResponse).get("id").asText());
        var recipeRequest = new RecipeRequest(
                "Brigadeiro",
                "Receita base",
                null,
                new BigDecimal("20.00"),
                "unidades",
                "Doces",
                new BigDecimal("45.00"),
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                true
        );
        String recipeResponse = mockMvc.perform(post("/api/v1/recipes")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipeRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Brigadeiro"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        UUID recipeId =
                UUID.fromString(objectMapper.readTree(recipeResponse).get("id").asText());
        var recipeItemRequest = new RecipeItemRequest(
                ingredientId,
                new BigDecimal("100.000"),
                "g"
        );
        mockMvc.perform(post("/api/v1/recipes/{id}/items", recipeId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipeItemRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].ingredientId").value(ingredientId.toString())).andExpect(jsonPath("$.ingredientsCost").exists())
                .andExpect(jsonPath("$.productionCost").exists())
                .andExpect(jsonPath("$.suggestedPrice").exists());
    }

}
