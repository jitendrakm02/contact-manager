package com.smartcontact.controller;

import java.security.Principal;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartcontact.helper.Message;
import com.smartcontact.model.User;
import com.smartcontact.repository.ContactRepository;
import com.smartcontact.repository.UserRepository;
import com.smartcontact.service.EmailService;

@Controller

public class PasswordController {
	
	 Random random = new Random(1000);
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ContactRepository contactRepository;
	@Autowired
	private EmailService emailService;
	
	/*
	 * @ModelAttribute public void addCommonData(Model model,Principal principal) {
	 * String userName=principal.getName(); //System.out.println(userName); User
	 * user=userRepository.getUserByEmail(userName); //System.out.println(user);
	 * model.addAttribute("user",user);
	 * 
	 * 
	 * }
	 */
	//password change handler
	@PostMapping("/user/change_password")
	public String changePassword(@RequestParam("oldPassword") String oldPassword,@RequestParam("newPassword") String newPassword,Principal principal,HttpSession session) {
	    String userName=principal.getName();
	    User user=userRepository.getUserByEmail(userName);
		
	    if(bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
	     user.setPassword(this.bCryptPasswordEncoder.encode(newPassword));	
	     this.userRepository.save(user);
	     session.setAttribute("message", new Message("Password Change Successfully"+user,"alert-success"));
	    }else {
	    	session.setAttribute("message", new Message("Password Change failed check your Old Password is not matching","alert-danger"));
           return "redirect:/user/settings";
	    }
		//System.out.println(oldPassword+" "+newPassword);
		return "normal/user_dashboard";
	}
	//open password change page
	@GetMapping("/user/settings")
	public String setting(Model model,Principal principal) {
		String userName=principal.getName();
		//System.out.println(userName);
		User user=userRepository.getUserByEmail(userName);
		//System.out.println(user);
		model.addAttribute("user",user);
		return "normal/password_change";
		
	}
	
	//email open form
	
	@GetMapping("/email_form")
	public String openEmailForm() {
		return "open_email_form";
		
	}
	//send otp
	@PostMapping("/send_otp")
	public String sendOtp(@RequestParam("email") String email,HttpSession session) {
		System.out.println(email);
		//generating random otp 4 digit
		
		 //int otp = random.nextInt(999999);
		 Integer otp=(int) ((Math.random()*900000)+100000);
		String val=""+otp;
		 System.out.println(val+""+otp);
		//write code to send otp
		 
		 String subject="OTP From SCM";
		 String message=""+"<div>"
		   + "<h1>"
		   +"OTP is :"
		   +"<b>"
		   +val
		   +"</b></n>"
		    +"</h1>"
		    +"</div>";
		 String to=email;
		 boolean flag = this.emailService.sendEmail(subject, message, to);
		 if(flag) {
			 session.setAttribute("serverOtp",otp);
			 session.setAttribute("email",email);
			 return "verify_otp";
		 }else 
		 {
			 session.setAttribute("message", "Check your email id");
		     return "redirect:/signin";
		 }
		
	}
	//verify otp
	@PostMapping("/verify_otp")
	public String verifyOtp(@RequestParam("otp") int otp,HttpSession session) {
		int serverOtp=(int)session.getAttribute("serverOtp");
		String email=(String)session.getAttribute("email");
		System.out.println("serverOtp"+serverOtp+" otp"+otp);
		if(serverOtp==otp){
			User user=this.userRepository.getUserByEmail(email);
			if(user==null) {
				session.setAttribute("message", " your email id does not exist ");
				//session.setAttribute("message", new Message("message","your email id does not exist"));
			    return "open_email_form";
			}else {
				
				return "change_password";
			}
		}else {
		session.setAttribute("message", " You have entered wrong OTP Check your email id ");
		return "verify_otp";
		}
		
		
	}
	
	//change password
	@PostMapping("/change_password")
	public String changePassword(@RequestParam("newPassword") String newPassword,HttpSession session) {
		String email=(String) session.getAttribute("email");
		User user = this.userRepository.getUserByEmail(email);
		user.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
		this.userRepository.save(user);
		return "redirect:signin?change=password changed successfully....";
		
	}

}
