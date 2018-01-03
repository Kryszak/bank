package bsr.project.bank.service.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class InvalidTitleException extends Exception {

    @Getter
    private String description;

}
