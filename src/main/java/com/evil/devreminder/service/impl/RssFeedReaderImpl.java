package com.evil.devreminder.service.impl;

import com.evil.devreminder.domain.NewsArticle;
import com.evil.devreminder.service.RssFeedReader;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RssFeedReaderImpl implements RssFeedReader {

    @Value("#{'${rss.feed.urls}'.split(',')}")
    private final List<String> urls;

    @Override
    public List<NewsArticle> getArticles() {
        List<NewsArticle> newsArticles = urls.stream().map(this::read).flatMap(Collection::stream).collect(Collectors.toList());
        Collections.shuffle(newsArticles);
        return newsArticles.stream().limit(10).collect(Collectors.toList());
    }

    private List<NewsArticle> read(String feedUrl) {
        List<NewsArticle> results = new ArrayList<>();
        try {
            URLConnection urlConnection = new URL(feedUrl).openConnection();
            urlConnection.setConnectTimeout(300);
            urlConnection.setReadTimeout(300);
            XmlReader reader = new XmlReader(urlConnection);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(reader);
            for (Object o : feed.getEntries()) {
                SyndEntry syndEntry = (SyndEntry) o;
                results.add(toArticle(syndEntry));
            }
        } catch (FeedException | IOException e) {
            return results;
        }
        return results;
    }

    private NewsArticle toArticle(SyndEntry syndEntry) {
        NewsArticle newsArticle = new NewsArticle();
        newsArticle.setTitle(syndEntry.getTitle());
        newsArticle.setLink(syndEntry.getLink());
        return newsArticle;
    }
}
