package com.example.jobconnect.audit;

import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AuditService {

    private final UserLogRepository userLogRepository;

    public AuditService(UserLogRepository userLogRepository) {
        this.userLogRepository = userLogRepository;
    }

    public void logRegistration(
            String email,
            String ipAddress) {

        UserLog log = new UserLog();


        log.setEmail(email);
        log.setAction(UserLog.UserLogAction.REGISTRATION);
        log.setTimestamp(Instant.now());
        log.setIpAddress(ipAddress);

        userLogRepository.save(log);
    }

    public void logLogin(
            String email,
            String ipAddress) {

        UserLog log = new UserLog();
        log.setEmail(email);
        log.setAction(UserLog.UserLogAction.LOGIN);
        log.setTimestamp(Instant.now());
        log.setIpAddress(ipAddress);

        userLogRepository.save(log);
    }
}