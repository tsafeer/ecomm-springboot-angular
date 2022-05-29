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
		List<String> messageList = new ArrayList<>();
		messageList.add(message);
		this.messages = messageList;
	}
	
	private long lineNo;
	private String field;
	private List<String> messages;
	
	public Errors addMessage(String message) {
		List<String> messageList = this.messages;
		messageList.add(message);
		return this;
	}
	
}
