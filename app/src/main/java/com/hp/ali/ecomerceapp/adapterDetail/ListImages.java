package com.hp.ali.ecomerceapp.adapterDetail;

public class ListImages {

    private String ImageUrl;

    public ListImages(){
    }

    public ListImages(String imageUrl){
        ImageUrl = imageUrl;
    }

    public String getImageUrl(){
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
