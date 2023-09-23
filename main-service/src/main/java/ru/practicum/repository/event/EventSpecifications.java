package ru.practicum.repository.event;

import org.springframework.data.jpa.domain.Specification;
import ru.practicum.model.category.Category;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.State;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.List;

public class EventSpecifications {
    public static Specification<Event> withFilters(
            String text, State state, List<Long> categories,
            Boolean paid, LocalDateTime rangeStart,
            LocalDateTime rangeEnd, Boolean checkParticipantLimit
    ) {
        return (root, query, builder) -> {
            Predicate predicate = builder.conjunction();

            if (text != null) {
                String searchText = "%" + text.toUpperCase() + "%";
                predicate = builder.and(
                        predicate,
                        builder.or(
                                builder.like(builder.upper(root.get("annotation")), searchText),
                                builder.like(builder.upper(root.get("description")), searchText)
                        )
                );
            }

            if (state != null) {
                predicate = builder.and(predicate, builder.equal(root.get("state"), state));
            }

            if (categories != null) {
                Join<Event, Category> categoryJoin = root.join("category", JoinType.INNER);
                predicate = builder.and(predicate, categoryJoin.get("id").in(categories));
            }

            if (paid != null) {
                predicate = builder.and(predicate, builder.equal(root.get("paid"), paid));
            }

            if (rangeStart != null) {
                predicate = builder.and(predicate, builder.or(
                        builder.isNull(root.get("eventDate")),
                        builder.greaterThanOrEqualTo(root.get("eventDate"), rangeStart)
                ));
            }

            if (rangeEnd != null) {
                predicate = builder.and(predicate, builder.or(
                        builder.isNull(root.get("eventDate")),
                        builder.lessThanOrEqualTo(root.get("eventDate"), rangeEnd)
                ));
            }

            if (checkParticipantLimit != null && checkParticipantLimit) {
                predicate = builder.and(predicate, builder.greaterThan(root.get("participantLimit"), root.get("confirmedRequests")));
            }

            return predicate;
        };
    }

    public static Specification<Event> search(
            List<Long> users, List<Long> categories, List<State> states,
            LocalDateTime rangeStart, LocalDateTime rangeEnd
    ) {
        return (root, query, builder) -> {
            Predicate predicate = builder.conjunction();

            if (users != null) {
                predicate = builder.and(predicate, root.get("initiator").get("id").in(users));
            }

            if (categories != null) {
                Join<Event, Category> categoryJoin = root.join("category", JoinType.INNER);
                predicate = builder.and(predicate, categoryJoin.get("id").in(categories));
            }

            if (states != null) {
                predicate = builder.and(predicate, root.get("state").in(states));
            }

            if (rangeStart != null) {
                predicate = builder.and(predicate, builder.or(
                        builder.isNull(root.get("eventDate")),
                        builder.greaterThanOrEqualTo(root.get("eventDate"), rangeStart)
                ));
            }

            if (rangeEnd != null) {
                predicate = builder.and(predicate, builder.or(
                        builder.isNull(root.get("eventDate")),
                        builder.lessThanOrEqualTo(root.get("eventDate"), rangeEnd)
                ));
            }

            return predicate;
        };
    }
}