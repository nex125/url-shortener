package by.lifetech.urlshortener.resource;

import by.lifetech.urlshortener.domain.ShortenedUrl;
import by.lifetech.urlshortener.dto.ShortenedUrlDTO;
import by.lifetech.urlshortener.service.ShortenedUrlService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/url-shortener")
public class ShortenedUrlResource {

    private final ShortenedUrlService shortenedUrlService;
    private final ModelMapper modelMapper;

    public ShortenedUrlResource(ShortenedUrlService shortenedUrlService, ModelMapper modelMapper) {
        this.shortenedUrlService = shortenedUrlService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{urlId}")
    public RedirectView resolveUrl(@PathVariable String urlId) {
        return new RedirectView(shortenedUrlService.resolveUrl(urlId).toString());
    }

    @PostMapping("/shorten")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ShortenedUrlDTO shortenUrl(@RequestBody ShortenedUrlDTO shortenedUrlDTO) {
        ShortenedUrl shortenedURL = convertToEntity(shortenedUrlDTO);
        ShortenedUrl shortenedUrlCreated = shortenedUrlService.shortenUrl(shortenedURL);
        return convertToDto(shortenedUrlCreated);
    }

    private ShortenedUrlDTO convertToDto(ShortenedUrl shortenedURL) {
        return modelMapper.map(shortenedURL, ShortenedUrlDTO.class);
    }

    private ShortenedUrl convertToEntity(ShortenedUrlDTO shortenedUrlDTO) {
        return modelMapper.map(shortenedUrlDTO, ShortenedUrl.class);
    }

}
