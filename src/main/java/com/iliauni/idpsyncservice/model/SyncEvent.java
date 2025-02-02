package com.iliauni.idpsyncservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@NoArgsConstructor
@MappedSuperclass
public class SyncEvent {

    public SyncEvent(String message) {
        this.message = message;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "message")
    private String message;

    @Column(name = "exception")
    private Exception exception;

    @Column(name = "exception_message")
    private String exceptionMessage;

    @CreationTimestamp
    private Date startDate;
}
