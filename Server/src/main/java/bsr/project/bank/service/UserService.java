package bsr.project.bank.service;

import bsr.project.bank.model.User;
import bsr.project.bank.model.exception.AuthenticationFailedException;
import bsr.project.bank.service.data.UserRepository;
import com.bsr.types.bank.AuthHeader;
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

    public User authenticate(AuthHeader auth) throws AuthenticationFailedException {
        User user = userRepository.findByName(auth.getUsername());
        if (user == null || !user.getPassword().equals(auth.getPassword())) {
            throw new AuthenticationFailedException();
        }
        return user;
    }
}
