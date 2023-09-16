package ru.practicum.repository.user;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.dto.user.UserOutputDto;
import ru.practicum.model.user.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT NEW ru.practicum.dto.user.UserOutputDto(u.id, u.name, u.email) " +
            "FROM User u " +
            "WHERE (:ids IS NULL OR u.id IN :ids) " +
            "ORDER BY u.id ASC")
    List<UserOutputDto> getUsersByIds(@Param("ids") List<Long> ids, Pageable pageable);
}
