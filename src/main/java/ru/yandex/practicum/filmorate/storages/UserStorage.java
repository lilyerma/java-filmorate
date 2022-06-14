package ru.yandex.practicum.filmorate.storages;

import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

public interface UserStorage {
    User create(User user);

    User update(User user);

    User remove(User user);

    int getCounter();

    HashMap<Long, User> getUsers();

    User getById(Long id);
    boolean equals(Object o);


    int hashCode();

    String toString();
}
