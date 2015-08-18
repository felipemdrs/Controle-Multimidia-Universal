package com.embedded.controlemultimidiauniversal.net.http;

/**
 * Enum de m&eacute;todos para o limite m&aacute;ximo de tempo para uma
 * conex&atilde;o HTTP.
 * 
 */
public enum TimeOut {
	SMALL(100), MIDDLE(150), BIG(200), VERY_BIG(250);

	private final int time;

	TimeOut(int time) {
		this.time = time;
	}

	public int getValue() {
		return time;
	}
}
