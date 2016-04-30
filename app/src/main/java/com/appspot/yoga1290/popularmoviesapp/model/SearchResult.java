package com.appspot.yoga1290.popularmoviesapp.model;

/**
 * Created by yoga1290 on 3/25/16.
 */
public class SearchResult {
    private int page;
    private int total_results;
    private int total_pages;
    private Movie movies[];


    public SearchResult(int page, int total_results, Movie movies[]) {
        this.page = page;
        this.total_results = total_results;
        this.movies = movies;
    }

    public Movie[] getMovies() {
        return movies;
    }

    public int getPage() {
        return page;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }
}
