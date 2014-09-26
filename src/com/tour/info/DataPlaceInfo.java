package com.tour.info;
/**
 * ¾°µã
 * @author ly
 *
 */
public class DataPlaceInfo {
	int placeId;
	String placeTitle;
	int placeAreaPid;
	int placeAreaCid;
	String placeArea;
	String placeContact;
	String placePhone;
	String placeFax;
	String placeContent;
	String placePhoto;
	String placePhotoDsc;
   public DataPlaceInfo(String placeTitle,String placeArea,String placeContact,
	String placePhone,String placeFax,String placeContent,String placePhoto,String placePhotoDsc){
	   this. placeTitle=placeTitle;
	   this.placeArea=placeArea;       
	   this.placeContact=placeContact;
	   this.placePhone=placePhone;
	   this.placeFax=placeFax;
	   this.placeContent=placeContent;
	   this.placePhoto=placePhoto;
	   this.placePhotoDsc=placePhotoDsc;
   }
	public int getPlaceId() {
		return placeId;
	}

	public void setPlaceId(int placeId) {
		this.placeId = placeId;
	}

	public String getPlaceTitle() {
		return placeTitle;
	}

	public void setPlaceTitle(String placeTitle) {
		this.placeTitle = placeTitle;
	}

	public int getPlaceAreaPid() {
		return placeAreaPid;
	}

	public void setPlaceAreaPid(int placeAreaPid) {
		this.placeAreaPid = placeAreaPid;
	}

	public int getPlaceAreaCid() {
		return placeAreaCid;
	}

	public void setPlaceAreaCid(int placeAreaCid) {
		this.placeAreaCid = placeAreaCid;
	}

	public String getPlaceArea() {
		return placeArea;
	}

	public void setPlaceArea(String placeArea) {
		this.placeArea = placeArea;
	}

	public String getPlaceContact() {
		return placeContact;
	}

	public void setPlaceContact(String placeContact) {
		this.placeContact = placeContact;
	}

	public String getPlacePhone() {
		return placePhone;
	}

	public void setPlacePhone(String placePhone) {
		this.placePhone = placePhone;
	}

	public String getPlaceFax() {
		return placeFax;
	}

	public void setPlaceFax(String placeFax) {
		this.placeFax = placeFax;
	}

	public String getPlaceContent() {
		return placeContent;
	}

	public void setPlaceContent(String placeContent) {
		this.placeContent = placeContent;
	}

	public String getPlacePhoto() {
		return placePhoto;
	}

	public void setPlacePhoto(String placePhoto) {
		this.placePhoto = placePhoto;
	}

	public String getPlacePhotoDsc() {
		return placePhotoDsc;
	}

	public void setPlacePhotoDsc(String placePhotoDsc) {
		this.placePhotoDsc = placePhotoDsc;
	}

}
