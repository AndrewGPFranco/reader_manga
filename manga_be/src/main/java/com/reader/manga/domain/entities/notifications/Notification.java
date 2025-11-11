package com.reader.manga.domain.entities.notifications;

import com.reader.manga.domain.enums.OriginType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Column(name = "content")
    private String content;

    @CreationTimestamp
    @Column(name = "data_in")
    private LocalDateTime dataIn;

    @Column(name = "data_out")
    private LocalDateTime dataOut;

    @Column(name = "origin")
    @Enumerated(EnumType.STRING)
    private OriginType origin;

    public Notification() {
    }

}
