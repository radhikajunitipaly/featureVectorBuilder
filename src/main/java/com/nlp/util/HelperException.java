package com.nlp.util;

import org.apache.commons.lang3.StringUtils;


/**
 *  This is thrown from Helper objects to other objects,
 *  and contains an extra message suitable for the end user.
 * 
 *  A "technical message" is available too - it's just 
 *  the wrapped exceptions' messages, separated by newlines.
 */
public class HelperException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String endUserMessage;

	
	public HelperException(String endUserMessage, Throwable e) {
		super(endUserMessage, e);
		this.endUserMessage = endUserMessage;
	}
	
	public HelperException(String endUserMessage) {
		super(endUserMessage); // It's all we have
		this.endUserMessage = endUserMessage;
	}

	public String getEndUserMessage() {
		String res = "";
		if (endUserMessage != null) {
			res = endUserMessage;
		}
		return res;
	}

	public String getTechnicalMessage() {
		
		StringBuilder sb = new StringBuilder("");
		Throwable cause = getCause();
		String sep = "";
		int depth = 0;
		while (cause != null) {
			if (! StringUtils.isBlank(cause.getMessage())) {
				depth += 1;
				sb.append(sep);
				sb.append("[");
				sb.append(depth);
				sb.append("] ");
				sb.append(cause.getMessage());
				sep = "\n";
			}
			cause = cause.getCause();
		}
		return sb.toString();
	}
}

