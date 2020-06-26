package com.kakaopay.homework.domain.money;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "TmkMoneyDivision")
public class MoneyDivision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long seq;

    @Column(nullable = false)
    private String token;

    private long dividedMoney;

    private String receivedMoneyUserId;

    @ManyToOne
    @JoinColumn(name = "throwMoneyDetailToken", referencedColumnName = "token")
    private ThrowMoneyDetail throwMoneyDetail;

}
