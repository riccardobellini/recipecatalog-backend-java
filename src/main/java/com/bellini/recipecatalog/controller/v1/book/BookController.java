package com.bellini.recipecatalog.controller.v1.book;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bellini.recipecatalog.exception.dishtype.NotExistingDishTypeException;
import com.bellini.recipecatalog.model.v1.Book;
import com.bellini.recipecatalog.model.v1.dto.book.BookDTO;
import com.bellini.recipecatalog.model.v1.mapper.book.BookResponseMapper;
import com.bellini.recipecatalog.service.v1.book.BookService;

@RestController
@RequestMapping("v1/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping(path = "", produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
    public ResponseEntity<Iterable<BookDTO>> getAllBooks(@RequestParam(name="q", required = false) String name, @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Iterable<Book> list = null;
        if (name != null) {
            list = bookService.get(name, pageable);
        } else {
            list = bookService.getAll(pageable);
        }
        Iterable<BookDTO> result = StreamSupport.stream(list.spliterator(), true)
                .map(book -> BookResponseMapper.getInstance().toDto(book))
                .collect(Collectors.toList());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE }, produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
    public ResponseEntity<Book> createBook(@RequestBody Book b) {
        Book insertedBk = bookService.create(b);
        return new ResponseEntity<>(insertedBk, HttpStatus.CREATED);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Book> getSingleBook(@PathVariable(value = "id") Long id) {
        Book b = bookService.get(id);

        if (b == null) {
            throw new NotExistingDishTypeException(id);
        }

        return new ResponseEntity<>(b, HttpStatus.OK);
    }

    @PutMapping(path = "/{id}", consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE }, produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
    public ResponseEntity<Book> update(@PathVariable(value = "id") Long id, @RequestBody Book dt) {
        return new ResponseEntity<Book>(bookService.update(id, dt), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public HttpEntity<String> delete(@PathVariable(value = "id") Long id) {
        bookService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
