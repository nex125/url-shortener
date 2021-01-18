package by.lifetech.urlshortener.service;

import by.lifetech.urlshortener.domain.ShortenedUrl;
import by.lifetech.urlshortener.exception.ShortenedUrlNotFoundException;
import by.lifetech.urlshortener.repository.ShortenedUrlRepository;
import by.lifetech.urlshortener.service.impl.Base62ShortenedUrlService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("SpringJavaAutowiredMembersInspection")
@RunWith(SpringRunner.class)
public class Base62ShortenedUrlServiceTest {

    @TestConfiguration
    static class Base62ShortenedUrlServiceTestContextConfiguration {

        @MockBean
        private ShortenedUrlRepository shortenedURLRepository;

        @Bean
        public Base62ShortenedUrlService shortenedUrlService() {
            return new Base62ShortenedUrlService(shortenedURLRepository);
        }
    }

    @Autowired
    private ShortenedUrlService shortenedUrlService;
    @Autowired
    private ShortenedUrlRepository shortenedURLRepository;

    private final Instant testDate = Instant.now();

    @Before
    public void setUp() throws MalformedURLException {
        ShortenedUrl resolvableUrl = new ShortenedUrl();
        resolvableUrl.setId("CwBMFp6");
        resolvableUrl.setLongUrl(new URL("http://test.com"));
        resolvableUrl.setCreationTime(testDate);

        Mockito.when(shortenedURLRepository.findById(resolvableUrl.getId()))
                .thenReturn(java.util.Optional.of(resolvableUrl));

        Mockito.when(shortenedURLRepository.save(Mockito.any(ShortenedUrl.class)))
                .thenReturn(resolvableUrl);
    }

    @Test
    public void whenResolveUrl_thenUrlShouldBeFound() throws MalformedURLException {
        String urlId = "CwBMFp6";
        URL found = shortenedUrlService.resolveUrl(urlId);
        assertThat(found, is(new URL("http://test.com")));
    }

    @Test
    public void whenCantResolveUrl_thenUrlShouldBeException() {
        assertThrows(ShortenedUrlNotFoundException.class, () -> shortenedUrlService.resolveUrl("invalid"));
    }

    @Test
    public void whenShortenUrl_thenShortenedUrlShouldBeReturned() throws MalformedURLException {
        ShortenedUrl urlToSave = new ShortenedUrl();
        urlToSave.setLongUrl(new URL("http://test.com"));

        ShortenedUrl createdShortenedUrl = shortenedUrlService.shortenUrl(urlToSave);

        assertThat(createdShortenedUrl.getId(), is("CwBMFp6"));
    }
}
