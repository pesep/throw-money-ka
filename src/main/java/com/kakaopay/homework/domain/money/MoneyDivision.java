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

    private String moneyThrowToken;

    private long dividedMoney;

    private String receivedMoneyUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "token", referencedColumnName = "token")
    private ThrowMoneyDetail throwMoneyDetail;

}
