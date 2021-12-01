package org.dmc.imgmockserver.repository;

import org.dmc.imgmockserver.model.Metadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MetadataRepository extends JpaRepository<Metadata, UUID> {
    Optional<Metadata> findByAssetId(String assetId);
}
