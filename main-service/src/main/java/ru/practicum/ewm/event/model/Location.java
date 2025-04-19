package ru.practicum.ewm.event.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "location")
@Builder
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private int id;

    @NotNull
    @Column(name = "lat", nullable = false)
    private float lat;

    @NotNull
    @Column(name = "lon", nullable = false)
    private float lon;
}
