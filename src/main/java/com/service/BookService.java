package com.service;

import com.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    Page<Book> getAllBooks(Pageable pageable);
    Page<Book> searchBooks(String keyword, Pageable pageable);
    Book getBookById(Long id);
    Book saveBook(Book book);
    Book updateBook(Long id, Book book);
    void deleteBook(Long id);
    boolean isIsbnExists(String isbn);
    boolean isIsbnExistsForUpdate(String isbn, Long id);
}