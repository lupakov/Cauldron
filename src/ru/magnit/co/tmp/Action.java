package ru.magnit.co.tmp;

import java.util.Date;

public class Action {
	private int artId;
	private int whsId;
	private int frmtId;
	private Date beginDate;
	private Date endDate;
	private int priceType; 
	
	public Action(int artId, int whsId, Date beginDate, Date endDate, int priceType) {
		this.setArtId(artId);
		this.setWhsId(whsId);
		this.setBeginDate(beginDate);
		this.setEndDate(endDate);
		this.setPriceType(priceType);
	}
	
	public Action(int artId, String frmt, Date beginDate, Date endDate, int priceType) {
		this.setArtId(artId);
		this.setFrmtId(getFrmtIdByName(frmt));
		this.setBeginDate(beginDate);
		this.setEndDate(endDate);
		this.setPriceType(priceType);
	}
	
	private int getFrmtIdByName(String name) {
		switch(name) {
		case "ММ":
		case "Сеть ММ":
		case "Сеть МД":
		case "МД":return 1;
		case "Сеть ГМ":
		case "ГМ":return 2;
		case "Сеть МК":
		case "МК":return 3;
		default: return 0;
		}
	}

	public int getArtId() {
		return artId;
	}

	public void setArtId(int artId) {
		this.artId = artId;
	}

	public int getWhsId() {
		return whsId;
	}

	public void setWhsId(int whsId) {
		this.whsId = whsId;
	}

	public int getFrmtId() {
		return frmtId;
	}

	public void setFrmtId(int frmtId) {
		this.frmtId = frmtId;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getPriceType() {
		return priceType;
	}

	public void setPriceType(int priceType) {
		this.priceType = priceType;
	}
	
	
}
