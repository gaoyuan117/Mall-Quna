
package com.server.api.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("serial")
public class DistinguishAdd extends BaseEntity implements Serializable
{
	@SerializedName("data")
    public String Data;
}
