package com.texhnotest.vk.security.repositories;

import com.texhnotest.vk.security.models.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}