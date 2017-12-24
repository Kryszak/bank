package bsr.project.bank.service.data;

import bsr.project.bank.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
}
