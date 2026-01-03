package com.example.jobconnect.Mongo;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "app_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppLog {

    @Id
    private String id;

    private String message;
    private LocalDateTime createdAt;
}
