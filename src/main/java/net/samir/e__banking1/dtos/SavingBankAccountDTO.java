package net.samir.e__banking1.dtos;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.samir.e__banking1.enums.AccountStatus;

import java.util.Date;

@Data
public class SavingBankAccountDTO extends BankAccountDTO {
    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private double interstRate;


}
