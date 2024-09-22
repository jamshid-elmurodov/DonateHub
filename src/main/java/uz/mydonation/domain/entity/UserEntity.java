package uz.mydonation.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.mydonation.domain.enums.UserPermission;
import uz.mydonation.domain.enums.UserRole;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity(name = "users_table")

@DynamicInsert
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity extends BaseEntity implements UserDetails {
    @Column(unique = true)
    private Long chatId;

    private String firstName;

    @Column(unique = true)
    private String username;

    private String description;

    private String channelUrl;

    @Column(unique = true)
    private String channelName;

    private String profileImgUrl;

    private String bannerImgUrl;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private UUID api;

    private Boolean online;

    private Boolean enable;

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