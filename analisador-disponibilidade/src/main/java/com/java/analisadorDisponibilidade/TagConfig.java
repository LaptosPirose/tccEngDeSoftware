package com.tcc.analisador_opc;

public class TagConfig {
	private String id;
	private int ns;
	private String address;

	public TagConfig() {
	} // Necessário para o Jackson

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getNs() {
		return ns;
	}

	public void setNs(int ns) {
		this.ns = ns;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
