package bsr.project.bank.service.data;

import bsr.project.bank.model.Account;
import bsr.project.bank.model.AccountOperation;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AccountHistoryRepository extends CrudRepository<AccountOperation, Integer> {

    List<AccountOperation> findByAccount(Account account);
}
