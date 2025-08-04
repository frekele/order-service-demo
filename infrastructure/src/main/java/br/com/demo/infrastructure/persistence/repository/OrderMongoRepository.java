package br.com.demo.infrastructure.persistence.repository;

import br.com.demo.infrastructure.persistence.document.OrderDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderMongoRepository extends MongoRepository<OrderDocument, UUID> {

    Optional<OrderDocument> findByExternalOrderId(String externalOrderId);
}