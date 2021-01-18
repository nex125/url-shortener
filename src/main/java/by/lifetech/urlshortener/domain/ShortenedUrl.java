package by.lifetech.urlshortener.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.net.URL;
import java.time.Instant;

@Data
@Entity
@Table(name = "shortened_url")
public class ShortenedUrl {

    @Id
    private String id;

    @Column(name = "long_url", nullable = false)
    private URL longUrl;

    @Column(name = "creation_time", nullable = false)
    private Instant creationTime;

}
