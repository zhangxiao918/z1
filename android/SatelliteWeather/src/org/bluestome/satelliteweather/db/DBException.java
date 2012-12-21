package org.bluestome.satelliteweather.db;

public class DBException extends RuntimeException {
	private static final long serialVersionUID = 3583566093089790852L;

	public DBException() {
		super();
	}

	public DBException(String message) {
		super(message);
	}

	public DBException(Throwable cause) {
		super(cause);
	}

	public DBException(String message, Throwable cause) {
		super(message, cause);
	}

}
