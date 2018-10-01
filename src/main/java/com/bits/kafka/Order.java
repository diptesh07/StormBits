package com.bits.kafka;

public class Order {

	private String id;
	private String customerId;
	private String creditCardNumber;
	private String creditCardExpiry;
	private long creditCardCode;
	private double chargeAmount;

	public String getId() {
		return id;
	}

	public void setId(String string) {
		this.id = string;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCreditCardNumber() {
		return creditCardNumber;
	}

	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}

	public String getCreditCardExpiry() {
		return creditCardExpiry;
	}

	public void setCreditCardExpiry(String creditCardExpiry) {
		this.creditCardExpiry = creditCardExpiry;
	}

	public long getCreditCardCode() {
		return creditCardCode;
	}

	public void setCreditCardCode(long creditCardCode) {
		this.creditCardCode = creditCardCode;
	}

	public double getChargeAmount() {
		return chargeAmount;
	}

	public void setChargeAmount(double chargeAmount) {
		this.chargeAmount = chargeAmount;
	}

	public String toString() {
		return this.id + " " + this.customerId + " " + this.creditCardNumber + " " + this.creditCardExpiry + " "
				+ this.creditCardCode + " " + this.chargeAmount;
	}

}
