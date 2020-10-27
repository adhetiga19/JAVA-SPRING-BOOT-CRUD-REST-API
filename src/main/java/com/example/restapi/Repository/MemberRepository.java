package com.example.restapi.Repository;

import com.example.restapi.Model.MemberModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import javax.transaction.Transactional;

public interface MemberRepository extends JpaRepository<MemberModel, Long> {
    @Query("FROM Tbl_Member WHERE PhoneNumber = ?1 OR Email = ?2")
    MemberModel findUserByPhoneOrEmail(String phoneNumber, String email);

    @Transactional
    @Modifying
    @Query("UPDATE Tbl_Member SET SessionId = ?1 WHERE PhoneNumber = ?2 OR Email = ?3")
    public void UpdateLastActiveMember(String currentTime, String phoneNumber, String email);
}
