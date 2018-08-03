package com.bellini.recipecatalog.model.v1.mapper.book;

import com.bellini.recipecatalog.model.v1.Book;
import com.bellini.recipecatalog.model.v1.dto.book.BookModificationDTO;
import com.bellini.recipecatalog.model.v1.mapper.Mapper;

public class BookModificationMapper implements Mapper<Book, BookModificationDTO> {

    private static BookModificationMapper instance = new BookModificationMapper();

    private BookModificationMapper() {
    }

    public static BookModificationMapper getInstance() {
        return instance;
    }

    @Override
    public BookModificationDTO toDto(Book param) {
        // not implemented
        return null;
    }

    @Override
    public Book fromDto(BookModificationDTO param) {
        Book obj = new Book();

        obj.setTitle(param.getTitle());

        return obj;
    }

}
