package donatehub.domain.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import donatehub.domain.request.AuthRequest;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import donatehub.domain.constants.UserRole;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity(name = "users_table")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity implements UserDetails {
    @Id
    @Column(unique = true)
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Column(name = "first_name")
    private String firstName;

    @Column(unique = true)
    private String username;

    private String description;

    @Column(name = "channel_url")
    private String channelUrl;

    @Column(unique = true, name = "channel_name")
    private String channelName;

    @Column(name = "profile_img_url")
    private String profileImgUrl;

    @Column(name = "banner_img_url")
    private String bannerImgUrl;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private String token;

    private Boolean online;

    private Boolean enable;

    @Column(name = "last_online_at")
    @JsonFormat(pattern = "dd:mm:yyyy hh:MM")
    private LocalDateTime lastOnlineAt;

    @Column(name = "min_donation_amount")
    private Float minDonationAmount;

    @Column(name = "full_registered_at")
    private LocalDateTime fullRegisteredAt;

    private Float balance;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return null;
    }

    public static UserEntity from(AuthRequest authRequest){
        return UserEntity.builder()
                .id(authRequest.getId())
                .online(false)
                .enable(false)
                .firstName(authRequest.getFirstName())
                .username(authRequest.getUsername())
                .balance(0f)
                .role(UserRole.STREAMER)
                .token(UUID.randomUUID().toString())
                .lastOnlineAt(LocalDateTime.now())
                .build();
    }
}