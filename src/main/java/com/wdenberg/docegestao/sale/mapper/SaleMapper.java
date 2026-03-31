package com.wdenberg.docegestao.sale.mapper;


import com.wdenberg.docegestao.sale.dto.SaleItemResponse;
import com.wdenberg.docegestao.sale.dto.SaleResponse;
import com.wdenberg.docegestao.sale.entity.Sale;
import com.wdenberg.docegestao.sale.entity.SaleItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SaleMapper {

    @Mapping(target = "client_id", source = "client.id")
    @Mapping(target = "clientName", source = "client.name")
    SaleResponse toResponse(Sale sale);


    @Mapping(target = "recipeId", source = "recipe.id")
    @Mapping(target = "recipeName", source = "recipe.name")
    SaleItemResponse toItemResponse(SaleItem SaleItem);
}
