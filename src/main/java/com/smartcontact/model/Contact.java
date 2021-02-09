package com.smartcontact.model;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="CONTACT")
public class Contact {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long cid;
	@NotBlank(message="Name is required")
	@Size(min=2,message="name should be minimum 2 charecter")
	@Size(max=20,message="name should be maximum 20 charecter")
	private String cname;
	private String cphone;
	private String nickname;
	private String cwwork;
	@Column(unique = true)
	private String cemail;
	@Column(length=600)
	@Size(min=30,message="minimum 30 charecter required about yourself")
	@Size(max=1000,message="maximum 1000 charecter allowed about yourself")
	private String cdescription;
	private String cimageUrl;
    @ManyToOne
    @JsonIgnore
	private User user;
	public Contact() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Long getCid() {
		return cid;
	}
	public void setCid(Long cid) {
		this.cid = cid;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public String getCphone() {
		return cphone;
	}
	public void setCphone(String cphone) {
		this.cphone = cphone;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getCwwork() {
		return cwwork;
	}
	public void setCwwork(String cwwork) {
		this.cwwork = cwwork;
	}
	public String getCemail() {
		return cemail;
	}
	public void setCemail(String cemail) {
		this.cemail = cemail;
	}
	public String getCdescription() {
		return cdescription;
	}
	public void setCdescription(String cdescription) {
		this.cdescription = cdescription;
	}
	public String getCimageUrl() {
		return cimageUrl;
	}
	public void setCimageUrl(String cimageUrl) {
		this.cimageUrl = cimageUrl;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	
	
	

}
