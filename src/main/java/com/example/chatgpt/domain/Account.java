package com.example.chatgpt.domain;

import com.example.chatgpt.domain.type.AccountStatus;
import com.example.chatgpt.domain.type.ErrorCode;
import com.example.chatgpt.exception.AccountException;
import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.Valid;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Account extends BaseEntity {


    @ManyToOne
    private AccountUser accountUser;

    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Valid
    private AccountStatus accountStatus;
    private Long balance;

    private LocalDateTime registeredAt;
    private LocalDateTime unRegisteredAt;

    /**
     *  useBalance: 계좌에서 amount 만큼 차감
     * */
    public void useBalance(Long amount) {
        if(amount > balance) {
            throw new AccountException(ErrorCode.AMOUNT_EXCEED_BALANCE);
        }
        balance -= amount;
    }

    /**
     *  Transaction 취소
     *  * */
    public void cancelBalance(Long amount) {
        if(amount < 0) {
            throw new AccountException(ErrorCode.INVALID_REQUEST);
        }
        balance += amount;
    }

}
