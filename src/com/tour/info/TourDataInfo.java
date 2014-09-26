package com.tour.info;


/**
 * мепео╒
 * 
 * @author ly
 * 
 */
public class TourDataInfo {
	int tourId;
	String tourTitle;
	String tourDay;
	String tourNo;
	int tourLineid;
	String tourLineContent;
	String tourType;
	String tourPtype;
	String tourClass;
	String tourPlace;
	String tourFlat;
	String tourLeader;
	String tourLeaderPhone;
	String tourDriver;
	String tourDriverPhone;
	String tourGuideid;
	String tourGuide;
	String tourGuidePhone;
	String tourBookingSeat;
	String tourCtCountList;
	int tourCtCount;
	String tourCtMax;
	String tourDate;
	String tour_zip;
	String tour_update_time;
	String tour_remark;
	String tour_clock;
   public TourDataInfo(String tourTitle,String tourNo,String tourLineContent,String tourFlat,String tourGuide,String tourGuidePhone,String tourDriver,String tourDriverPhone,
		   String tourType,String tourPlace,String tourDate,String tourCtCountList,String tour_zip,String tour_update_time,String tour_remark,String tour_clock){
    	this.tourTitle=tourTitle;
    	this.tourNo=tourNo;
    	this.tourLineContent=tourLineContent;
    	this.tourFlat=tourFlat;
    	this. tourGuide=tourGuide;
    	this.tourGuidePhone=tourGuidePhone;
    	this. tourDriver=tourDriver;
    	this.tourDriverPhone=tourDriverPhone;
    	this. tourType=tourType;
        this.tourPlace=tourPlace;
    	this. tourDate=tourDate;
    	this.tourCtCountList=tourCtCountList;
    	this. tour_zip=tour_zip;
    	this. tour_update_time=tour_update_time;
    	this.tour_remark=tour_remark;
    	this.tour_clock=tour_clock;
    	
    }
	public int getTourId() {
		return tourId;
	}

	public void setTourId(int tourId) {
		this.tourId = tourId;
	}

	public String getTourTitle() {
		return tourTitle;
	}

	public void setTourTitle(String tourTitle) {
		this.tourTitle = tourTitle;
	}

	public String getTourDay() {
		return tourDay;
	}

	public void setTourDay(String tourDay) {
		this.tourDay = tourDay;
	}

	public String getTourNo() {
		return tourNo;
	}

	public void setTourNo(String tourNo) {
		this.tourNo = tourNo;
	}

	public int getTourLineid() {
		return tourLineid;
	}

	public void setTourLineid(int tourLineid) {
		this.tourLineid = tourLineid;
	}

	public String getTourLineContent() {
		return tourLineContent;
	}

	public void setTourLineContent(String tourLineContent) {
		this.tourLineContent = tourLineContent;
	}

	public String getTourType() {
		return tourType;
	}

	public void setTourType(String tourType) {
		this.tourType = tourType;
	}

	public String getTourPtype() {
		return tourPtype;
	}

	public void setTourPtype(String tourPtype) {
		this.tourPtype = tourPtype;
	}

	public String getTourClass() {
		return tourClass;
	}

	public void setTourClass(String tourClass) {
		this.tourClass = tourClass;
	}

	public String getTourPlace() {
		return tourPlace;
	}

	public void setTourPlace(String tourPlace) {
		this.tourPlace = tourPlace;
	}

	public String getTourFlat() {
		return tourFlat;
	}

	public void setTourFlat(String tourFlat) {
		this.tourFlat = tourFlat;
	}

	public String getTourLeader() {
		return tourLeader;
	}

	public void setTourLeader(String tourLeader) {
		this.tourLeader = tourLeader;
	}

	public String getTourLeaderPhone() {
		return tourLeaderPhone;
	}

	public void setTourLeaderPhone(String tourLeaderPhone) {
		this.tourLeaderPhone = tourLeaderPhone;
	}
	public String getTourDriver() {
		return tourDriver;
	}
	
	public void setTourDriver(String tourDriver) {
		this.tourDriver = tourDriver;
	}
	
	public String getTourDriverPhone() {
		return tourDriverPhone;
	}
	
	public void setTourDriverPhone(String tourDriverPhone) {
		this.tourDriverPhone = tourDriverPhone;
	}

	public String getTourGuideid() {
		return tourGuideid;
	}

	public void setTourGuideid(String tourGuideid) {
		this.tourGuideid = tourGuideid;
	}

	public String getTourGuide() {
		return tourGuide;
	}

	public void setTourGuide(String tourGuide) {
		this.tourGuide = tourGuide;
	}

	public String getTourGuidePhone() {
		return tourGuidePhone;
	}

	public void setTourGuidePhone(String tourGuidePhone) {
		this.tourGuidePhone = tourGuidePhone;
	}

	public String getTourBookingSeat() {
		return tourBookingSeat;
	}

	public void setTourBookingSeat(String tourBookingSeat) {
		this.tourBookingSeat = tourBookingSeat;
	}

	public String getTourCtCountList() {
		return tourCtCountList;
	}

	public void setTourCtCountList(String tourCtCountList) {
		this.tourCtCountList = tourCtCountList;
	}

	public int getTourCtCount() {
		return tourCtCount;
	}

	public void setTourCtCount(int tourCtCount) {
		this.tourCtCount = tourCtCount;
	}

	public String getTourCtMax() {
		return tourCtMax;
	}

	public void setTourCtMax(String tourCtMax) {
		this.tourCtMax = tourCtMax;
	}

	public String getTourDate() {
		return tourDate;
	}

	public void setTourDate(String tourDate) {
		this.tourDate = tourDate;
	}
	public String getTourZip() {
		return tour_zip;
	}
	
	public void setTourrZip(String tour_zip) {
		this.tour_zip = tour_zip;
	}
	public String getTouUpdataTime() {
		return tour_update_time;
	}
	
	public void setTouUpdataTime(String tour_update_time) {
		this.tour_update_time = tour_update_time;
	}
	public String getRemark() {
		return tour_remark;
	}
	
	public void setRemark(String tour_remark) {
		this.tour_remark = tour_remark;
	}
	public String getClock() {
		return tour_clock;
	}
	
	public void setClock(String tour_clock) {
		this.tour_clock = tour_clock;
	}

}
