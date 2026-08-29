package com.example.jobconnect.audit;



import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
@Data
@Document(collection = "user_logs")
public class UserLog {

    @Id
    private String id;

    private Long userId;

    private String email;

    private UserLogAction action;

    private Instant timestamp;

    private String ipAddress;




    public enum UserLogAction {
        REGISTRATION,
        LOGIN
    }
}

