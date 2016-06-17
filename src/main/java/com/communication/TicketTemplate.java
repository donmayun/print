package com.communication;

import java.io.Serializable;

import com.print.parse.model.PrintSetUp;

public class TicketTemplate implements Serializable {

	private transient static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private Integer imageId;
	private String imageUrl;
	private String properties;
	private Long createTime;
	private Integer isDel;
	private Integer sessionId;
	private Integer ticketWidth;
	private Integer ticketHeight;
	private Integer printDensity;
	private Integer ticketType;
	private Integer gapWidth;
	private Integer printOffset;

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

	public Integer getTicketWidth() {
		return ticketWidth;
	}

	public void setTicketWidth(Integer ticketWidth) {
		this.ticketWidth = ticketWidth;
	}

	public Integer getTicketHeight() {
		return ticketHeight;
	}

	public void setTicketHeight(Integer ticketHeight) {
		this.ticketHeight = ticketHeight;
	}

	public Integer getPrintDensity() {
		return printDensity;
	}

	public void setPrintDensity(Integer printDensity) {
		this.printDensity = printDensity;
	}

	public Integer getTicketType() {
		return ticketType;
	}

	public void setTicketType(Integer ticketType) {
		this.ticketType = ticketType;
	}

	public Integer getGapWidth() {
		return gapWidth;
	}

	public void setGapWidth(Integer gapWidth) {
		this.gapWidth = gapWidth;
	}

	public Integer getPrintOffset() {
		return printOffset;
	}

	public void setPrintOffset(Integer printOffset) {
		this.printOffset = printOffset;
	}

	/**
	 * description:
	 *
	 * @return
	 *
	 * @author don
	 * @date 2016年6月17日 上午9:16:31
	 */
	public PrintSetUp getPrintSetUp() {
		PrintSetUp psu = new PrintSetUp();
		psu.setDensity(printDensity);
		psu.setGapWidth(gapWidth);
		psu.setWidth(ticketWidth);
		psu.setHeight(ticketHeight);
		psu.setPrintOffset(printOffset);
		psu.setType(ticketType);
		return psu;
	}

}
