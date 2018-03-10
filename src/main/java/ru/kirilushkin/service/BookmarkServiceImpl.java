package ru.kirilushkin.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kirilushkin.domain.Bookmark;
import ru.kirilushkin.exception.RestValidationException;
import ru.kirilushkin.repository.BookmarkRepository;

import java.util.List;

@Service
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    public BookmarkServiceImpl(BookmarkRepository bookmarkRepository) {
        this.bookmarkRepository = bookmarkRepository;
    }

    @Override
    @Transactional
    public void add(Bookmark bookmark) {
        if (getByUrl(bookmark.getUrl()) == null) {
            bookmarkRepository.add(bookmark);
        } else {
            throw new RestValidationException("validation.error.bookmark.url.unique", "url");
        }
    }

    @Override
    public void deleteById(int id) {
        bookmarkRepository.deleteById(id);
    }

    @Override
    public void updateReadStatus(int id, boolean read) {
        bookmarkRepository.updateReadStatus(id, read);
    }

    @Override
    public List<Bookmark> getAll(boolean read) {
        return bookmarkRepository.getAll(read);
    }

    @Override
    public List<Bookmark> search(Boolean unread, Boolean read, String tag, String link) {
        return bookmarkRepository.search(unread, read, tag, link);
    }

    @Override
    public Bookmark getByUrl(String url) {
        return bookmarkRepository.getByUrl(url);
    }
}
