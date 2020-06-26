package com.kakaopay.homework.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ThrowUserRepository extends JpaRepository<ThrowUser, String> {

    ThrowUser findByUserIdAndChatRoomId(String userId, String chatRoomId);

}
