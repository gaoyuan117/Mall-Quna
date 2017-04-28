
package com.server.api.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("serial")
public class DistinguishDetail extends BaseEntity implements Serializable
{
	@SerializedName("data")
    public DistinguishItem Data;
}
