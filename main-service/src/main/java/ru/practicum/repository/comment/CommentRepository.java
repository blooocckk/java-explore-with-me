package ru.practicum.repository.comment;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.comment.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByEvent_Id(Long eventId, Pageable pageable);

    List<Comment> findByUser_Id(Long creatorId, Pageable pageable);

    List<Comment> findByContentContaining(String searchText, Pageable pageable);
}
