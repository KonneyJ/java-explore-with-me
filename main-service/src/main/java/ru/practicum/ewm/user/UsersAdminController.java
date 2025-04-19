package ru.practicum.ewm.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.service.UserAdminService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
public class UsersAdminController {
    private final UserAdminService userService;

    @GetMapping
    public Collection<UserDto> getUsers(@RequestParam(required = false) List<Integer> ids,
                                        @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                        @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("GET /admin/users запрос with ids {}, from {}, size {}", ids, from, size);
        return userService.getUsers(ids, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody NewUserRequest newUserRequest) {
        log.info("POST /admin/users запрос with newUserRequest {}", newUserRequest);
        return userService.createUser(newUserRequest);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("userId") int userId) {
        log.info("DELETE /admin/users/userId запрос with id {}", userId);
        userService.deleteUser(userId);
    }
}
