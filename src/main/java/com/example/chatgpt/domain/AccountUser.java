package com.example.chatgpt.domain;

import lombok.*;

import jakarta.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class AccountUser extends BaseEntity {

    private String name;
}
