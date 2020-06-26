package com.kakaopay.homework.domain.money;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MoneyDivisionRepository extends JpaRepository<MoneyDivision, Long> {

    MoneyDivision findByReceivedMoneyUserIdAndToken(String userId, String token);

}
