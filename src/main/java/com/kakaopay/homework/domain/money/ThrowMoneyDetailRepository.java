package com.kakaopay.homework.domain.money;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ThrowMoneyDetailRepository extends JpaRepository<ThrowMoneyDetail, String> {

    ThrowMoneyDetail findByToken(String token);

}
