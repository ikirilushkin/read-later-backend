package ru.kirilushkin.service;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import ru.kirilushkin.domain.Bookmark;
import ru.kirilushkin.exception.RestValidationException;
import ru.kirilushkin.repository.BookmarkRepository;

import java.util.List;

@Service
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    private final MessageSource messageSource;

    public BookmarkService(BookmarkRepository bookmarkRepository, MessageSource messageSource) {
        this.bookmarkRepository = bookmarkRepository;
        this.messageSource = messageSource;
    }

    public void add(Bookmark bookmark, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new RestValidationException(messageSource.getMessage(
                    bindingResult.getFieldError().getDefaultMessage(),
                    new Object[]{},
                    null
            ));
        } else {
            bookmarkRepository.add(bookmark);
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
