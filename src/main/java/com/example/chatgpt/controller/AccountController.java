package com.example.chatgpt.controller;

import com.example.chatgpt.domain.Account;
import com.example.chatgpt.dto.AccountInfo;
import com.example.chatgpt.dto.CreateAccount;
import com.example.chatgpt.dto.DeleteAccount;
import com.example.chatgpt.service.AccountService;
import jakarta.validation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/account")
    public CreateAccount.Response createAccount(
            @RequestBody @Valid CreateAccount.Request request
    ) {

        return CreateAccount.Response.from(
                accountService.createAccount(
                        request.getUserId(),
                        request.getInitialBalance()
                )
        );
    }

    @DeleteMapping("/account")
    public DeleteAccount.Response deleteAccount(
            @RequestBody @Valid DeleteAccount.Request request
    ) {
        return DeleteAccount.Response.from(
                accountService.deleteAccount(
                        request.getUserId(),
                        request.getAccountNumber()
                )
        );
    }

    @GetMapping("/account")
    public List<AccountInfo> getAccountByUserId(
            @RequestParam("user_id") Long userId
    ) {
        for(int i=0;i<100000;i++){
            System.out.println("just log");
            // bad comment
        }
        accountService.getAccountsByUserId(userId);
        accountService.getAccountsByUserId(userId);
        accountService.getAccountsByUserId(userId);

        return accountService.getAccountsByUserId(userId)
                .stream().map(accountDto ->
                        AccountInfo.builder()
                        .accountNumber(accountDto.getAccountNumber())
                        .balance(accountDto.getBalance())
                        .build())
                .collect(Collectors.toList());
    }

    @GetMapping("/account/{id}")
    public Account getAccount(
            @PathVariable Long id) {
        return accountService.getAccount(id);
    }
}
