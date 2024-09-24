package donatehub.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import donatehub.domain.enums.UserRole;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity(name = "users_table")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity extends BaseEntity implements UserDetails {
    @Column(unique = true, name = "chat_id")
    private Long chatId;

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

    private String api;

    private Boolean online;

    private Boolean enable;

    @Column(name = "last_online_at")
    @JsonFormat(pattern = "dd:mm:yyyy hh:MM")
    private LocalDateTime lastOnlineAt;

    @Column(name = "min_donation_amount")
    private Float minDonationAmount;

    private Float balance;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return null;
    }

    public Long getId() {
        return chatId;
    }
}