package com.bank.manage.entites;

public class Address {

	private int RoomNumber;
	private String AddressLine1;
	private String AddressLine2;
	private String city;
	private String country;
	private int postalCOde;

	public int getRoomNumber() {
		return RoomNumber;
	}

	public void setRoomNumber(int roomNumber) {
		RoomNumber = roomNumber;
	}

	public String getAddressLine1() {
		return AddressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		AddressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return AddressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		AddressLine2 = addressLine2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getPostalCOde() {
		return postalCOde;
	}

	public void setPostalCOde(int postalCOde) {
		this.postalCOde = postalCOde;
	}
	
	

	public Address(int roomNumber, String addressLine1, String addressLine2, String city, String country,
			int postalCOde) {
		super();
		RoomNumber = roomNumber;
		AddressLine1 = addressLine1;
		AddressLine2 = addressLine2;
		this.city = city;
		this.country = country;
		this.postalCOde = postalCOde;
	}

	@Override
	public String toString() {
		return   RoomNumber + ", " + AddressLine1 + ", " + AddressLine2
				+ ", " + city + ", " + country + ", " + postalCOde ;
	}
	
	

}
