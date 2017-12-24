package bsr.project.bank.service.data;

import bsr.project.bank.model.AccountOperation;
import org.springframework.data.repository.CrudRepository;

public interface AccountHistoryRepository extends CrudRepository<AccountOperation, Integer> {
}
