package com.utils;

import java.util.ArrayList;
import java.util.List;

public class PageUtil {
	private int currentPage = 0;// 当前页

	private int rowCount = 0;// 总行数

	private int pageSize = 0;// 页大小

	private int pageCount = 0;// 总页数

	private int beginPosition = 0;// 页起始

	private int endPosition = 0;// 页终止

	private boolean hasNextPage = false;// 是否有下一页

	private boolean hasPreviousPage = false;// 是否上一页

	private List arrayList = new ArrayList();// 记录集

	/**
	 * 初始化变量
	 * 
	 * @param rowCount
	 * @param pageSize
	 * @param currentPage
	 */
	public PageUtil(List arrayList, int pageSize, String currentPage) {
		this.arrayList = arrayList;
		this.rowCount = arrayList.size();
		this.pageSize = pageSize;
		this.getPageCount();
		if (currentPage == null || currentPage.equals("")) {
			this.currentPage = 1;
		} else {
			try {
				this.currentPage = Integer.parseInt(currentPage);
			} catch (NumberFormatException nfe) {
				this.currentPage = 1;
			}
		}
		this.getCurrentPage();
		this.getBeginPosition();
		this.getEndPosition();
	}

	/**
	 * 获取总行数
	 * 
	 * @return rowCount
	 */
	public int getRowCount() {
		return rowCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getCurrentPage() {
		if (currentPage >= pageCount) {
			currentPage = pageCount;
		} else if (currentPage <= 1) {
			currentPage = 1;
		}
		return currentPage;
	}

	public int getPageCount() {
		// 计算出总页数
		pageCount = (rowCount + pageSize - 1) / pageSize;
		return pageCount;
	}

	public int getBeginPosition() {
		// 计算出页起始
		beginPosition = (currentPage - 1) * pageSize + 1;
		return beginPosition;
	}

	public int getEndPosition() {
		// 计算出页终止
		if (currentPage >= pageCount) {
			endPosition = rowCount;
		} else {
			endPosition = currentPage * pageSize;
		}
		return endPosition;
	}

	public boolean isHasNextPage() {
		// 计算出是否有下一页
		if (currentPage >= pageCount) {
			hasNextPage = false;
		} else {
			hasNextPage = true;
		}
		return hasNextPage;
	}

	public boolean isHasPreviousPage() {
		// 计算出是否有上一页
		if (currentPage <= 1) {
			hasPreviousPage = false;
		} else {
			hasPreviousPage = true;
		}
		return hasPreviousPage;
	}

	public boolean isLastPage() {
		// 计算出是否有上一页
		if (currentPage == pageCount) {
			return true;
		} else {
			return false;
		}
	}
	
	public int getFirstPage() {
		return 1;
	}

	public int getPreviousPage() {
		if (this.isHasPreviousPage()) {
			return currentPage - 1;
		}
		return currentPage;
	}

	public int getNextPage() {
		if (this.isHasNextPage()) {
			return currentPage + 1;
		} else {
			return currentPage;
		}
	}

	public int getLastPage() {
		return pageCount;
	}

	/**
	 * 获取页数据
	 * 
	 * @return ArrayList
	 */
	public ArrayList getArrayList() {
		ArrayList list = new ArrayList();
		for (int i = beginPosition; i <= endPosition; i++) {
			list.add(arrayList.get(i - 1));
		}
		return list;
	}
}
