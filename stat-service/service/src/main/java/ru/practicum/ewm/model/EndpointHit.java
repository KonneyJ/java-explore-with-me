package ru.practicum.ewm.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "stats")
@Builder
public class EndpointHit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotBlank
    @Column(name = "app")
    private String app;

    @NotBlank
    @Column(name = "uri")
    private String uri;

    @NotBlank
    @Column(name = "ip")
    private String ip;

    @NotNull
    @Column(name = "created")
    private LocalDateTime timestamp;
}
