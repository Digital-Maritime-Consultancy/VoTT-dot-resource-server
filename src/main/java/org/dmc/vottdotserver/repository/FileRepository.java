package org.dmc.vottdotserver.repository;

import org.dmc.vottdotserver.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FileRepository extends JpaRepository<File, UUID> {
    Optional<File> findByFileName(String fileName);
    Optional<File> findByTaskId(String taskId);
    Optional<File> findByFileNameAndTaskId(String fileName, String taskId);
}
