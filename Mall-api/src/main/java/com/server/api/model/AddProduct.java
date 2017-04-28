package com.server.api.model;

import java.io.Serializable;


@SuppressWarnings("serial")
public class AddProduct extends BaseEntity implements Serializable
{
	public Data data;
	
	public static class Data
	{
		public int id;
		
		public String verify;
	}
}
