package ru.kirilushkin.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kirilushkin.domain.Bookmark;
import ru.kirilushkin.exception.RestValidationException;
import ru.kirilushkin.repository.BookmarkRepository;

import java.util.List;

@Service
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    public BookmarkService(BookmarkRepository bookmarkRepository) {
        this.bookmarkRepository = bookmarkRepository;
    }

    @Transactional
    public void add(Bookmark bookmark) {
        if (getByUrl(bookmark.getUrl()) == null) {
            bookmarkRepository.add(bookmark);
        } else {
            throw new RestValidationException("validation.error.bookmark.url.unique", "url");
        }
    }

    public void deleteById(int id) {
        bookmarkRepository.deleteById(id);
    }

    public void updateRead(int id, boolean read) {
        bookmarkRepository.updateRead(id, read);
    }

    public List<Bookmark> getAll(boolean read) {
        return bookmarkRepository.getAll(read);
    }

    public List<Bookmark> search(Boolean unread, Boolean read, String tag, String link) {
        return bookmarkRepository.search(unread, read, tag, link);
    }

    public Bookmark getByUrl(String url) {
        return bookmarkRepository.getByUrl(url);
    }
}
