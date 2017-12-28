package bsr.project.bank.service;

import bsr.project.bank.model.User;
import bsr.project.bank.service.data.UserRepository;
import bsr.project.bank.utility.logging.LogMethodCall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void createUser(User user) {
        userRepository.save(user);
    }

    public boolean userExists(User user) {
        return userRepository.exists(user.getId());
    }

    @LogMethodCall
    public boolean authenticate(User user) {
        // TODO
        return true;
    }
}
