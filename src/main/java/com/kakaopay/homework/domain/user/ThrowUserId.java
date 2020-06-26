package com.kakaopay.homework.domain.user;


import lombok.EqualsAndHashCode;

import javax.persistence.Id;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = false)
public class ThrowUserId implements Serializable {

    @Id
    private String userId;

    @Id
    private String chatRoomId;

}
