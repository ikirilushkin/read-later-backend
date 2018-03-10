package ru.kirilushkin.e2e;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ru.kirilushkin.domain.Bookmark;

import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class BookmarkRestControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void getAll() {
        List<Bookmark> bookmarks = restTemplate.exchange(
                "/api/bookmarks?read=true",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Bookmark>>() {
                }).getBody();
        Assertions.assertEquals(1, bookmarks.size());
    }

    @Test
    @DirtiesContext
    @Transactional
    public void addBookmark() {
        List<Bookmark> bookmarks = restTemplate.exchange(
                "/api/bookmarks?read=false",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Bookmark>>() {
                }).getBody();
        Assertions.assertEquals(1, bookmarks.size());

        Bookmark bookmark = Bookmark.builder()
                .title("bookmark1")
                .url("bookmark1")
                .tags(List.of("tag1", "tag2"))
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Bookmark> entity = new HttpEntity<>(bookmark, headers);
        ResponseEntity<Object> response = restTemplate.exchange("/api/bookmarks", HttpMethod.POST, entity, Object.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        bookmarks = restTemplate.exchange(
                "/api/bookmarks?read=false",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Bookmark>>() {
                }).getBody();
        Assertions.assertEquals(2, bookmarks.size());
    }

    @Test
    public void shouldErrorOnTitleSize() {
        Bookmark bookmark = Bookmark.builder()
                .url("bookmark1")
                .tags(List.of("tag1", "tag2"))
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Bookmark> entity = new HttpEntity<>(bookmark, headers);
        ResponseEntity<Object> responseEntity = restTemplate.exchange("/api/bookmarks", HttpMethod.POST, entity, Object.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        LinkedHashMap<String, String> response = (LinkedHashMap<String, String>) responseEntity.getBody();
        assertEquals("title", response.get("field"));
        assertEquals("validation.error.bookmark.title.size", response.get("code"));
    }

    @Test
    public void shouldErrorOnUrlSize() {
        Bookmark bookmark = Bookmark.builder()
                .title("bookmark1")
                .tags(List.of("tag1", "tag2"))
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Bookmark> entity = new HttpEntity<>(bookmark, headers);
        ResponseEntity<Object> responseEntity = restTemplate.exchange("/api/bookmarks", HttpMethod.POST, entity, Object.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        LinkedHashMap<String, String> response = (LinkedHashMap<String, String>) responseEntity.getBody();
        assertEquals("url", response.get("field"));
        assertEquals("validation.error.bookmark.url.size", response.get("code"));
    }

//    @Test
//    @DirtiesContext
//    @Sql("classpath:sql/insert-bookmark.sql")
//    public void shouldErrorOnUrlUnique() {
//        Bookmark bookmark = Bookmark.builder()
//                .title("bookmark1")
//                .url("bookmark1")
//                .tags(List.of("tag1", "tag2"))
//                .build();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity<Bookmark> entity = new HttpEntity<>(bookmark, headers);
//        ResponseEntity<Object> responseEntity = restTemplate.exchange("/api/bookmarks", HttpMethod.POST, entity, Object.class);
//        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
//        LinkedHashMap<String, String> response = (LinkedHashMap<String, String>) responseEntity.getBody();
//        assertEquals("url", response.get("field"));
//        assertEquals("validation.error.bookmark.url.unique", response.get("code"));
//    }

    @Test
    public void search() {
        List<Bookmark> bookmarks = restTemplate.exchange(
                "/api/bookmarks/search?tag=search",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Bookmark>>() {
                }).getBody();
        Assertions.assertEquals(1, bookmarks.size());
    }

    @Test
    @DirtiesContext
    @Transactional
    public void setReadStatus() {
        List<Bookmark> bookmarks = restTemplate.exchange(
                "/api/bookmarks?read=false",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Bookmark>>() {
                }).getBody();
        Assertions.assertEquals(1, bookmarks.size());
        Bookmark bookmark = bookmarks.get(0);
        ResponseEntity<Object> responseEntity = restTemplate.exchange("/api/bookmarks/" + bookmark.getId() + "/read", HttpMethod.PUT, null, Object.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        bookmarks = restTemplate.exchange(
                "/api/bookmarks?read=false",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Bookmark>>() {
                }).getBody();
        Assertions.assertEquals(0, bookmarks.size());
    }

    @Test
    @DirtiesContext
    @Transactional
    public void setUnreadStatus() {
        List<Bookmark> bookmarks = restTemplate.exchange(
                "/api/bookmarks?read=true",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Bookmark>>() {
                }).getBody();
        Assertions.assertEquals(1, bookmarks.size());
        Bookmark bookmark = bookmarks.get(0);
        ResponseEntity<Object> responseEntity = restTemplate.exchange("/api/bookmarks/" + bookmark.getId() + "/read", HttpMethod.DELETE, null, Object.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        bookmarks = restTemplate.exchange(
                "/api/bookmarks?read=true",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Bookmark>>() {
                }).getBody();
        Assertions.assertEquals(0, bookmarks.size());
    }

    @Test
    @DirtiesContext
    @Transactional
    public void deleteBookmark() {
        List<Bookmark> bookmarks = restTemplate.exchange(
                "/api/bookmarks?read=true",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Bookmark>>() {
                }).getBody();
        Assertions.assertEquals(1, bookmarks.size());
        Bookmark bookmark = bookmarks.get(0);
        ResponseEntity<Object> responseEntity = restTemplate.exchange("/api/bookmarks/" + bookmark.getId(), HttpMethod.DELETE, null, Object.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        bookmarks = restTemplate.exchange(
                "/api/bookmarks?read=true",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Bookmark>>() {
                }).getBody();
        Assertions.assertEquals(0, bookmarks.size());
    }
}
