package com.example.library.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.library.model.Book;
import com.example.library.model.BookListResult;
import com.example.library.model.ResponseResult;
import com.example.library.repository.BookPagingRepository;
import com.example.library.repository.BookRepository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class BookController {
	
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private BookPagingRepository pagingRepository;
	
	@PostMapping("/addBook")
	public ResponseEntity<ResponseResult<Book>> addBook(@RequestBody @Valid Book book) {
		try {
			Book newBook = bookRepository.saveAndFlush(book);
			ResponseResult<Book> result = new ResponseResult<Book>(0, "", newBook);
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getBook/{id}")
	public ResponseEntity<ResponseResult<Book>> getBookById(@PathVariable("id") long id) {
		try {
			Optional<Book> tutorialData = bookRepository.findById(id);
			if (tutorialData.isPresent()) {
				ResponseResult<Book> result = new ResponseResult<Book>(0, "", tutorialData.get());
				return new ResponseEntity<>(result, HttpStatus.OK);
			} else {
				ResponseResult<Book> result = new ResponseResult<Book>(-99, "The book is not exist", null);
				return new ResponseEntity<>(result, HttpStatus.OK);
			}
		} catch(Exception e) {
			ResponseResult<Book> result = new ResponseResult<Book>(-99, "The book is not exist", null);
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
	}
	
	@PutMapping("/updateBook/{id}")
	public ResponseEntity<ResponseResult<Book>> updateTutorial(@PathVariable("id") long id, @RequestBody Book book) {
		try {
			Optional<Book> bookData = bookRepository.findById(id);

			if (bookData.isPresent()) {
				Book oldBook = bookData.get();
				oldBook.setName(book.getName());
				oldBook.setRemark(book.getRemark());
				ResponseResult<Book> result = new ResponseResult<Book>(0, "", bookRepository.save(oldBook));
				return new ResponseEntity<>(result, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/delBook/{id}")
	public ResponseEntity<ResponseResult<Long>> deleteBook(@PathVariable("id") long id) {
		try {
			bookRepository.deleteById(id);
			ResponseResult<Long> result = new ResponseResult<Long>(0, "", id);
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/bookList/{page}/{size}")
	public ResponseEntity<ResponseResult<BookListResult>> findAll(@PathVariable("page") Integer page, @PathVariable("size") Integer size){
		try {
			Sort sort = Sort.by(Sort.Direction.DESC, "id");
			Pageable pageable = PageRequest.of(page, size, sort);
			Page<Book> pageBook = pagingRepository.findAll(pageable);
			List list = pageBook.getContent();
			BookListResult listResult = new BookListResult(page >= pageBook.getTotalPages(), list);
			ResponseResult<BookListResult> result = new ResponseResult<BookListResult>(0, "", listResult);
			System.out.print(String.format("page= %d, totalPage=%d", page, pageBook.getTotalPages()));
			return new ResponseEntity<>(result, HttpStatus.OK);
		}  catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
