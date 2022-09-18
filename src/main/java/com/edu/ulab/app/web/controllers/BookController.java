package com.edu.ulab.app.web.controllers;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.web.constant.WebConstant;
import com.edu.ulab.app.web.response.BookResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = WebConstant.VERSION_URL + "/books",
        produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class BookController {
    private BookService bookService;

    @PostMapping
    public ResponseEntity<BookResponse> createBook(@RequestBody BookDto bookDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(BookResponse.builder().id(bookService.createBook(bookDto).getId()).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(@RequestBody BookDto bookDto, @PathVariable Long id){
        bookDto.setId(id);
        return ResponseEntity.status(HttpStatus.OK).body(BookResponse.builder().id(bookService.updateBook(bookDto).getId()).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBook(@PathVariable Long id){
        BookDto dto=bookService.getBookById(id);
        if (dto==null) throw new NotFoundException("Book with id="+id+" is not exists");
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @GetMapping()
    public ResponseEntity<List<BookDto>> getAll(){
        return ResponseEntity.status(HttpStatus.OK).body(bookService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        BookDto bookDto=bookService.deleteBookById(id);
        if (bookDto==null) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
