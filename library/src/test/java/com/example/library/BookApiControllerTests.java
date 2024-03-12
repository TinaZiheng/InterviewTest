//package com.example.library;
//
//import org.junit.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import com.example.library.controller.BookController;
//import com.example.library.model.Book;
//import com.example.library.repository.BookRepository;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//@WebMvcTest(BookController.class)
//public class BookApiControllerTests {
//	private static final String END_POINT_PATH = "/api";
//	private static final String ADD_BOOK_PATH = "/addBook";
//	private static final String GET_BOOK_PATH = "/getBook";
//	private static final String UPDATE_BOOK_PATH = "/updateBook";
//	private static final String DELETE_BOOK_PATH = "/delBook";
//	
//	@Autowired
//	private MockMvc mockMvc;
//	@Autowired
//	private ObjectMapper objectMapper;
//	@MockBean 
//	BookRepository bookRepository;
//	
//	@Test
//	public void testAddBookShouldReturn400BadRequest()throws Exception {
//		Book book = new Book();
//		book.setName("");
//		book.setRemark("");
//		
//		String requestBody = objectMapper.writeValueAsString(book);
//		mockMvc.perform(
//				MockMvcRequestBuilders.post(END_POINT_PATH + ADD_BOOK_PATH)
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(requestBody))
//				.andExpect(MockMvcResultMatchers.status().isOk())
//				.andDo(MockMvcResultHandlers.print());
//		
//	}
//	
//	@Test
//	public void testAddShouldReturn201Created() throws Exception {
//		Book book = new Book();
//		book.setName("Thinking in Java");
//		book.setRemark("For beginners and experts alike. Teaches Java linguistics, not platform-dependent mechanics");
//		
//		Mockito.when(bookRepository.saveAndFlush(book)).thenReturn(book);
//		
//		String requestBody = objectMapper.writeValueAsString(book);
//		
//		mockMvc.perform(MockMvcRequestBuilders.post(END_POINT_PATH + ADD_BOOK_PATH)
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(requestBody))
//				.andExpect(MockMvcResultMatchers.status().isCreated())
//				.andDo(MockMvcResultHandlers.print());
//	
//	}
//}
