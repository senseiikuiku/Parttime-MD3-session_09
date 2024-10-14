package com.example.model.dao.user;

import com.example.model.entity.User;

public interface UserDAO {
    boolean create(User user) ;
    User findUserByEmail(String email);

}
