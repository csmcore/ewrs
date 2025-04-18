package app.ewarehouse.service;

import app.ewarehouse.entity.Bank;
import app.ewarehouse.entity.LegalStatus;

import java.util.List;

public interface BankService {
    Integer save(String bank);
    Bank getById(Integer Id);
    List<Bank> getAll();
}
