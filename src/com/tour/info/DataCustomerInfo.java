package com.tour.info;

import java.io.Serializable;

/**
 * ÂÃ¿Í
 * @author ly
 */
public class DataCustomerInfo implements Serializable {
	int ctId;
	String ctTitle;
	String ctType;
	String ctType_remark;
	String ctSex;
	String ctAge;
	String ctBirthday;
	String ctIdno;
	String ctPhone;
	String ctMail;
	String ctAddr;
	int ctTid;
	String ctSeat;
	String ctPlace;
	String ctRemark;
	String ctTeam;
	boolean absent;
    public DataCustomerInfo(int ctId,String ctTitle, String ctSeat,String ctType,String ctType_remark,String ctSex,String ctAge,String ctIdno,String ctPhone,String ctPlace,String ctRemark,String ctTeam,boolean absent){
    	this.ctId=ctId;
    	this.ctTitle=ctTitle;
    	this.ctSeat=ctSeat;
    	this.ctType=ctType;
		this.ctType_remark=ctType_remark;
		this.ctSex=ctSex;
		this.ctAge=ctAge;
		this.ctIdno=ctIdno;
		this.ctPhone=ctPhone;
		this.ctPlace=ctPlace;
		this.ctRemark=ctRemark;
		this.ctTeam=ctTeam;
		this.absent=absent;
    }
	public int getCtId() {
		return ctId;
	}

	public void setCtId(int ctId) {
		this.ctId = ctId;
	}

	public String getCtTitle() {
		return ctTitle;
	}

	public void setCtTitle(String ctTitle) {
		this.ctTitle = ctTitle;
	}

	public String getCtType() {
		return ctType;
	}

	public void setCtType(String ctType) {
		this.ctType = ctType;
	}

	public String getCtType_remark() {
		return ctType_remark;
	}

	public void setCtType_remark(String ctType_remark) {
		this.ctType_remark = ctType_remark;
	}

	public String getCtSex() {
		return ctSex;
	}

	public void setCtSex(String ctSex) {
		this.ctSex = ctSex;
	}

	public String getCtAge() {
		return ctAge;
	}

	public void setCtAge(String ctAge) {
		this.ctAge = ctAge;
	}

	public String getCtBirthday() {
		return ctBirthday;
	}

	public void setCtBirthday(String ctBirthday) {
		this.ctBirthday = ctBirthday;
	}

	public String getCtIdno() {
		return ctIdno;
	}

	public void setCtIdno(String ctIdno) {
		this.ctIdno = ctIdno;
	}

	public String getCtPhone() {
		return ctPhone;
	}

	public void setCtPhone(String ctPhone) {
		this.ctPhone = ctPhone;
	}

	public String getCtMail() {
		return ctMail;
	}

	public void setCtMail(String ctMail) {
		this.ctMail = ctMail;
	}

	public String getCtAddr() {
		return ctAddr;
	}

	public void setCtAddr(String ctAddr) {
		this.ctAddr = ctAddr;
	}

	public int getCtTid() {
		return ctTid;
	}

	public void setCtTid(int ctTid) {
		this.ctTid = ctTid;
	}

	public String getCtSeat() {
		return ctSeat;
	}

	public void setCtSeat(String ctSeat) {
		this.ctSeat = ctSeat;
	}

	public String getCtPlace() {
		return ctPlace;
	}

	public void setCtPlace(String ctPlace) {
		this.ctPlace = ctPlace;
	}

	public String getCtRemark() {
		return ctRemark;
	}

	public void setCtRemark(String ctRemark) {
		this.ctRemark = ctRemark;
	}

	public String getCtTeam() {
		return ctTeam;
	}

	public void setCtTeam(String ctTeam) {
		this.ctTeam = ctTeam;
	}
	public boolean isAbsent() {
		return absent;
	}
	public void setAbsent(boolean absent) {
		this.absent = absent;
	}
}
