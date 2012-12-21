package com.verycd;

import java.util.Vector;

public class ResourceQueneUpdate{

	private Vector quene = new Vector();

	private static ResourceQueneUpdate instance = null;

	public ResourceQueneUpdate() {
	}

	public static ResourceQueneUpdate getInstance() {
		if (instance == null)
			instance = new ResourceQueneUpdate();
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