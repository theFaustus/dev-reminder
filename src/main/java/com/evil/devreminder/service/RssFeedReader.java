package com.evil.devreminder.service;

import com.evil.devreminder.domain.NewsArticle;

import java.util.List;

public interface RssFeedReader {
    List<NewsArticle> getArticles();
}
