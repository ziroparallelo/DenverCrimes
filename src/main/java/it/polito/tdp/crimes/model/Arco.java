package it.polito.tdp.crimes.model;

public class Arco {
	
	private String crime1;
	private String crime2;
	private int peso;
	
	
	public Arco(String crime1, String crime2, int peso) {
		super();
		this.crime1 = crime1;
		this.crime2 = crime2;
		this.peso = peso;
	}
	public String getCrime1() {
		return crime1;
	}
	public void setCrime1(String crime1) {
		this.crime1 = crime1;
	}
	public String getCrime2() {
		return crime2;
	}
	public void setCrime2(String crime2) {
		this.crime2 = crime2;
	}
	public int getPeso() {
		return peso;
	}
	public void setPeso(int peso) {
		this.peso = peso;
	}
	@Override
	public String toString() {
		return "Arco "+ crime1 + " <-> " + crime2 + ", peso=" + peso;
	}
	
	
	
	

}
