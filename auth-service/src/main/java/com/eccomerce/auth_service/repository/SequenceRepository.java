package com.eccomerce.auth_service.repository;

import com.eccomerce.auth_service.entity.SequenceEntity;
import org.springframework.data.repository.CrudRepository;

public interface SequenceRepository extends CrudRepository<SequenceEntity, String> {
}
