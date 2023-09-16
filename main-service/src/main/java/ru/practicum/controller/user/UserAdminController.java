package ru.practicum.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.user.UserDto;
import ru.practicum.dto.user.UserOutputDto;
import ru.practicum.service.user.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserAdminController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserOutputDto> create(@Valid @RequestBody UserDto userDto) {
        log.info("Received a request to create a user: {}", userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(userDto));
    }

    @GetMapping
    public ResponseEntity<Collection<UserOutputDto>> getAll(@RequestParam(required = false) List<Long> ids,
                                                            @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                            @RequestParam(value = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Received a request to fetch all users. IDs: {}, From: {}, Size: {}", ids, from, size);
        return ResponseEntity.ok(userService.get(ids, from, size));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable Long userId) {
        log.info("Received a request to delete a user by ID: {}", userId);
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }
}
