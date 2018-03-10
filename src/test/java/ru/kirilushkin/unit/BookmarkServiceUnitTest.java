package ru.kirilushkin.unit;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.kirilushkin.domain.Bookmark;
import ru.kirilushkin.exception.RestValidationException;
import ru.kirilushkin.repository.BookmarkRepository;
import ru.kirilushkin.repository.BookmarkRepositoryImpl;
import ru.kirilushkin.service.BookmarkService;
import ru.kirilushkin.service.BookmarkServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class BookmarkServiceUnitTest {

    @Test
    public void getAll() {
        BookmarkRepository bookmarkRepository = Mockito.mock(BookmarkRepositoryImpl.class);
        when(bookmarkRepository.getAll(false))
                .thenReturn(List.of(
                        Bookmark.builder()
                                .id(1)
                                .title("bookmark1")
                                .url("bookmark1")
                                .tags(List.of("tag1"))
                                .build(),
                        Bookmark.builder()
                                .id(2)
                                .title("bookmark2")
                                .url("bookmark2")
                                .tags(List.of("tag2"))
                                .build()
                ));
        BookmarkService bookmarkService = new BookmarkServiceImpl(bookmarkRepository);
        assertEquals(2, bookmarkService.getAll(false).size());
        verify(bookmarkRepository).getAll(false);
        verifyNoMoreInteractions(bookmarkRepository);
    }

    @Test
    public void addBookmark() {
        BookmarkRepository bookmarkRepository = Mockito.mock(BookmarkRepositoryImpl.class);
        when(bookmarkRepository.getByUrl(anyString()))
                .thenReturn(null);
        BookmarkService bookmarkService = new BookmarkServiceImpl(bookmarkRepository);
        bookmarkService.add(Bookmark.builder()
                .title("bookmark1")
                .url("bookmark1")
                .tags(List.of("tag1"))
                .build());
        verify(bookmarkRepository).getByUrl(anyString());
        verify(bookmarkRepository).add(any());
        verifyNoMoreInteractions(bookmarkRepository);
    }

    @Test
    public void throwExceptionOnAdd() {
        BookmarkRepository bookmarkRepository = Mockito.mock(BookmarkRepositoryImpl.class);
        when(bookmarkRepository.getByUrl(anyString()))
                .thenReturn(new Bookmark());
        BookmarkService bookmarkService = new BookmarkServiceImpl(bookmarkRepository);
        assertThrows(RestValidationException.class, () -> bookmarkService.add(Bookmark.builder()
                .title("bookmark1")
                .url("bookmark1")
                .tags(List.of("tag1"))
                .build()));
        verify(bookmarkRepository).getByUrl(anyString());
        verify(bookmarkRepository, times(0)).add(any());
        verifyNoMoreInteractions(bookmarkRepository);
    }

    @Test
    public void search() {
        BookmarkRepository bookmarkRepository = Mockito.mock(BookmarkRepositoryImpl.class);
        when(bookmarkRepository.search(anyBoolean(), anyBoolean(), anyString(), anyString()))
                .thenReturn(List.of(
                        Bookmark.builder()
                                .id(1)
                                .title("bookmark1")
                                .url("bookmark1")
                                .tags(List.of("tag1"))
                                .build(),
                        Bookmark.builder()
                                .id(2)
                                .title("bookmark2")
                                .url("bookmark2")
                                .tags(List.of("tag1"))
                                .build()
                ));
        BookmarkService bookmarkService = new BookmarkServiceImpl(bookmarkRepository);
        assertEquals(2, bookmarkService.search(true, true, "", "tag1").size());
        verify(bookmarkRepository).search(anyBoolean(), anyBoolean(), anyString(), anyString());
        verifyNoMoreInteractions(bookmarkRepository);

    }

    @Test
    public void getByUrl() {
        BookmarkRepository bookmarkRepository = Mockito.mock(BookmarkRepositoryImpl.class);
        when(bookmarkRepository.getByUrl(anyString()))
                .thenReturn(Bookmark.builder()
                        .id(1)
                        .title("bookmark1Title")
                        .url("bookmark1")
                        .tags(List.of("tag1"))
                        .build());
        BookmarkService bookmarkService = new BookmarkServiceImpl(bookmarkRepository);
        assertEquals("bookmark1Title", bookmarkService.getByUrl("bookmark1").getTitle());
        verify(bookmarkRepository).getByUrl(anyString());
        verifyNoMoreInteractions(bookmarkRepository);
    }

    @Test
    public void updateReadStatus() {
        BookmarkRepository bookmarkRepository = Mockito.mock(BookmarkRepositoryImpl.class);
        doNothing().when(bookmarkRepository).updateReadStatus(anyInt(), anyBoolean());
        BookmarkService bookmarkService = new BookmarkServiceImpl(bookmarkRepository);
        bookmarkService.updateReadStatus(1, true);
        verify(bookmarkRepository).updateReadStatus(anyInt(), anyBoolean());
        verifyNoMoreInteractions(bookmarkRepository);
    }

    @Test
    public void deleteById() {
        BookmarkRepository bookmarkRepository = Mockito.mock(BookmarkRepositoryImpl.class);
        doNothing().when(bookmarkRepository).deleteById(anyInt());
        BookmarkService bookmarkService = new BookmarkServiceImpl(bookmarkRepository);
        bookmarkService.deleteById(1);
        verify(bookmarkRepository).deleteById(anyInt());
        verifyNoMoreInteractions(bookmarkRepository);
    }
}
