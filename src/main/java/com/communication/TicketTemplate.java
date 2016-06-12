package com.communication;

public class TicketTemplate {

	private Integer id;
	private String name;
	private Integer imageId;
	private String imageUrl;
	private String properties;
	private Long createTime;
	private Integer isDel;
	private Integer sessionId;

	public Integer getSessionId() {
		return sessionId;
	}

	public void setSessionId(Integer sessionId) {
		this.sessionId = sessionId;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setImageId(Integer imageId) {
		this.imageId = imageId;
	}

	public Integer getImageId() {
		return imageId;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}

	public String getProperties() {
		return properties;
	}

	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getCreateTime() {
		return createTime;
	}

	/**
	 * description:
	 */
	@Override
	public String toString() {
		return "TicketTemplate [id=" + id + ", name=" + name + ", imageId=" + imageId + ", imageUrl=" + imageUrl + ", properties=" + properties
				+ ", createTime=" + createTime + ", isDel=" + isDel + ", sessionId=" + sessionId + "]";
	}

}
