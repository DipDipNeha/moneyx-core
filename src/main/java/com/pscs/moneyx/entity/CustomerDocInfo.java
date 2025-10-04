package com.pscs.moneyx.entity;

import java.sql.Clob;
import java.util.Date;

import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "MONEYX_DOC_INFO")
@DynamicUpdate
public class CustomerDocInfo {

	@Id	
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String documentType;
	private Clob fileData;
	private String filePath;
	private String description;
	private String blog;
	private String blogCategory;
	private Date createdDttm;
	private String createdBy;
	private Date updatedDttm;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDocumentType() {
		return documentType;
	}
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	public Clob getFileData() {
		return fileData;
	}
	public void setFileData(Clob fileData) {
		this.fileData = fileData;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getBlog() {
		return blog;
	}
	public void setBlog(String blog) {
		this.blog = blog;
	}
	public String getBlogCategory() {
		return blogCategory;
	}
	public void setBlogCategory(String blogCategory) {
		this.blogCategory = blogCategory;
	}
	public Date getCreatedDttm() {
		return createdDttm;
	}
	public void setCreatedDttm(Date createdDttm) {
		this.createdDttm = createdDttm;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getUpdatedDttm() {
		return updatedDttm;
	}
	public void setUpdatedDttm(Date updatedDttm) {
		this.updatedDttm = updatedDttm;
	}
	@Override
	public String toString() {
		return "Document [id=" + id + ", documentType=" + documentType + ", fileData=" + fileData + ", filePath="
				+ filePath + ", description=" + description + ", blog=" + blog + ", blogCategory=" + blogCategory
				+ ", createdDttm=" + createdDttm + ", createdBy=" + createdBy + ", updatedDttm=" + updatedDttm + "]";
	}
	
	
	
	
	
}
