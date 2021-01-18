package by.lifetech.urlshortener.service;

import by.lifetech.urlshortener.domain.ShortenedUrl;

import java.net.URL;

public interface ShortenedUrlService {

    URL resolveUrl(String urlId);

    ShortenedUrl shortenUrl(ShortenedUrl urlToShorten);

}
