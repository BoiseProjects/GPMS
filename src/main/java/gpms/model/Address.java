package gpms.model;

import java.io.Serializable;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;

@Embedded
public class Address implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Property("street")
	private String street = new String();

	@Property("apt")
	private String apt = new String();

	@Property("city")
	private String city = new String();

	@Property("state")
	private String state = new String();

	@Property("zipcode")
	private String zipcode = new String();

	@Property("country")
	private String country = new String();

	public Address() {

	}

	public Address(String setStreet, String setApt, String setCity,
			String setState, String setZipcode, String setCountry) {
		this.street = setStreet;
		this.apt = setApt;
		this.city = setCity;
		this.state = setState;
		this.zipcode = setZipcode;
		this.country = setCountry;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getApt() {
		return apt;
	}

	public void setApt(String apt) {
		this.apt = apt;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public String toString() {
		String output = "";
		output += "street  : " + street + "\n";
		output += "apartment #  : " + apt + "\n";
		output += "city    : " + city + "\n";
		output += "state   : " + state + "\n";
		output += "zipcode : " + zipcode + "\n";
		output += "country : " + country + "\n";
		return output;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((apt == null) ? 0 : apt.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((street == null) ? 0 : street.hashCode());
		result = prime * result + ((zipcode == null) ? 0 : zipcode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Address other = (Address) obj;
		if (apt == null) {
			if (other.apt != null)
				return false;
		} else if (!apt.equals(other.apt))
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (street == null) {
			if (other.street != null)
				return false;
		} else if (!street.equals(other.street))
			return false;
		if (zipcode == null) {
			if (other.zipcode != null)
				return false;
		} else if (!zipcode.equals(other.zipcode))
			return false;
		return true;
	}

}
