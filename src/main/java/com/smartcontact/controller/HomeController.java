package com.smartcontact.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartcontact.helper.Message;
import com.smartcontact.model.User;
import com.smartcontact.repository.UserRepository;

@Controller
public class HomeController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("title","Home-Smart Contact Manager");
		return "home";
	}
	@RequestMapping("/about")
	public String about(Model model) {
		model.addAttribute("title","About-Smart Contact Manager");
		return "about";
	}
	@RequestMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title","Register-Smart Contact Manager");
		model.addAttribute("user",new User());
		
		return "signup";
	}
	
	@RequestMapping(value="/do_register",method = RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user")User user,BindingResult res,@RequestParam(value = "agreement",defaultValue = "false")
	 boolean agreement, Model model,HttpSession session) {
      try {
			
			if(!agreement){
				System.out.println("you have not agreed the terms and condition");
				
				throw new Exception("you have not agreed the terms and condition");
			}
			if(res.hasErrors()) {
				System.out.println("Error : "+res.toString());
				model.addAttribute("user",user);
				return "signup";
			}
			user.setRole("ROLE_USER");
			user.setEnable(true);
			user.setImageUrl("default.jpg");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			//System.out.println("Agreement :"+agreement);
			//System.out.println("User : "+user);
			User result=this.userRepository.save(user);
			model.addAttribute("user",new User());
			session.setAttribute("message", new Message("successfully register","alert-success"));
			//return "login";
		}catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message", new Message("something went wrong ","alert-danger"));

			
		}
      return "signup";
	}
	
	@RequestMapping("/signin")
	public String login(Model model) {
		model.addAttribute("title","Login-Smart Contact Manager");
		return "login";
	}
	

}
