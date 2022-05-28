/**
 * 
 */
package com.demo.ecommerce.vm;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * @author User
 *
 */
@Data
@JsonInclude(Include.NON_EMPTY)
public class ResponseVm {

	public ResponseVm(){}

	public ResponseVm(int status, String message){
		this.status = status;
		this.message = message;
	}

	public ResponseVm(int status, String message, List<Errors> errors){
		this.status = status;
		this.message = message;
		this.errors = errors;
	}

	private int status;
	private String message;
	private List<Errors> errors;

}
