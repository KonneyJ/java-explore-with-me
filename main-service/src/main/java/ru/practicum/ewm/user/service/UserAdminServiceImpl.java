package ru.practicum.ewm.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.user.UserRepository;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class UserAdminServiceImpl implements UserAdminService {
    private final UserRepository userRepository;

    @Override
    public Collection<UserDto> getUsers(List<Integer> ids, int from, int size) {
        return List.of();
    }

    @Override
    public UserDto createUser(NewUserRequest newUserRequest) {
        return null;
    }

    @Override
    public void deleteUser(int userId) {

    }
}
