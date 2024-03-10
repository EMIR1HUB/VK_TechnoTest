package com.texhnotest.vk.security;

import com.texhnotest.vk.security.models.AuditLog;
import com.texhnotest.vk.security.models.UserPrincipal;
import com.texhnotest.vk.security.repositories.AuditLogRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Aspect
@Component
public class AuditLogAspect {

  private final AuditLogRepository auditLogRepository;

  @Autowired
  public AuditLogAspect(AuditLogRepository auditLogRepository) {
    this.auditLogRepository = auditLogRepository;
  }

  @Around("@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
          "@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
          "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
          "@annotation(org.springframework.web.bind.annotation.PatchMapping) || " +
          "@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
  public Object auditLog(ProceedingJoinPoint joinPoint) throws Throwable {
    Object result;
    try {
      result = joinPoint.proceed();
      logAudit(joinPoint, true);
    } catch (Exception e) {
      logAudit(joinPoint, false);
      throw e;
    }
    return result;
  }

  private void logAudit(ProceedingJoinPoint joinPoint, boolean success) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = (authentication != null && authentication.getPrincipal() instanceof UserPrincipal)
            ? ((UserPrincipal) authentication.getPrincipal()).getUsername()
            : null;
    String roles = (authentication != null)
            ? authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(", "))
            : "";

    String action = joinPoint.getSignature().toShortString();
    String requestParams = Arrays.toString(joinPoint.getArgs());

    AuditLog auditLog = new AuditLog();
    auditLog.setTimestamp(LocalDateTime.now());
    auditLog.setUsername(username);
    auditLog.setRoles(roles);
    auditLog.setAction(action);
    auditLog.setSuccess(success);
    auditLog.setRequestParams(requestParams);

    auditLogRepository.save(auditLog);
  }
}
