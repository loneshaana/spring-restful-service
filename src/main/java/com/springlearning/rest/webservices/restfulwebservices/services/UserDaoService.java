package com.springlearning.rest.webservices.restfulwebservices.services;

import com.springlearning.rest.webservices.restfulwebservices.beans.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Component
public class UserDaoService {
    private static final List<User> users = new ArrayList<>();
    private static int usersCount = 6;

    static {
        users.add(new User(1, "Admin", new Date()));
        users.add(new User(2, "User", new Date()));
        users.add(new User(3, "Shaman", new Date()));
        users.add(new User(4, "Anwar", new Date()));
        users.add(new User(5, "local", new Date()));
        users.add(new User(6, "Admin2", new Date()));
    }

    public List<User> findAll() {
        return users;
    }

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(++usersCount);
        }
        users.add(user);
        return user;
    }

    public User findOne(int id) {
        for (User user : users) {
            if (user.getId() == id) return user;
        }
        return null;
    }

    public User deleteById(int id) {
        Iterator<User> iterator = users.iterator();
        while (iterator.hasNext()) {
            User user = iterator.next();
            if (user.getId() == id) {
                iterator.remove();
                return user;
            }
        }
        return null;
    }

}
