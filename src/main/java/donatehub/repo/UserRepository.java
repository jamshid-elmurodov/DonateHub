package donatehub.repo;

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

    Page<UserInfo> getAllByEnable(Boolean approved, Pageable pageable);

    Page<UserInfo> getAllByFirstNameLikeIgnoreCaseOrUsernameLikeIgnoreCase(String firstName, String username, Pageable pageable);
}