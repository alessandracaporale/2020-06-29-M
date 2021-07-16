package it.polito.tdp.imdb.model;

public class Adiacenti implements Comparable<Adiacenti>{

	private Director d;
	private int n;
	public Adiacenti(Director d, int n) {
		super();
		this.d = d;
		this.n = n;
	}
	public Director getD() {
		return d;
	}
	public void setD(Director d) {
		this.d = d;
	}
	public int getN() {
		return n;
	}
	public void setN(int n) {
		this.n = n;
	}
	@Override
	public int compareTo(Adiacenti o) {
		return this.getN()-o.getN();
	}
	@Override
	public String toString() {
		return this.getD().getFirstName()+" "+this.getD().getLastName()+" - "+this.getN();
	}
	
	
}
