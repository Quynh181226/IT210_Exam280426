package com.controller;

import com.entity.Book;
import com.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    private static final int PAGE_SIZE = 5;

    @GetMapping
    public String listBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(page, PAGE_SIZE, sort);
        Page<Book> bookPage;

        if (keyword != null && !keyword.trim().isEmpty()) {
            bookPage = bookService.searchBooks(keyword, pageable);
            model.addAttribute("keyword", keyword);
        } else {
            bookPage = bookService.getAllBooks(pageable);
        }

        model.addAttribute("books", bookPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", bookPage.getTotalPages());
        model.addAttribute("totalItems", bookPage.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equalsIgnoreCase("asc") ? "desc" : "asc");

        return "book/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("isEdit", false);
        return "book/form";
    }

    @PostMapping("/create")
    public String createBook(
            @Valid @ModelAttribute("book") Book book,
            BindingResult bindingResult,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (bookService.isIsbnExists(book.getIsbn())) {
            bindingResult.rejectValue("isbn", "error.book", "ISBN đã tồn tại trong hệ thống");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", false);
            model.addAttribute("currentPage", page);
            model.addAttribute("keyword", keyword);
            model.addAttribute("sortField", sortField);
            model.addAttribute("sortDir", sortDir);
            return "book/form";
        }

        bookService.saveBook(book);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm sách thành công!");

        return "redirect:/books?page=0&keyword=" + (keyword != null ? keyword : "") +
                "&sortField=" + sortField + "&sortDir=" + sortDir;
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model) {

        Book book = bookService.getBookById(id);
        model.addAttribute("book", book);
        model.addAttribute("isEdit", true);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);

        return "book/form";
    }

    @PostMapping("/update/{id}")
    public String updateBook(
            @PathVariable Long id,
            @Valid @ModelAttribute("book") Book book,
            BindingResult bindingResult,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (bookService.isIsbnExistsForUpdate(book.getIsbn(), id)) {
            bindingResult.rejectValue("isbn", "error.book", "ISBN đã tồn tại trong hệ thống");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", true);
            model.addAttribute("currentPage", page);
            model.addAttribute("keyword", keyword);
            model.addAttribute("sortField", sortField);
            model.addAttribute("sortDir", sortDir);
            return "book/form";
        }

        bookService.updateBook(id, book);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật sách thành công!");

        return "redirect:/books?page=" + page + "&keyword=" + (keyword != null ? keyword : "") +
                "&sortField=" + sortField + "&sortDir=" + sortDir;
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            RedirectAttributes redirectAttributes) {

        bookService.deleteBook(id);
        redirectAttributes.addFlashAttribute("successMessage", "Xóa sách thành công!");

        return "redirect:/books?page=" + page + "&keyword=" + (keyword != null ? keyword : "") +
                "&sortField=" + sortField + "&sortDir=" + sortDir;
    }
}