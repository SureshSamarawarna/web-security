package lk.ijse.dep9.service.custom.impl;

import lk.ijse.dep9.service.custom.UserService;

public class UserServiceImpl implements UserService {

    @Override
    public boolean verifyUser(String username, String password) {
        return ((username.equals("admin") && password.equals("admin")) || (username.equals("staff") && password.equals("staff")));
    }
}
