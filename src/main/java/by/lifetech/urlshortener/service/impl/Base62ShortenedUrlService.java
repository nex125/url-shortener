package by.lifetech.urlshortener.service.impl;

import by.lifetech.urlshortener.domain.ShortenedUrl;
import by.lifetech.urlshortener.repository.ShortenedUrlRepository;
import by.lifetech.urlshortener.service.ShortenedUrlService;
import by.lifetech.urlshortener.exception.ShortenedUrlNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class Base62ShortenedUrlService implements ShortenedUrlService {

    private final ShortenedUrlRepository shortenedURLRepository;
    private final AtomicInteger counter = new AtomicInteger();

    public Base62ShortenedUrlService(ShortenedUrlRepository shortenedURLRepository) {
        this.shortenedURLRepository = shortenedURLRepository;
    }


    @Override
    @Cacheable(value = "shortened_urls", key = "#urlId")
    public URL resolveUrl(String urlId) {
        log.info("Getting URL with ID {}", urlId);
        return shortenedURLRepository.findById(urlId)
                .orElseThrow(() -> new ShortenedUrlNotFoundException("Shortened URL not found"))
                .getLongUrl();
    }

    @Override
    public ShortenedUrl shortenUrl(ShortenedUrl urlToShorten) {
        ShortenedUrl shortenedURL = new ShortenedUrl();
        shortenedURL.setId(generateId());
        shortenedURL.setLongUrl(urlToShorten.getLongUrl());
        shortenedURL.setCreationTime(Instant.now());
        return shortenedURLRepository.save(shortenedURL);
    }

    private String generateId() {
        final int counterValue = counter.getAndUpdate((operand) -> (operand + 1) % 1000);
        final long base10Id = Long.parseLong("" + counterValue + System.currentTimeMillis());
        return Base62Converter.fromBase10(base10Id);
    }

    private static class Base62Converter {

        public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        public static final int BASE = ALPHABET.length();

        public static String fromBase10(long i) {
            StringBuilder sb = new StringBuilder();
            while (i > 0) {
                i = fromBase10(i, sb);
            }
            return sb.reverse().toString();
        }

        private static long fromBase10(long i, final StringBuilder sb) {
            int rem = (int) (i % BASE);
            sb.append(ALPHABET.charAt(rem));
            return i / BASE;
        }

        public static long toBase10(String str) {
            return toBase10(new StringBuilder(str).reverse().toString().toCharArray());
        }

        private static long toBase10(char[] chars) {
            long n = 0;
            for (int i = chars.length - 1; i >= 0; i--) {
                n += toBase10(ALPHABET.indexOf(chars[i]), i);
            }
            return n;
        }

        private static long toBase10(long n, long pow) {
            return n * (long) Math.pow(BASE, pow);
        }

    }
}
