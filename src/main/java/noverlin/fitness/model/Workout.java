package noverlin.fitness.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;

@Entity
@Setter
@Getter
@Accessors(chain = true)
@Table(name = "workouts")
public class Workout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column
    private Instant date;

    @Column(nullable = false)
    private Long duration;

    @Column(nullable = false)
    private Long calories;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkoutType type;
}
