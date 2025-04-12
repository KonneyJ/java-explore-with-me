package ru.practicum.ewm.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.exceptions.ConflictException;
import ru.practicum.ewm.exceptions.NotFoundException;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.dto.UserMapper;
import ru.practicum.ewm.user.UserRepository;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserAdminServiceImpl implements UserAdminService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Collection<UserDto> getUsers(List<Integer> ids, int from, int size) {
        log.info("Получение списка пользователей с ids {}, from {}, size {}", ids, from, size);
        PageRequest page = PageRequest.of(from, size, Sort.by("user_id").ascending());
        List<User> users = new ArrayList<>();
        if (ids == null || ids.isEmpty()) {
            users = userRepository.findAll(page).getContent();
        } else {
            users = userRepository.findByIdIn(ids, page).getContent();
        }
        log.info("Пользователи успешно найдены");
        return users.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto createUser(NewUserRequest newUserRequest) {
        log.info("Проверка наличия пользователя с email {}", newUserRequest.getEmail());
        if (userRepository.findByEmail(newUserRequest.getEmail())) {
            throw new ConflictException("Пользователь с email: " + newUserRequest.getEmail() + " уже существует. " +
                    "Добавление невозможно");
        }
        log.info("Добавление нового пользователя {}", newUserRequest);
        User user = userRepository.save(userMapper.toUser(newUserRequest));
        log.info("Пользователь успешно добавлен с id {}", user.getId());
        return userMapper.toUserDto(user);
    }

    @Override
    public void deleteUser(int userId) {
        log.info("Удаление пользователя с id {}", userId);
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
        userRepository.deleteById(userId);
        log.info("Пользователь успешно удален");
    }
}
