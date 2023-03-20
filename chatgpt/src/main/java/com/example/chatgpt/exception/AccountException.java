package com.example.chatgpt.exception;

import com.example.chatgpt.domain.type.ErrorCode;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountException extends RuntimeException {
    private ErrorCode errorCode;
    private String errorMessage;

    public AccountException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }

}
