package net.samir.e__banking1.dtos;

import lombok.Data;
import net.samir.e__banking1.enums.OperationType;

import java.sql.Date;


@Data
public class AccountOperationDTO {
    private long id;
    private Date operationDate;
    private double amount;
    private OperationType operationType;
    private String description;



}
