package net.slipp.web;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Question {
	@Id
	@GeneratedValue
	private Long id;
	
	private String writer;
	
	private String title;
	
	private String content;
	
	public Question() {} //JPA requires this default constructor

	public Question(String writer, String title, String content) {
		this.writer = writer;
		this.title = title;
		this.content = content;
	}
	
}
