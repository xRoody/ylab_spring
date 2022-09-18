package com.edu.ulab.app.facade;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.dto.UserDto;


import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.service.UserService;

import com.edu.ulab.app.web.request.UserBookRequest;
import com.edu.ulab.app.web.response.UserBookResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class UserDataFacade {
    private final UserService userService;
    private final BookService bookService;
    private final UserMapper userMapper;
    private final BookMapper bookMapper;

    public UserDataFacade(UserService userService,
                          BookService bookService,
                          UserMapper userMapper,
                          BookMapper bookMapper) {
        this.userService = userService;
        this.bookService = bookService;
        this.userMapper = userMapper;
        this.bookMapper = bookMapper;
    }

    public UserBookResponse createUserWithBooks(UserBookRequest userBookRequest) {
        log.info("Got user book create request: {}", userBookRequest);
        UserDto userDto = userMapper.userRequestToUserDto(userBookRequest.getUserRequest());
        log.info("Mapped user request: {}", userDto);
        UserDto createdUser = userService.createUser(userDto);
        log.info("Created user:{}", createdUser);
        return saveBooksLogic(userBookRequest, createdUser);
    }

    public UserBookResponse updateUserWithBooks(UserBookRequest userBookRequest) {
        log.info("Got user book create request: {}", userBookRequest);
        UserDto userDto=userService.updateUser(userMapper.userRequestToUserDto(userBookRequest.getUserRequest()));
        userService.clearBookSet(userDto.getId());
        List<Long> books=new ArrayList<>();
        if(userBookRequest.getBookRequests()!=null) {
            books = userBookRequest.getBookRequests()
                    .stream()
                    .filter(Objects::nonNull)
                    .map(bookMapper::bookRequestToBookDto)
                    .peek(bookDto -> bookDto.setUserId(userDto.getId()))
                    .peek(mappedBookDto -> log.info("mapped book: {}", mappedBookDto))
                    .map(bookService::updateBook)
                    .peek(createdBook -> log.info("Created book: {}", createdBook))
                    .map(BookDto::getId)
                    .toList();
            log.info("Collected book ids: {}", books);
        }

        return UserBookResponse.builder()
                .userId(userDto.getId())
                .booksIdList(books)
                .build();
    }

    private UserBookResponse saveBooksLogic(UserBookRequest userBookRequest, UserDto userDto){
        List<Long> books=new ArrayList<>();
        if(userBookRequest.getBookRequests()!=null) {
            books = userBookRequest.getBookRequests()
                    .stream()
                    .filter(Objects::nonNull)
                    .map(bookMapper::bookRequestToBookDto)
                    .peek(bookDto -> bookDto.setUserId(userDto.getId()))
                    .peek(mappedBookDto -> log.info("mapped book: {}", mappedBookDto))
                    .map(bookService::createBook)
                    .peek(createdBook -> log.info("Created book: {}", createdBook))
                    .map(BookDto::getId)
                    .toList();
            log.info("Collected book ids: {}", books);
        }

        return UserBookResponse.builder()
                .userId(userDto.getId())
                .booksIdList(books)
                .build();
    }

    public UserBookResponse getUserWithBooks(Long userId) {
        UserDto userDto=userService.getUserById(userId);
        UserBookResponse response=null;
        if (userDto!=null){
            response=UserBookResponse.builder()
                    .userId(userId)
                    .booksIdList(bookService.findAllBooksByUserId(userId).stream().map(BookDto::getId).toList()).build();
        }
        return response;
    }

    public void deleteUserWithBooks(Long userId) {
        for (BookDto x : bookService.findAllBooksByUserId(userId)) {
            bookService.deleteBookById(x.getId());
        }
        userService.deleteUserById(userId);
    }


}
