package by.lifetech.urlshortener.repository;

import by.lifetech.urlshortener.domain.ShortenedUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface ShortenedUrlRepository extends JpaRepository<ShortenedUrl, String> {

    List<ShortenedUrl> getAllByCreationTimeBefore(Instant creationTime);

}
