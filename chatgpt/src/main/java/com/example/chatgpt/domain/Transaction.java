package com.example.chatgpt.domain;

import com.example.chatgpt.domain.type.TransactionResultType;
import com.example.chatgpt.domain.type.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Transaction extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    @Enumerated(EnumType.STRING)
    private TransactionResultType transactionResultType;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    private Long amount;
    private Long balanceSnapshot;

    private String transactionId;

    @LastModifiedDate
    private LocalDateTime transactedAt;
}
