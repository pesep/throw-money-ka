package com.kakaopay.homework.domain.money;

import com.kakaopay.homework.domain.BaseTimeEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;

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

//    @Type(type = "yes_no")
//    @Column(length = 1, name = "completeYn")
//    @ColumnDefault("'N'")
//    private boolean isComplete;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "throwMoneyDetail")
    private List<MoneyDivision> moneyDivisionList;


}
