package com.example.library.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.library.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>{
	@Query(value = "SELECT * FROM book",
            countQuery = "SELECT count(*) FROM Book",
            nativeQuery = true)
    Page<Book> findAll1(Pageable pageable);
}
