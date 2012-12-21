package com.message;

import java.util.Vector;

public class RequestRecordQuene {

	Vector quene = new Vector();

	private static RequestRecordQuene instance = null;

	public RequestRecordQuene() {
	}

	public static RequestRecordQuene getInstance() {
		if (instance == null)
			instance = new RequestRecordQuene();
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
