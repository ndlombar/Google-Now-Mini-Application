package com.c323finalproject.nicklombardi;

public class SavedLinks {

    private String linkTitle;
    private String linkLink;

    public SavedLinks(String linkTitle, String linkLink) {
        this.linkTitle = linkTitle;
        this.linkLink = linkLink;
    }

    public String getLinkTitle() {
        return linkTitle;
    }

    public void setLinkTitle(String linkTitle) {
        this.linkTitle = linkTitle;
    }

    public String getLinkLink() {
        return linkLink;
    }

    public void setLinkLink(String linkLink) {
        this.linkLink = linkLink;
    }

    @Override
    public String toString() {
        return "Title: "+this.linkTitle+" Link: "+this.linkLink;
    }
}
