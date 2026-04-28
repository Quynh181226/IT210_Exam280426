package com.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.Year;

@Entity
@Table(name = "books", uniqueConstraints = @UniqueConstraint(columnNames = "isbn"))
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(min = 5, max = 150, message = "Tiêu đề phải từ 5 đến 150 ký tự")
    @Column(nullable = false, length = 150)
    private String title;

    @NotBlank(message = "Tác giả không được để trống")
    @Size(min = 2, max = 100, message = "Tác giả phải từ 2 đến 100 ký tự")
    @Column(nullable = false, length = 100)
    private String author;

    @NotBlank(message = "ISBN không được để trống")
    @Pattern(regexp = "\\d{13}", message = "ISBN phải gồm đúng 13 chữ số")
    @Column(nullable = false, unique = true, length = 13)
    private String isbn;

    @NotNull(message = "Năm xuất bản không được để trống")
    @Min(value = 1900, message = "Năm xuất bản phải từ 1900")
    @Max(value = 2026, message = "Năm xuất bản không được vượt quá năm hiện tại")
    @Column(nullable = false)
    private Integer publicationYear;

    @NotNull(message = "Giá bán không được để trống")
    @DecimalMin(value = "1000", message = "Giá bán phải từ 1.000 VND")
    @DecimalMax(value = "1000000", message = "Giá bán không được quá 1.000.000 VND")
    @Column(nullable = false)
    private Double price;

    public Book() {}

    public Book(String title, String author, String isbn, Integer publicationYear, Double price) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
        this.price = price;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public Integer getPublicationYear() { return publicationYear; }
    public void setPublicationYear(Integer publicationYear) { this.publicationYear = publicationYear; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}