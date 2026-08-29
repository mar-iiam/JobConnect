package com.example.jobconnect.audit;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserLogRepository extends MongoRepository<UserLog, String> {
}