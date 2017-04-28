package com.server.api.model;


public class VerifyCode
{
	public int code;

	public String message;
	
	public Data data;
	
	public static class Data
	{
		public String verify;
	}
}
