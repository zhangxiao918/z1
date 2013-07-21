
package com.bluestome.imageloader.domain;

import java.io.Serializable;

public class ImageBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String detailLink;
    private String title;
    private String imageUrl;
    private String imageDesc;
    private String uploadTime;
    private String screensize;

    public ImageBean() {

    }

    public String getDetailLink() {
        return detailLink;
    }

    public void setDetailLink(String detailLink) {
        this.detailLink = detailLink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getScreensize() {
        return screensize;
    }

    public void setScreensize(String screensize) {
        this.screensize = screensize;
    }

    public String getImageDesc() {
        return imageDesc;
    }

    public void setImageDesc(String imageDesc) {
        this.imageDesc = imageDesc;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("detailLink:").append(detailLink).append("\r\n");
        sb.append("title:").append(title).append("\r\n");
        sb.append("imageUrl:").append(imageUrl).append("\r\n");
        sb.append("uploadTime:").append(uploadTime).append("\r\n");
        sb.append("screensize:").append(screensize).append("\r\n");
        sb.append("imageDesc:").append(imageDesc).append("\r\n");
        return sb.toString();
    }

}
