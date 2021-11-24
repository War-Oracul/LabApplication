package com.example.labapplication;

public class Item {

    private String mName;
    private String mCompany;
    private String mPost;
    private int mId;

     public Item(String name, String company, String post, int id){
         mName = name;
         mCompany = company;
         mPost = post;
         mId = id;
     }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getCompany() {
        return mCompany;
    }

    public void setCompany(String company) {
        mCompany = company;
    }

    public String getPost() {
        return mPost;
    }

    public void setPost(String post) {
        mPost = post;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }
}
