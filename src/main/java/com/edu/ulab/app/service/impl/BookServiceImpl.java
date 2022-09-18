package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.repositories.BookRepo;
import com.edu.ulab.app.repositories.UserRepo;
import com.edu.ulab.app.service.BookService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {
    private BookMapper bookMapper;
    private BookRepo bookRepo;
    private UserRepo userRepo;

    @Override
    public BookDto createBook(BookDto bookDto) {
        Book b=bookMapper.bookDtoToBook(bookDto);
        if (bookDto.getUserId()!=null) {
            Optional<User> user= userRepo.findById(bookDto.getUserId());
            user.ifPresent(x -> {
                x.getBookSet().add(b);
                b.setUser(x);
            });
        }
        Book bb=bookRepo.persist(b);
        BookDto d=bookMapper.bookToBookDto(bb);
        if (bb.getUser()!=null) d.setUserId(bb.getUser().getId());
        return d;
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {
        return bookRepo.findById(bookDto.getId())
                .stream()
                .peek(x->{
                    if (bookDto.getTitle()!=null) x.setTitle(bookDto.getTitle());
                    if (bookDto.getAuthor()!=null) x.setAuthor(bookDto.getAuthor());
                    if (bookDto.getPageCount()!=0) x.setPageCount(bookDto.getPageCount());
                    if (x.getUser() != null && !x.getUser().getUser_id().equals(bookDto.getUserId())) {
                        for (int i=0; i<x.getUser().getBookSet().size(); i++){
                            if (x.getUser().getBookSet().get(i).getId().equals(x.getId())){
                                x.getUser().getBookSet().remove(i);
                                break;
                            }
                        }
                        if (bookDto.getUserId() != null) {
                            userRepo.findById(bookDto.getUserId()).ifPresent(v -> {
                                x.setUser(v);
                                v.getBookSet().add(x);
                            });
                        }
                    }
                }).map(x->bookMapper.bookToBookDto(x)).findAny().orElse(null);
    }

    @Override
    public BookDto getBookById(Long id) {
        Optional<Book> book=bookRepo.findById(id);
        if (book.isEmpty()) return null;
        BookDto dto=bookMapper.bookToBookDto(book.get());
        if (book.get().getUser()!=null) dto.setUserId(book.get().getUser().getId());
        return dto;
    }

    @Override
    public BookDto deleteBookById(Long id) {
        Book book=bookRepo.deleteById(id);
        BookDto dto=bookMapper.bookToBookDto(book);
        if (book.getUser()!=null) dto.setUserId(book.getUser().getId());
        return dto;
    }

    public List<BookDto> findAllBooksByUserId(Long id){
        return bookRepo.findAllBooksByUserId(id).stream().map(book -> {
            BookDto d=bookMapper.bookToBookDto(book);
            if (book.getUser()!=null) d.setUserId(book.getUser().getId());
            return d;
        }).collect(Collectors.toList());
    }

    public List<BookDto> findAll(){
        return bookRepo.findAll().stream().map(book -> {
            BookDto d=bookMapper.bookToBookDto(book);
            if (book.getUser()!=null) d.setUserId(book.getUser().getId());
            return d;
        }).toList();
    }
}
