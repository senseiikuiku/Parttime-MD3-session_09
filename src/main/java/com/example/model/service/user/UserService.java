package com.example.model.service.user;

import com.example.model.entity.User;

public interface UserService {
    boolean register(User user) ;
    User login(User user);

}
