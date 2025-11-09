package com.reader.manga.domain.entities.notifications;

import com.reader.manga.domain.enums.OriginType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
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
    private LocalDate dataIn;

    @Column(name = "data_out")
    private LocalDate dataOut;

    @Column(name = "origin")
    @Enumerated(EnumType.STRING)
    private OriginType origin;

    public Notification(String content, LocalDate dataIn, LocalDate dataOut, OriginType origin) {
        this.content = content;
        this.dataIn = dataIn;
        this.dataOut = dataOut;
        this.origin = origin;
    }

}
