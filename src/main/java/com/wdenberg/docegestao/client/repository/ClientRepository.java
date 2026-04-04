package com.wdenberg.docegestao.client.repository;

import com.wdenberg.docegestao.client.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID>, JpaSpecificationExecutor<Client> {


    @Query("SELECT c FROM Client c WHERE c.id = :id AND c.user.id = :userId")
    Optional<Client> findByIdAndUser_Id(@Param("id") UUID id, @Param("userId") UUID userId);
}
