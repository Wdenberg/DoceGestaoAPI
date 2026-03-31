package com.wdenberg.docegestao.client.repository;

import com.wdenberg.docegestao.client.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID>, JpaSpecificationExecutor<Client> {


    Optional<Client> findByIdUser_Id(UUID id, UUID userId);
}
