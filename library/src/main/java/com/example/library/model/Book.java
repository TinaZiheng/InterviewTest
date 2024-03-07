package com.example.library.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Book {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int Id;
	
	@Column
	private String name;
	
	@Column
	private String remark;
	
	public int getId() {
		return Id;
	}
	
	public void setId(int id) {
		Id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getRemark() {
		return name;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Book(int id, String name, String remark) {
		super();
		Id = id;
		this.name = name;
		this.remark = remark;
	}
	
	public Book(String name, String remark) {
		this(0, name, remark);
	}

	public Book() {
		super();
	}

}
