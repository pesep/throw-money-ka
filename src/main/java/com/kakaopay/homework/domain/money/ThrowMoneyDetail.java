package com.kakaopay.homework.domain.money;

import com.kakaopay.homework.domain.BaseTimeEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity(name = "TmkMoneyThrowDetail")
public class ThrowMoneyDetail extends BaseTimeEntity {

    @Id
    private String token;

    private String groupChatId;

    private String moneyMaker;

    private long totalMoney;

    private int throwPeople;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "throwMoneyDetail")
    private List<MoneyDivision> moneyDivisionList;


}
