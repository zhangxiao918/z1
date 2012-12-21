package com.chinamilitary.bean;

/*******************************************************************************
 * File name: PicsModel.java Create date: 2003-11-18 Copyright: Copyright (c)
 * 2003 by HEKAI
 * 
 * @Version: 1.0 Modify History Author Date Description hekai 2003-11-18 Create
 *           the PicsModel.java
 ******************************************************************************/
// 请用你的相应包名进行替换
import java.io.Serializable;

public class PicsModel implements Serializable {
	private String picsExtendName = null;

	private int picsWidth = 0;

	private int picsHeight = 0;

	private String picsColor = null;

	private String picsSize = null;

	public PicsModel() {
	}

	public String getpicsExtendName() {
		return picsExtendName;
	}

	public void setpicsExtendName(String picsExtendName) {
		this.picsExtendName = picsExtendName;
	}

	public int getpicsWidth() {
		return picsWidth;
	}

	public void setpicsWidth(int picsWidth) {
		this.picsWidth = picsWidth;
	}

	public int getpicsHeight() {
		return picsHeight;
	}

	public void setpicsHeight(int picsHeight) {
		this.picsHeight = picsHeight;
	}

	public String getpicsColor() {
		return picsColor;
	}

	public void setpicsColor(String picsColor) {
		this.picsColor = picsColor;
	}

	public String getpicsSize() {
		return picsSize;
	}

	public void setpicsSize(String picsSize) {
		this.picsSize = picsSize;
	}
}
