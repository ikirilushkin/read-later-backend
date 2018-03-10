package ru.kirilushkin.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.kirilushkin.domain.Bookmark;
import ru.kirilushkin.rest.BookmarkRestController;
import ru.kirilushkin.service.BookmarkService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookmarkRestController.class)
public class BookmarkRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookmarkService bookmarkService;

    @Test
    public void shouldReturnBookmarkList() throws Exception {
        when(bookmarkService.getAll(anyBoolean())).thenReturn(
                List.of(Bookmark.builder()
                        .id(1)
                        .title("bookmark1")
                        .url("bookmark1")
                        .tags(List.of("tag1"))
                        .build())
        );
        mockMvc.perform(get("/api/bookmarks?read=true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.[0].title").value("bookmark1"));
    }

    @Test
    public void shouldAddBookmark() throws Exception {
        doNothing().when(bookmarkService).add(any(Bookmark.class));
        mockMvc.perform(post("/api/bookmarks")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"title\": \"bookmark1\", \"url\": \"bookmark1\", \"tags\": [\"tag1\", \"tag2\"]}"))
                .andExpect(status().isOk())
        ;
        verify(bookmarkService).add(any(Bookmark.class));
    }

    @Test
    public void shouldSetReadStatus() throws Exception {
        doNothing().when(bookmarkService).updateReadStatus(anyInt(), anyBoolean());
        mockMvc.perform(put("/api/bookmarks/1/read"))
                .andExpect(status().isOk());
        verify(bookmarkService).updateReadStatus(anyInt(), anyBoolean());
    }

    @Test
    public void shouldSetUnreadStatus() throws Exception {
        doNothing().when(bookmarkService).updateReadStatus(anyInt(), anyBoolean());
        mockMvc.perform(delete("/api/bookmarks/1/read"))
                .andExpect(status().isOk());
        verify(bookmarkService).updateReadStatus(anyInt(), anyBoolean());
    }

    @Test
    public void shouldSearchBookmark() throws Exception {
        when(bookmarkService.search(anyBoolean(), anyBoolean(), anyString(), anyString()))
                .thenReturn(
                        List.of(Bookmark.builder()
                                .id(1)
                                .title("bookmark1")
                                .url("bookmark1")
                                .tags(List.of("tag1"))
                                .read(false)
                                .build())
                );
        mockMvc.perform(get("/api/bookmarks/search?unread=true&read=true&tag=\"\"&link=\"\""))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.[0].title").value("bookmark1"));
    }

    @Test
    public void shouldDeleteById() throws Exception {
        doNothing().when(bookmarkService).deleteById(anyInt());
        mockMvc.perform(delete("/api/bookmarks/1"))
                .andExpect(status().isOk());
        verify(bookmarkService).deleteById(anyInt());
    }

}
