package ru.kirilushkin.validator;

import org.springframework.beans.factory.annotation.Autowired;
import ru.kirilushkin.service.BookmarkService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueBookmarkUrlValidator implements ConstraintValidator<UniqueBookmarkUrl, String> {

    private final BookmarkService bookmarkService;

    @Autowired
    public UniqueBookmarkUrlValidator(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @Override
    public void initialize(UniqueBookmarkUrl uniqueBookmarkUrl) {

    }

    @Override
    public boolean isValid(String url, ConstraintValidatorContext constraintValidatorContext) {
        return bookmarkService.getByUrl(url) == null;
    }
}
