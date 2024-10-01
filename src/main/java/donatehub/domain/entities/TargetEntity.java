package donatehub.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "targets_table")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TargetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updateAt;

    private String name;

    @Column(name = "target_amount")
    private Float targetAmount;

    @Column(name = "current_amount")
    private Float currentAmount;

    @ManyToOne
    private UserEntity streamer;

    private Boolean active;
}
