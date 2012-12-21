package org.bluestome.pcs.bean;

public class LinkBean {
	private String link;
	private String name;
	private String title;
	private String attri;
	
	public LinkBean() {
	}
	
	public LinkBean(String link){
		this.link = link;
	}
	
	public LinkBean(String link,String name){
		this.name = name;
		this.link = link;
	}
	
	public LinkBean(String link,String name,String title){
		this.link = link;
		this.name = name;
		this.title = title;
	}
	
	public LinkBean(String link,String name,String title,String attri){
		this.link = link;
		this.name = name;
		this.title = title;
		this.attri = attri;
		
	}
	public void setLink(String link) {
		this.link = link;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLink() {
		return link;
	}

	public String getName() {
		return name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAttri() {
		return attri;
	}

	public void setAttri(String attri) {
		this.attri = attri;
	}
	
	
	
	
}
