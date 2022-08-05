package com.example.microservice.componentProcessor.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "returnorder")
public class ComponentOrderProcessorEntity {
	@Id
	private String id;
	private String custName;
	private String custContactDetails;
	private String compType;
	private String compName;
	private String details;
	private int qty;
	private double processCharge;
	private double packAndDelCharge;
	private String delDate;
	private String createdAt;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getCustContactDetails() {
		return custContactDetails;
	}

	public void setCustContactDetails(String custContactDetails) {
		this.custContactDetails = custContactDetails;
	}

	public String getCompType() {
		return compType;
	}

	public void setCompType(String compType) {
		this.compType = compType;
	}

	public String getCompName() {
		return compName;
	}

	public void setCompName(String compName) {
		this.compName = compName;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public double getProcessCharge() {
		return processCharge;
	}

	public void setProcessCharge(double processCharge) {
		this.processCharge = processCharge;
	}

	public double getPackAndDelCharge() {
		return packAndDelCharge;
	}

	public void setPackAndDelCharge(double packAndDelCharge) {
		this.packAndDelCharge = packAndDelCharge;
	}

	public String getDelDate() {
		return delDate;
	}

	public void setDelDate(String delDate) {
		this.delDate = delDate;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

}
