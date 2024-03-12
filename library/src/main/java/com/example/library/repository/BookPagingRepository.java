package com.example.library.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.example.library.model.Book;

@Repository
public interface BookPagingRepository extends PagingAndSortingRepository<Book, Long> {
 Iterable<Book> findAll(Sort sort);

 Page<Book> findAll(Pageable pageable);
}
