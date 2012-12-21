package com.xcar;

import java.util.Vector;

public class ResourceQueneInsert{

	private Vector quene = new Vector();

	private static ResourceQueneInsert instance = null;

	public ResourceQueneInsert() {
	}

	public static ResourceQueneInsert getInstance() {
		if (instance == null)
			instance = new ResourceQueneInsert();
		return instance;
	}

	public synchronized void setElement(Object obj) {
		quene.addElement(obj);
	}

	public synchronized Object getElement() {
		if (quene.size() == 0)
			return null;
		Object o = quene.firstElement();
		quene.remove(0);
		return o;
	}
	
	public synchronized Vector getQuene() {
		return quene;
	}

	public synchronized void setQuene(Vector quene) {
		this.quene = quene;
	}
}