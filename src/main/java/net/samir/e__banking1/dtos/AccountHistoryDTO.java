package net.samir.e__banking1.dtos;


import lombok.Data;
import net.samir.e__banking1.enums.OperationType;

import java.util.List;

@Data
public class AccountHistoryDTO {
private String AccountId;
private double Balance;
private int currentPage;
private int totalPages;
private int pageSize;
private List<AccountOperationDTO> AccountOperationDTOS;



}
