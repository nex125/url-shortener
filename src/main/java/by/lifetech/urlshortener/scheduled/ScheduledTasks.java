package by.lifetech.urlshortener.scheduled;

import by.lifetech.urlshortener.domain.ShortenedUrl;
import by.lifetech.urlshortener.repository.ShortenedUrlRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Slf4j
@Component
public class ScheduledTasks {

    private final ShortenedUrlRepository shortenedURLRepository;

    private final Long timeToLive;

    public ScheduledTasks(ShortenedUrlRepository shortenedURLRepository, @Value("${application.url.ttl}") Long timeToLive) {
        this.shortenedURLRepository = shortenedURLRepository;
        this.timeToLive = timeToLive;
    }

    @Scheduled(cron = "0 0 3 * * ?")
    @CacheEvict(value = "shortened_urls", allEntries = true)
    public void cleanupTask() {
        log.info("Link cleanup task has started");
        List<ShortenedUrl> urlsToDelete = shortenedURLRepository.getAllByCreationTimeBefore(Instant.now().minusSeconds(timeToLive));
        shortenedURLRepository.deleteAll(urlsToDelete);
        log.info("Link cleanup task has finished");
    }

    @Scheduled(cron = "0 0 */3 ? * *")
    @CacheEvict(value = "shortened_urls", allEntries = true)
    public void evictCacheTask() {
        log.info("Ceche eviction task has started");
        List<ShortenedUrl> urlsToDelete = shortenedURLRepository.getAllByCreationTimeBefore(Instant.now().minusSeconds(timeToLive));
        shortenedURLRepository.deleteAll(urlsToDelete);
        log.info("Cache eviction task has finished");
    }
}
