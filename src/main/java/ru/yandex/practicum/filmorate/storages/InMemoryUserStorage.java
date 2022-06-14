package ru.yandex.practicum.filmorate.storages;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validations.UserNotFoundException;
import ru.yandex.practicum.filmorate.validations.ValidationException;
import java.util.HashMap;
import java.util.Map;

@Data
@Component
public class InMemoryUserStorage implements UserStorage{

    private int counter = 0;
    private final Map<Long, User> users = new HashMap<Long, User>();

    @Override
    public HashMap<Long, User> getUsers(){
        return (HashMap<Long, User>) users;
    }

    @Override
    public User create(User user)  {
        if (user.getName() == null||user.getName().isBlank()||user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        user.setId(counter + 1);
        counter+=1;
        long id = user.getId();
        users.put(id, user);
        return user;
    }


    @Override
    public User update(User user)  {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return user;
        } else {
            throw new UserNotFoundException("Нет пользователя с таким id");
        }
    }

    @Override
    public User getById(Long id) {
       if(!users.containsKey(id)){
           throw new UserNotFoundException("нет такого пользователя");
       }
        return users.get(id);
    }



    @Override
    public User remove(User user)  {
        if (users.containsKey(user.getId())) {
            users.remove(user.getId(), user);
            return user;
        } else {
            throw new ValidationException("Нет пользователя с таким id");
        }
    }

}
