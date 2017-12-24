package bsr.project.bank.utility;

import bsr.project.bank.model.User;
import bsr.project.bank.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody User user) {
        log.info(">>> createUser {}", user);
        userService.createUser(user);
        log.info("<<< createUser");
    }
}
