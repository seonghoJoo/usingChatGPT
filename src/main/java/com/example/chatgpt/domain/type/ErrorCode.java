package com.example.chatgpt.domain.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER_ERROR("Internal server error"),
    INVALID_REQUEST("Invalid request"),
    USER_NOT_FOUND("user not found"),
    ACCOUNT_NOT_FOUND("account not found"),
    ACCOUNT_TRANSACTION_LOCK("already used account"),
    TRANSACTION_NOT_FOUND("can not found transaction"),
    AMOUNT_EXCEED_BALANCE("amount more than account balance"),
    TRANSACTION_ACCOUNT_UN_MATCH("transaction unmatched"),
    CANCEL_MUST_FULLY("can not partial pay"),
    TOO_OLD_ORDER_TO_CANCEL("can not cancel after 10 years"),
    USER_ACCOUNT_UN_MATCH("user account unmatch"),
    ACCOUNT_ALREADY_UNREGISTERED("already terminated"),
    BALANCE_NOT_EMPTY("balance not empty"),
    MAX_ACCOUNT_PER_USER_3("max account is 3"),
    ;

    private final String description;
}
