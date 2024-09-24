package donatehub.repo;

import donatehub.domain.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import donatehub.domain.entity.UserEntity;
import donatehub.domain.projection.UserInfoForDonate;
import donatehub.domain.projection.UserInfo;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByChatId(Long chatId);

    Optional<UserInfo> getByChatId(Long chatId);

    Optional<UserInfoForDonate> findByChannelNameIgnoreCase(String username);

    Page<UserInfo> getAllByEnableAndRole(Boolean approved, Pageable pageable, UserRole userRole);

    Page<UserInfo> getAllByFirstNameContainingIgnoreCaseOrUsernameContainingIgnoreCase(String firstName, String username, Pageable pageable);

//    @Query(
//            "SELECT new donatehub.domain.response.UserStatisticRes(DATE(ut.createdAt), COUNT(*)) " +
//                    "FROM users_table ut " +
//                    "WHERE DATE(ut.createdAt) >= :date " +
//                    "GROUP BY DATE(ut.createdAt)"
//    )
//    List<UserStatisticRes> getStatisticOfRegister(@Param("date") LocalDate date);
//
//    @Query(
//            "SELECT new donatehub.domain.response.UserStatisticRes(DATE(ut.createdAt), COUNT(*)) " +
//                    "FROM users_table ut " +
//                    "WHERE DATE(ut.lastOnlineAt) >= :date " +
//                    "GROUP BY DATE(ut.lastOnlineAt)"
//    )
//    List<UserStatisticRes> getStatisticOfLastOnline(@Param("date") LocalDate date);
}