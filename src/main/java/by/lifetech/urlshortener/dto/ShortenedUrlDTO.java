package by.lifetech.urlshortener.dto;

import lombok.Data;

import java.net.URL;

@Data
public class ShortenedUrlDTO {

    private String id;
    private URL longUrl;

}
