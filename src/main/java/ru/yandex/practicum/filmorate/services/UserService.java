package ru.yandex.practicum.filmorate.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storages.UserStorage;
import ru.yandex.practicum.filmorate.validations.UserNotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void checkIdIsValid(long id) {
        if (!userStorage.getUsers().containsKey(id)){
            throw new UserNotFoundException("Нет пользователя с одним из id");
        }
    }

    public Map<User, User> addFriend(long userId1, long userId2) {
        checkIdIsValid(userId1);
        checkIdIsValid(userId2);
        User user1 = userStorage.getUsers().get(userId1);
        if (user1.getFriends().contains(userId2)){
            throw  new UserNotFoundException("Уже друзья");
        }
            user1.getFriends().add(userId2);
            userStorage.update(user1);
            userStorage.getUsers().get(userId2).getFriends().add(userId1);
            User user2 = userStorage.update(userStorage.getUsers().get(userId2));
            return Map.of(user1, user2);
        }

    public Map<Long, Long> deleteFriend(long userId1, long userId2) {
        checkIdIsValid(userId1);
        checkIdIsValid(userId2);
        User user1 = userStorage.getUsers().get(userId1);
        if (!user1.getFriends().contains(userId2)){
            throw  new UserNotFoundException("Нет вдрузьях");
        }
            userStorage.getUsers().get(userId1).getFriends().remove(userId2);
            userStorage.getUsers().get(userId2).getFriends().remove(userId1);
            return Map.of(userId1, userId2);
        }


    public List<User> getFriendsById(Long id) {
        if(!userStorage.getUsers().containsKey(id)){
            throw new UserNotFoundException("нет такого пользователя");
        }
        Set<Long> friends = userStorage.getById(id).getFriends();
        return  friends.stream()
                .map(Item -> userStorage.getById(Item))
                .collect(Collectors.toList());
    }

    public List<User> mutualFriends(long userId1, long userId2) {
        checkIdIsValid(userId1);
        checkIdIsValid(userId2);
        User user1 = userStorage.getUsers().get(userId1);
        User user2 = userStorage.getUsers().get(userId2);
            Set<Long> friendsTofilter = new HashSet<>();
            friendsTofilter.addAll(user1.getFriends());
            friendsTofilter.retainAll(user2.getFriends());
            List<User> mutualFriends = friendsTofilter.stream()
                    .map(Item -> userStorage.getUsers().get(Item))
                    .collect(Collectors.toList());
            return mutualFriends;
    }
}
