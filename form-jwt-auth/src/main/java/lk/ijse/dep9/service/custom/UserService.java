package lk.ijse.dep9.service.custom;

import lk.ijse.dep9.service.SuperService;

public interface UserService extends SuperService {

    boolean verifyUser(String username, String password);
}
