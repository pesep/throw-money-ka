package com.kakaopay.homework.domain.user;


import lombok.EqualsAndHashCode;

import javax.persistence.Id;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = false)
public class MoneyUserId implements Serializable {

    @Id
    private String UserId;

    @Id
    private String ChatRoomId;

}
