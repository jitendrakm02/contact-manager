package com.smartcontact.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


import com.razorpay.*;

import com.smartcontact.helper.Message;
import com.smartcontact.model.Contact;
import com.smartcontact.model.MyOrder;
import com.smartcontact.model.User;
import com.smartcontact.repository.ContactRepository;
import com.smartcontact.repository.MyOrderRepository;
import com.smartcontact.repository.UserRepository;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ContactRepository contactRepository;
	@Autowired
	private MyOrderRepository myOrderRepo;
	@ModelAttribute
	public void addCommonData(Model model,Principal principal) {
		String userName=principal.getName();
		System.out.println(userName);
		User user=userRepository.getUserByEmail(userName);
		//System.out.println(user);
		model.addAttribute("user",user);
		
		
	}
	//dashboard home
	@RequestMapping(value="/index")
	public String dashboard(Model model,Principal principal) {
		model.addAttribute("title", "User Dashboard");
		return "normal/user_dashboard";
		
	}
	@RequestMapping(value="/add_contact" )
	public String openAddContactForm(Model model) {
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact",new Contact());
		return "normal/add_contact_form";
		
	}
	//adding data into database of contact page
	@PostMapping("/do_contact_register")
	public String addContact(@ModelAttribute Contact contact,
			@RequestParam("cimage") MultipartFile file
			,Principal principal,HttpSession session) {
		try {
		String name=principal.getName();
		User user=userRepository.getUserByEmail(name);
		//file uploading 
		if(file.isEmpty()) {
			System.out.println("File is empty");
			contact.setCimageUrl("default.jpg");
		}else {
			contact.setCimageUrl(file.getOriginalFilename());
			File saveFile=new ClassPathResource("static/img").getFile();
			Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
			Files.copy(file.getInputStream(), path,StandardCopyOption.REPLACE_EXISTING);
			
		}
		
		contact.setUser(user);
		user.getContact().add(contact);
		//System.out.println(contact);
		userRepository.save(user);
		session.setAttribute("message", new Message("successfully contact added.!!add more contact..","alert-success"));
		}catch(Exception e) {
			System.out.println("Error :"+e.getMessage());
			e.printStackTrace();
			session.setAttribute("message", new Message("something went wrong.!!try again..","alert-danger"));
		}
		return "normal/add_contact_form";
		
	}
	//fetch all details from database
	//abhishek  Ranchi  8603257454
	@GetMapping("/show_contacts/{page}")
	public String contactDetails(@PathVariable("page") Integer page,Model model,Principal principal) {
		model.addAttribute("title", "Show Contact-Smart Contact Manager");
		String userName=principal.getName();
		User user=userRepository.getUserByEmail(userName);
		Pageable pageable=PageRequest.of(page, 5);
		
		Page<Contact> contacts=contactRepository.findByUser(user.getId(),pageable);
		model.addAttribute("contacts", contacts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages",contacts.getTotalPages());
		
		return "normal/show_contacts";
	}
	//showing particular contact details
	
	@GetMapping("/{cid}/contact")
	public String singleContactDetails(@PathVariable("cid") Long cid,Model model,Principal principal) {
		
		//System.out.println("CId :"+cid);
		Optional<Contact> contactOptional = contactRepository.findById(cid);
		 Contact contact = contactOptional.get();
		 String userName=principal.getName();
		 User user=userRepository.getUserByEmail(userName);
		 if(user.getId()==contact.getUser().getId())
		 {
		 model.addAttribute("contact", contact);
		 model.addAttribute("title", contact.getCname());
		 }
		 return "normal/contact_details";
		
	}

	//delete handler
	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") Long cid,Model model,Principal principal,HttpSession session) {
		
		Optional<Contact> contactOptional = contactRepository.findById(cid);
		Contact contact=contactOptional.get();
		
		String userName=principal.getName();
		User user=userRepository.getUserByEmail(userName);
		
		if(user.getId()==contact.getUser().getId()) {
			contact.setUser(null);
			contactRepository.delete(contact);
			model.addAttribute("contact deleted successfully", "success");
			session.setAttribute("message",new Message("contact deleted successfully", "success"));
			
		}else {
			session.setAttribute("message",new Message("contact not deleted...user have not access to delete other person contact", "danger"));
		}
		
		return "redirect:/user/show_contacts/0";
		
	}
	//open update form 
	@PostMapping("/update_contacts/{cid}")
	public String updateForm(@PathVariable("cid") Long cid,Model model) {
		model.addAttribute("title", "Update-Smart Contact Manager");
		Contact contact=contactRepository.findById(cid).get();
		model.addAttribute("contact",contact);
		
		return "normal/update_form";
		
	}
	//update handler data
	
	@RequestMapping(value="/do_contact_update",method = RequestMethod.POST)
	public String updateContact(@ModelAttribute Contact contact,@RequestParam("cimage") MultipartFile file
			,Model model,Principal principal,HttpSession session) {
		try {
			//old contact details
			Contact oldContact=contactRepository.findById(contact.getCid()).get();
			
			if(!file.isEmpty()) {
				
				//code for update image
				//delete old image if new is added
				File deleteFile=new ClassPathResource("static/img").getFile();
				File file1=new File(deleteFile,oldContact.getCimageUrl());
				file1.delete();
				//update new image 
				File saveFile=new ClassPathResource("static/img").getFile();
				Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
				Files.copy(file.getInputStream(), path,StandardCopyOption.REPLACE_EXISTING);
				
				contact.setCimageUrl(file.getOriginalFilename());
				
			}else {
				contact.setCimageUrl(oldContact.getCimageUrl());
			}
			User user=userRepository.getUserByEmail(principal.getName());
			contact.setUser(user);
			this.contactRepository.save(contact);
			//model.addAttribute("contact", contact);
			session.setAttribute("message", new Message("contact update successfully", "success"));
		}catch(Exception e) {
			e.printStackTrace();
			session.setAttribute("message", new Message("contact not update ", "danger"));
		}
		//System.out.println(contact);
		//System.out.println(contact.getCemail()+contact.getCimageUrl()+contact.getCphone());
		//System.out.println(contact);
		return "redirect:/user/"+contact.getCid()+"/contact";
		
	}
	//view your profile
	@GetMapping("/profile")
	public String viewProfile(Model model) {
		model.addAttribute("title", "Your Profile-Smart Contact Manager");
		return "normal/view_profile";
	}
	//creating order for payment
	@PostMapping("/create_order")
	@ResponseBody
	public String createOrder(@RequestBody Map<String, Object> data,Principal principal) throws Exception {
		//System.out.println(data);
		int amount=Integer.parseInt(data.get("amount").toString());
		//System.out.println(amount);
		//creating razorpay client test key
		//String key="rzp_test_XRRWklbEDFgJly";
		//String secret="8CfwsXW5ebHIcnz7P7q5cGHl";
		
		RazorpayClient client = new RazorpayClient("rzp_test_XRRWklbEDFgJly", "8CfwsXW5ebHIcnz7P7q5cGHl");
	   //System.out.println("client :"+client);
		JSONObject obj=new JSONObject();
		obj.put("amount", amount*100);
		obj.put("currency", "INR");
		obj.put("receipt", "txn_235425");
		
		//create order 
		Order order =client.Orders.create(obj);
		//System.out.println(order);
		//save order to mysql database
		
		MyOrder mo=new MyOrder();
		mo.setAmount(order.get("amount")+"");
		mo.setOrderId(order.get("id"));
		mo.setCurrency(order.get("currency"));
		mo.setReceipt(order.get("receipt"));
		mo.setStatus(order.get("status"));
		mo.setUser(this.userRepository.getUserByEmail(principal.getName()));
		mo.setPaymentId(null);
		
		this.myOrderRepo.save(mo);
		
		return order.toString();
		
	}
	//update payment id on server
	
	@PostMapping("/update_order")
	public ResponseEntity<?> updatePayment(@RequestBody Map<String,Object> data) {
		MyOrder myorder=this.myOrderRepo.findByOrderId(data.get("order_id").toString());
		System.out.println(myorder);
		myorder.setPaymentId(data.get("payment_id").toString());
		//myorder.setOrderId(data.get("order_id").toString());
		myorder.setStatus(data.get("status").toString());
		this.myOrderRepo.save(myorder);
		System.out.println(data);
		return ResponseEntity.ok(Map.of("msg","updated"));
		
	}
	
	
	//end of class braces
}
