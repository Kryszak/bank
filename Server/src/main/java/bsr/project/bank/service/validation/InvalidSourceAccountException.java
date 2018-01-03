package bsr.project.bank.service.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class InvalidSourceAccountException extends Exception {

    @Getter
    private String description;

}
