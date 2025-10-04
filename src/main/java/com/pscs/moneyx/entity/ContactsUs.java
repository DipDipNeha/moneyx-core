/**
 * @author Dipak
 */

package com.pscs.moneyx.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "Moneyx_core_ContactsUs")
public class ContactsUs {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	private String email;
	private String subject;
	private String message;
	private Date createdDttm;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Date getCreatedDttm() {
		return createdDttm;
	}
	public void setCreatedDttm(Date createdDttm) {
		this.createdDttm = createdDttm;
	}
	@Override
	public String toString() {
		return "ContactsUs [id=" + id + ", name=" + name + ", email=" + email + ", subject=" + subject + ", message="
				+ message + ", createdDttm=" + createdDttm + "]";
	}
	
	
	

}
