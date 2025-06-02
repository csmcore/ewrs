package app.ewarehouse.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class UserIdConstants {

    @Value("${role.OIC_FINANCE}")
    private int oicFinance;

    @Value("${role.OIC}")
    private int oic;

    @Value("${role.INSPECTOR}")
    private int inspector;

    @Value("${role.APPROVER}")
    private int approver;

    @Value("${role.CEO}")
    private int ceo;

    @Value("${role.WAREHOUSE_OPERATOR}")
    private int warehouseOperator;

    @Value("${role.WAREHOUSE_MANAGER}")
    private int warehouseManager;

    @Value("${role.WAREHOUSE_CLERK}")
    private int warehouseClerk;

    @Value("${role.DIRECTOR}")
    private int director;

    @Value("${role.TECHNICAL}")
    private int technical;

    @Value("${role.REVENUE}")
    private int revenue;

    @Value("${role.CLC}")
    private int clc;

    @Value("${role.CECM}")
    private int cecm;

    @Value("${role.FINANCE_OFFICER}")
    private int financeOfficer;

    @Value("${role.VERIFICATION_OFFICER}")
    private int verificationOfficer;

    @Value("${role.MANAGER_RC}")
    private int managerRc;

    @Value("${role.FOOD_CROPS_DIRECTORATE}")
    private int foodCropsDirectorate;

    @Value("${role.MANAGER_RCC}")
    private int managerRcc;

    @Value("${role.COMPLAINT}")
    private int complaint;

    @Value("${role.APPLICANT}")
    private int applicant;

    @Value("${role.BANK}")
    private int bank;

    @Value("${role.OIC_COMPLIANCE}")
    private int oicCompliance;

    @Value("${role.DISPUTE_OFFICER}")
    private int disputeOfficer;

    @Value("${role.OIC_LEGAL}")
    private int oicLegal;
}
