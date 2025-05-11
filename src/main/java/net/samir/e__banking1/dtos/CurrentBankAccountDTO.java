package net.samir.e__banking1.dtos;

import lombok.Data;
import net.samir.e__banking1.enums.AccountStatus;

import java.sql.Date;

@Data
public class CurrentBankAccountDTO extends BankAccountDTO{
    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private double overDraft;
}
