package org.dmc.vottdotserver.repository;

import org.dmc.vottdotserver.model.Json;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JsonRepository extends JpaRepository<Json, UUID> {
    Optional<Json> findByFileName(String fileName);
}
