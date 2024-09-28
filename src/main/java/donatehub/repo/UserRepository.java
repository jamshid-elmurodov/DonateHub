package donatehub.repo;

import donatehub.domain.projection.UserInfoForView;
import donatehub.domain.enums.UserRole;
import donatehub.domain.response.UserStatisticRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import donatehub.domain.entity.UserEntity;
import donatehub.domain.projection.UserInfoForDonate;
import donatehub.domain.projection.UserInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserInfo> getByIdOrderByCreatedAt(Long id);

    Optional<UserInfoForDonate> findByChannelNameIgnoreCase(String username);

    Page<UserInfoForView> getAllByEnableAndRoleOrderByFullRegisteredAt(Boolean approved, Pageable pageable, UserRole userRole);

    @Query("SELECT u FROM users_table u WHERE (LOWER(u.firstName) LIKE LOWER(CONCAT('%', :firstName, '%')) OR LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%'))) AND u.enable = :enable ORDER BY u.lastOnlineAt")
    Page<UserInfoForView> findAllByFirstNameOrUsernameAndEnable(@Param("firstName") String firstName, @Param("username") String username, @Param("enable") Boolean enable, Pageable pageable);

    @Query(
            value = "SELECT days AS day, COALESCE(COUNT(us.*), 0) as count " +
                    "FROM generate_series(current_date - INTERVAL '1 day' * :days, current_date, '1 day'::interval) AS days " +
                    "LEFT JOIN users_table us ON us.full_registered_at::date = days " +
                    "GROUP BY days " +
                    "ORDER BY days",
            nativeQuery = true
    )
    List<UserStatisticRes> getStatisticOfRegister(@Param("days") int days);

    @Query(
            value = "SELECT days AS day, COALESCE(COUNT(us.*), 0) as count " +
                    "FROM generate_series(current_date - INTERVAL '1 day' * :days, current_date, '1 day'::interval) AS days " +
                    "LEFT JOIN users_table us ON us.last_online_at::date = days " +
                    "GROUP BY days " +
                    "ORDER BY days",
            nativeQuery = true
    )
    List<UserStatisticRes> getStatisticOfLastOnline(@Param("days") int days);
}