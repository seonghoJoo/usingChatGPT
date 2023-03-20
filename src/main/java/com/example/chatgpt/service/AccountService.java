package com.example.chatgpt.service;

import com.example.chatgpt.domain.Account;
import com.example.chatgpt.domain.AccountUser;
import com.example.chatgpt.domain.type.AccountStatus;
import com.example.chatgpt.domain.type.ErrorCode;
import com.example.chatgpt.dto.AccountDto;
import com.example.chatgpt.exception.AccountException;
import com.example.chatgpt.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountUserRepository accountUserRepository;

    /**
     * 1. 사용자가 있는지 조회
     * 2. 계좌의 valid 체크
     * 3. 계좌 
     * 계좌의 번호를 생성
     * 계좌를 저장, 해당 정보를 넘김
     */
    @Transactional(rollbackFor = {AccountException.class})
    public AccountDto createAccount(Long userId, Long initialBalance) {
        // account_user 에서 user 찾기
        AccountUser accountUser = getAccountUser(userId);

        // account_user 중복된 계좌 3개 초과인지 체크
        validateCreateAccount(accountUser);

        // 순차계좌번호 생성
        String createAccountNumber = accountRepository.findFirstByOrderByIdDesc().map(account -> Integer.parseInt(account.getAccountNumber()) + 1 + "").orElse("1000000000");

        // 계좌 생성
        return AccountDto.fromEntity(
                accountRepository.save(Account.builder()
                        .accountUser(accountUser)
                        .accountStatus(AccountStatus.IN_USE)
                        .accountNumber(createAccountNumber)
                        .balance(initialBalance)
                        .registeredAt(LocalDateTime.now())
                        .build())
        );
    }
    /**
     *  account_user 에서 user 찾기 없다면 예외발생
     * */
    private AccountUser getAccountUser(Long userId){
        return accountUserRepository.findById(userId).orElseThrow(() -> new AccountException(ErrorCode.USER_NOT_FOUND));
    }
    /**
     *  user당 account는 3개 이하를 가져야 한다.
     * */
    private void validateCreateAccount(AccountUser accountUser){
        int cnt = accountRepository.countByAccountUser(accountUser);
        if(cnt>=3){
            throw new AccountException(ErrorCode.MAX_ACCOUNT_PER_USER_3);
        }
    }

    @Transactional(readOnly = true)
    public Account getAccount(Long id) {
        if (id < 0) {
            throw new RuntimeException("Minus");
        }
        return accountRepository.findById(id).get();
    }

    @Transactional
    public AccountDto deleteAccount(Long userId, String accountNumber) {
        AccountUser accountUser = getAccountUser(userId);

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountException(ErrorCode.ACCOUNT_NOT_FOUND));


        validateDeleteAccount(accountUser, account);

        account.setAccountStatus(AccountStatus.UNREGISTERED);
        account.setUnRegisteredAt(LocalDateTime.now());

        accountRepository.save(account);

        return AccountDto.fromEntity(account);
    }

    /**
     *  1. account accountUser 일치 여부 :  불일치시 USER_ACCOUNT_UN_MATCH
     *  2. account 사용여부 :               불일치시 ACCOUNT_ALREADY_UNREGISTERED
     *  3. account 잔액여부 :               없을 시 BALANCE_NOT_EMPTY
     * */
    private void validateDeleteAccount(AccountUser accountUser,Account account){
        if(account.getAccountUser().getId() != accountUser.getId()){
            throw new AccountException(ErrorCode.USER_ACCOUNT_UN_MATCH);
        }
        if(account.getAccountStatus() == AccountStatus.UNREGISTERED){
            throw new AccountException(ErrorCode.ACCOUNT_ALREADY_UNREGISTERED);
        }
        if(account.getBalance() > 0){
            throw new AccountException(ErrorCode.BALANCE_NOT_EMPTY);
        }
    }


    @Transactional
    public List<AccountDto> getAccountsByUserId(Long userId, Long nullvalue) {
        AccountUser accountUser = getAccountUser(userId);

        for(int i=0;i< 100000;i++){
            System.out.println("129381729");
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<Account> accounts = accountRepository
                .findByAccountUser(accountUser);

        return accounts.stream()
                .map(AccountDto::fromEntity)
                .collect(Collectors.toList());
    }
}
