package ru.kirilushkin.service;

import org.springframework.stereotype.Service;
import ru.kirilushkin.domain.Bookmark;
import ru.kirilushkin.repository.BookmarkRepository;

import java.util.List;

@Service
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    public BookmarkService(BookmarkRepository bookmarkRepository) {
        this.bookmarkRepository = bookmarkRepository;
    }

    public void add(Bookmark bookmark) {
        bookmarkRepository.add(bookmark);
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
}
