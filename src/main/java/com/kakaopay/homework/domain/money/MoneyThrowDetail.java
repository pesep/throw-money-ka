package com.kakaopay.homework.domain.money;

import com.kakaopay.homework.domain.BaseTimeEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity(name = "TmkMoneyThrowDetail")
public class MoneyThrowDetail extends BaseTimeEntity {

    @Id
    private String token;

    private String groupChatId;

    private String moneyMaker;

    private long totalMoney;

    private int throwCount;

    private boolean isComplete;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "moneyThrowDetail")
    private List<MoneyDivision> moneyDivision;


}
