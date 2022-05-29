package ru.yandex.practicum.filmorate.storages;

import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.model.User;

public interface UserStorage {
    User create(User user);

    User update(User user);

    User remove(User user);

    int getCounter();

    java.util.Map<Long, User> getUsers();

    User getById(Long id);
    boolean equals(Object o);


    int hashCode();

    String toString();
}
