
package com.server.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@SuppressWarnings("serial")
public class HomeNoticeDetail extends BaseEntity implements Serializable {

	@SerializedName("data")
	public HomeNotice.NoticeItem Data;

}
