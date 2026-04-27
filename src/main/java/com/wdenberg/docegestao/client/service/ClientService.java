package com.wdenberg.docegestao.client.service;


import com.wdenberg.docegestao.client.dto.ClientRequest;
import com.wdenberg.docegestao.client.dto.ClientResponse;
import com.wdenberg.docegestao.client.entity.Client;
import com.wdenberg.docegestao.client.mapper.ClientMapper;
import com.wdenberg.docegestao.client.repository.ClientRepository;
import com.wdenberg.docegestao.client.repository.ClientSpecification;
import com.wdenberg.docegestao.common.exception.ResouceNotFoundExceptio;
import com.wdenberg.docegestao.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@Service
public class ClientService {



    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final ClientMapper clientMapper;


    @Transactional
    public ClientResponse crate(UUID userId, ClientRequest request){
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResouceNotFoundExceptio("Usuario Não Encontrado"));


        Client client = new Client();

        client.setUser(user);
        client.setName(request.name());
        client.setEmail(request.email());
        client.setPhone(request.phone());
        client.setAddress(request.address());
        client.setNotes(request.notes());
        client.setActive(request.active() == null ? Boolean.TRUE : request.active());

        client.setTotalOrders(0);
        client.setTotalSpent(BigDecimal.ZERO);

        return clientMapper.toResponse(clientRepository.save(client));
    }

    @Transactional
    public Page<ClientResponse> findAll(UUID userId, String name, Boolean active, Pageable pageable){
        return clientRepository.findAll(ClientSpecification.byFilters(userId, name, active), pageable).map(clientMapper::toResponse);
    }

    @Transactional
    public ClientResponse findById(UUID id, UUID userId){
        return clientRepository.findByIdAndUser_Id(id, userId)
                .map(clientMapper::toResponse)
                .orElseThrow(() -> new ResouceNotFoundExceptio("Cliente não encontrado"));
    }


    @Transactional
    public ClientResponse update(UUID id, UUID userId, ClientRequest request){
        Client client = clientRepository.findByIdAndUser_Id(id, userId)
                .orElseThrow(() -> new ResouceNotFoundExceptio("Cliente não encontrado"));

        client.setName(request.name());
        client.setPhone(request.phone());
        client.setEmail(request.email());
        client.setAddress(request.address());
        client.setNotes(request.notes());
        client.setActive(request.active() == null ? client.getActive() : request.active());

        client.setTotalOrders(0);
        client.setTotalSpent(BigDecimal.ZERO);

        return clientMapper.toResponse(clientRepository.save(client));
    }

    @Transactional
    public void delete(UUID id, UUID userId){
        Client client = clientRepository.findByIdAndUser_Id(id, userId)
                .orElseThrow(() -> new ResouceNotFoundExceptio("Cliente não encontrado"));

        clientRepository.delete(client);
    }
}
