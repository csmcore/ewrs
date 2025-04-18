package app.ewarehouse.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ErrorMessages {
    @Value("${error.notfound}")
    private String errorMessageNotFound;
    @Value("${error.passportExsits}")
    private String passportExsits;
    @Value("${error.emailExists}")
    private String emailExists;
    @Value("${error.phoneNoExists}")
    private String phoneNoExists;
    @Value("${error.companyEmailExists}")
    private String companyEmailExists;
    @Value("${error.kraPinExists}")
    private String kraPinExists;
    @Value("${error.companyRegExists}")
    private String companyRegExists;
    @Value("${error.buyer-application-exists}")
    private String buyerApplicationExists;
    @Value("${error.generalDuplicateRecords}")
    private String generalDuplicateRecords;
    @Value("${error.unknownError}")
    private String unknownError;
    @Value("${error.subCountyExists}")
    private String subCountyExists;
    @Value("${error.constraintError}")
    private String constraintError;

    @Value("${error.message.duplicate-country-name}")
    private String duplicateCountryName;
    @Value("${error.message.duplicate-country-code}")
    private String duplicateCountryCode;
    @Value("${error.message.entity-not-found}")
    private String entityNotFound;
    @Value("${error.message.internal-server-error}")
    private String internalServerError;
    @Value("${error.message.not-authorized}")
    private String notAuthorized;
    @Value("${error.message.complaint-resolution-forwarded}")
    private String alreadyForwarded;
    @Value("${error.message.complaint-resolution-verdict}")
    private String alreadyResolved;
    @Value("${error.message.complaint-resolution-invalid-verdict}")
    private String invalidVerdict;

    @Value("${error.message.failed-fetching-commodites}")
    private String failedFetchingCommodities;
    @Value("${error.message.failed-fetching-commodity}")
    private String failedFetchingCommodity;

    @Value("${error.message.duplicate-county-name}")
    private String duplicateCountyName;
    @Value("${error.message.only_pending_can_be_retired_cancelled_or_delivered}")
    private String onlyPendingRecipts;
    @Value("${error.message.failed-to-fetch}")
    private String failedToFetch;
    @Value("${error.message.already-validated}")
    private String alreadyValidated;
    @Value("${error.message.no-such-commodity}")
    private String noSuchCommodity;
    @Value("${error.message.depositor-not-existent}")
    private String depositorNonExistent;
    @Value("${error.message.valid-commodity-id}")
    private String validCommodityId;
    @Value(("${error.message.valid-depositor}"))
    private  String validDepositorId;
    @Value("${error.message.contact-not-unique}")
    private String contactNotUnique;
    @Value("${error.message.receipt-not-found}")
    private  String receiptNotFound;
    @Value("${error.message.approved-aoc-not-found}")
    private String approvedAocNotFound;

    @Value("${error.message.receipt-not-pending}")
    private String receiptStatusNotPending;
    @Value(("${error.message.particulars-not-found}"))
    private String particularsNotFound;
    @Value("${error.message.split-receipt}")
    private String splitReceiptNotFound;
    @Value("${error.message.depositor-not-valid}")
    private String depositorNotValid;

    @Value("${error.message.charges-calculator}")
    private String noChargesCalculator;

    @Value(("${error.message.invoice-payment}"))
    private String hasBuyerPayed;

    @Value("${error.message.invoice-not-found}")
    private String invoiceNotFound;
}