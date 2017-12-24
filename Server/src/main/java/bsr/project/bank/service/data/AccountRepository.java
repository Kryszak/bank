package bsr.project.bank.service.data;

import bsr.project.bank.model.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AccountRepository extends CrudRepository<Account, String> {

    List<Account> findAll();
}
