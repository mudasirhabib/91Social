package com.social91.dto;

public class CycleComponents {

	private Frame frame;
	private HandleBarBrakes handleBarBrakes;
	private Seating seating;  
	private Wheels wheels; 
	private ChainAssembly chainAssembly;
	private String dateOfPricing;
	private String cycleId;
	
	public Frame getFrame() {
		return frame;
	}
	public void setFrame(Frame frame) {
		this.frame = frame;
	}
	public HandleBarBrakes getHandleBarBrakes() {
		return handleBarBrakes;
	}
	public void setHandleBarBrakes(HandleBarBrakes handleBarBrakes) {
		this.handleBarBrakes = handleBarBrakes;
	}
	public Seating getSeating() {
		return seating;
	}
	public void setSeating(Seating seating) {
		this.seating = seating;
	}
	public Wheels getWheels() {
		return wheels;
	}
	public void setWheels(Wheels wheels) {
		this.wheels = wheels;
	}
	public ChainAssembly getChainAssembly() {
		return chainAssembly;
	}
	public void setChainAssembly(ChainAssembly chainAssembly) {
		this.chainAssembly = chainAssembly;
	}
	public String getDateOfPricing() {
		return dateOfPricing;
	}
	public void setDateOfPricing(String dateOfPricing) {
		this.dateOfPricing = dateOfPricing;
	}
	public String getCycleId() {
		return cycleId;
	}
	public void setCycleId(String cycleId) {
		this.cycleId = cycleId;
	}
}
