package com.smartcontact.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smartcontact.model.Contact;
import com.smartcontact.model.User;
import com.smartcontact.repository.ContactRepository;
import com.smartcontact.repository.UserRepository;

@RestController
public class SearchController {
	
	@Autowired
	private UserRepository userRepositry;
	@Autowired
	private ContactRepository contactRepositry;
	//search handler
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query") String query,Principal principal) {
		User user=userRepositry.getUserByEmail(principal.getName());
		
		List<Contact> contacts = contactRepositry.findByCnameContainingAndUser(query, user);
		
		return ResponseEntity.ok(contacts);
		
	}

}
