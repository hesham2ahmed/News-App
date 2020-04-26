package com.hesham.newsapp;

import java.net.URL;

public class News {
    private String author_name;
    private String type;
    private String publication_date;
    private String title;
    private String url;
    private String section_name;

    public News(String section_name, String type, String author_name ,String publication_date, String title, String url ) {
        this.type = type;
        this.author_name = author_name;
        this.publication_date = publication_date;
        this.title = title;
        this.url = url;
        this.section_name = section_name;
    }

    public String getType() {
        return type;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public String getSection_name() {
        return section_name;
    }

    public String getPublication_date() {
        return publication_date;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

}
