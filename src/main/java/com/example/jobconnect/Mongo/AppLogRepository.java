package com.example.jobconnect.Mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppLogRepository extends MongoRepository<AppLog, String> {
}
