package com.smartcontact.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smartcontact.model.Contact;
import com.smartcontact.model.User;

public interface ContactRepository extends JpaRepository<Contact, Long> {
	@Query("from Contact as c where c.user.id=:userId ")
	public Page<Contact> findByUser(@Param("userId")Long userId,Pageable pageable);
	
	public List<Contact> findByCnameContainingAndUser(String cname,User user);

}
