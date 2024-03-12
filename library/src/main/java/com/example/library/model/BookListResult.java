package com.example.library.model;

import java.util.List;

public class BookListResult {
	private boolean isEnd;
	private List<Book> results;
	
	public BookListResult(boolean isEnd, List<Book> results) {
		this.isEnd = isEnd;
		this.results = results;
	}
	
	public boolean isEnd() {
		return isEnd;
	}
	public void setEnd(boolean isEnd) {
		this.isEnd = isEnd;
	}
	public List<Book> getResults() {
		return results;
	}
	public void setResults(List<Book> results) {
		this.results = results;
	}
	
}
