package com.wdenberg.docegestao.client.repository;

import com.wdenberg.docegestao.client.entity.Client;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class ClientSpecification {

    public static Specification<Client> byFilters(UUID userId, String name, Boolean active){
        return (root, query, cb) ->{
            var predicate = cb.equal(root.get("user").get("id"), userId);
            if( name != null && !name.isBlank()){
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            if(active != null) {
                predicate = cb.and(predicate, cb.equal(root.get("active"), active));
            }
            return predicate;
        };

    }
}
