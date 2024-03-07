package com.example.library.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.library.model.Book;
import com.example.library.repository.BookRepository;

@RestController
@RequestMapping("/api")
public class BookController {
	
	@Autowired
	private BookRepository bookRepository;
	
	@PostMapping("/addBook")
	public ResponseEntity<Book> addBook(@RequestBody Book book) {
		try {
			Book newBook = bookRepository.saveAndFlush(book);
			return new ResponseEntity<>(newBook, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getBook/{id}")
	public ResponseEntity<Book> getBookById(@PathVariable("id") long id) {
		Optional<Book> tutorialData = bookRepository.findById(id);
		if (tutorialData.isPresent()) {
			return new ResponseEntity<>(tutorialData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping("/updateBook/{id}")
	public ResponseEntity<Book> updateTutorial(@PathVariable("id") long id, @RequestBody Book book) {
		Optional<Book> bookData = bookRepository.findById(id);

		if (bookData.isPresent()) {
			Book oldBook = bookData.get();
			oldBook.setRemark(book.getRemark());
			return new ResponseEntity<>(bookRepository.save(oldBook), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("/delBook/{id}")
	public ResponseEntity<HttpStatus> deleteBook(@PathVariable("id") long id) {
		try {
			bookRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
