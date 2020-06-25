package com.kakaopay.homework.domain.user;

import com.kakaopay.homework.domain.BaseTimeEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Getter
@Setter
@Entity(name = "TmkMoneyUser")
@IdClass(ThrowUserId.class)
public class ThrowUser extends BaseTimeEntity {

    @Id
    private String UserId;

    @Id
    private String ChatRoomId;

}
