package com.demo.ecommerce.vm;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * @author User
 *
 */
@Data
public class Errors {
	
	public Errors(long lineNo, String field, String message) {
		this.lineNo = lineNo;
		this.field = field;
		List<String> messages = new ArrayList<>();
		messages.add(message);
		this.messages = messages;
	}
	
	private long lineNo;
	private String field;
	private List<String> messages;
	
//	public Errors addMessage(int lineNo, String field, String message) {
	public Errors addMessage(String message) {
		List<String> messages = this.messages;
//		if(messages == null) {
//			this.lineNo = lineNo;
//			this.field = field;
//			messages = new ArrayList<>();
//			messages.add(message);
//			this.messages = messages;
//		}else {
		messages.add(message);
//		}
		return this;
	}
	
}
