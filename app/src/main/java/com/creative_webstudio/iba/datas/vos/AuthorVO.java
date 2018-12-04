package com.creative_webstudio.iba.datas.vos;

import com.google.gson.annotations.SerializedName;

public class AuthorVO {

    @SerializedName("author-id")
    private double authorId = 0;

    @SerializedName("author-name")
    private String name;

    @SerializedName("author-picture")
    private String authorPic;

    public double getAuthorId() {
        return authorId;
    }

    public void setAuthorId(double authorId) {
        this.authorId = authorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthorPic() {
        return authorPic;
    }

    public void setAuthorPic(String authorPic) {
        this.authorPic = authorPic;
    }
}
