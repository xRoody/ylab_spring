package com.edu.ulab.app.service.impl;


import com.edu.ulab.app.dto.UserDto;

import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.repositories.UserRepo;
import com.edu.ulab.app.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;



@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserMapper userMapper;
    private UserRepo userRepo;


    @Override
    public UserDto createUser(UserDto userDto) {
        User user = userMapper.userDtoToUser(userDto);
        user.setBookSet(new ArrayList<>());
        return userMapper.userToUserDto(userRepo.persist(user));
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        return userMapper.userToUserDto(userRepo.findById(userDto.getId())
                .stream()
                .peek(x->{
                    if (userDto.getAge()>=0) x.setAge(userDto.getAge());
                    if (userDto.getFullName()!=null) x.setFullName(userDto.getFullName());
                    if (userDto.getTitle()!=null) x.setTitle(userDto.getTitle());
                })
                .findFirst()
                .orElse(null));
    }

    @Override
    public UserDto getUserById(Long id) {
        return userMapper.userToUserDto(userRepo.findById(id).orElse(null));
    }

    @Override
    public UserDto deleteUserById(Long id) {
        return userMapper.userToUserDto(userRepo.deleteById(id));
    }

    public void clearBookSet(Long id){
        userRepo.findById(id).ifPresent(x->{
            x.getBookSet().forEach(y->y.setUser(null));
            x.getBookSet().clear();
        });
    }

    public void removeBook(Long userId, Long bookId){
        userRepo.findById(userId).ifPresent(x->{
            for (int i=0; i<x.getBookSet().size(); i++){
                if (x.getBookSet().get(i).getId().equals(bookId)) {
                    x.getBookSet().get(i).setUser(null);
                    x.getBookSet().remove(i);
                    break;
                }
            }
        });
    }
}
