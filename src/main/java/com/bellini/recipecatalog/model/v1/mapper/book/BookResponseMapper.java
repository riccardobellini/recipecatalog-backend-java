package com.bellini.recipecatalog.model.v1.mapper.book;

import java.time.ZoneOffset;

import com.bellini.recipecatalog.model.v1.Book;
import com.bellini.recipecatalog.model.v1.dto.book.BookDTO;
import com.bellini.recipecatalog.model.v1.mapper.Mapper;

public class BookResponseMapper implements Mapper<Book, BookDTO> {

    private static BookResponseMapper instance = new BookResponseMapper();

    private BookResponseMapper() {
    }

    public static BookResponseMapper getInstance() {
        return instance;
    }

    @Override
    public BookDTO toDto(Book param) {
        BookDTO dto = new BookDTO();
        
        dto.setId(param.getId());
        dto.setTitle(param.getTitle());
        if (param.getCreationTime() != null) {
            dto.setCreationTime(param.getCreationTime().atZone(ZoneOffset.UTC));
        }
        if (param.getLastModificationTime() != null) {
            dto.setLastModificationTime(param.getLastModificationTime().atZone(ZoneOffset.UTC));
        }
        
        return dto;
    }

    @Override
    public Book fromDto(BookDTO param) {
        // not implemented
        return null;
    }
    
}
