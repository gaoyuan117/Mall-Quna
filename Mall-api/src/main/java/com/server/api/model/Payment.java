
package com.server.api.model;

import com.google.gson.annotations.SerializedName;

public class Payment extends BaseEntity
{

	@SerializedName("data")
	public PaymentData Data;

	public static class PaymentData{

	}
}
