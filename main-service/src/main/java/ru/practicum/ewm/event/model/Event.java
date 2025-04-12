package ru.practicum.ewm.event.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.event.model.enums.EventState;
import ru.practicum.ewm.user.User;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "events")
@Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private int id;

    @Column(name = "annotation", nullable = false)
    @Size(min = 20, max = 2000)
    private String annotation;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "confirmed_requests")
    private int confirmedRequests;

    @Column(name = "created_date")
    private LocalDateTime createdOn;

    @Column(name = "description")
    @Size(min = 20, max = 7000)
    private String description;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User initiator;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "location_id")
    private Location location;

    @Column(name = "paid")
    private boolean paid;

    @Column(name = "limit")
    private int participantLimit;

    @Column(name = "published_date", nullable = false)
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    private boolean requestModeration;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private EventState state;

    @Column(name = "title", nullable = false)
    @Size(min = 3, max = 120)
    private String title;
}
