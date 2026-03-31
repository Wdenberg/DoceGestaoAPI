package com.wdenberg.docegestao.client.mapper;


import com.wdenberg.docegestao.client.dto.ClientResponse;
import com.wdenberg.docegestao.client.entity.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    ClientResponse toResponse(Client client);


}
