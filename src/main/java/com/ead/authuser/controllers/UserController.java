package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<List<UserModel>> getAllUSers(){
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getOneUser(@PathVariable(value = "userId") UUID userId){
        Optional<UserModel> userModelOptional = userService.findById(userId);
        if(!userModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado!");
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(userModelOptional.get());
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "userId") UUID userId){
        Optional<UserModel> userModelOptional = userService.findById(userId);
        if(!userModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado!");
        }else{
            userService.delete(userModelOptional.get());
            return ResponseEntity.status(HttpStatus.OK).body("Usuário deletado com sucesso!");
        }
    }
    
    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable(value = "userId") UUID userId,
    		@RequestBody @Validated(UserDto.UserView.UserPut.class) 
    		@JsonView(UserDto.UserView.UserPut.class) UserDto userDto){
        Optional<UserModel> userModelOptional = userService.findById(userId);
        if(!userModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado!");
        }else{
        	var userModel = userModelOptional.get();
        	userModel.setFullName(userDto.getFullName());
        	userModel.setPhoneNumber(userDto.getPhoneNumber());
        	userModel.setCpf(userDto.getCpf());
        	userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        	
            userService.save(userModel);
            return ResponseEntity.status(HttpStatus.OK).body("Usuário atualizado com sucesso!");
        }
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<Object> updatePassword(@PathVariable(value = "userId") UUID userId,
    		@RequestBody @Validated(UserDto.UserView.PasswordPut.class) 
    		@JsonView(UserDto.UserView.PasswordPut.class) UserDto userDto){
        Optional<UserModel> userModelOptional = userService.findById(userId);
        if(!userModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado!");
        }else if(!userModelOptional.get().getPassword().equals(userDto.getOldPassword())) {
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("O campo 'Senha anterior' informa senha diferente da atual!");
        } else{
        	var userModel = userModelOptional.get();
        	
        	if(userModel.getPassword().equals(userDto.getPassword())) {
        		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Você informou a mesma senha!");	
        	}else {
        		userModel.setPassword(userDto.getPassword());
            	userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            	
                userService.save(userModel);
                return ResponseEntity.status(HttpStatus.OK).body("Usuário atualizado com sucesso!");	
        	}
        }
    }
    
    @PutMapping("/{userId}/image")
    public ResponseEntity<Object> updateImage(@PathVariable(value = "userId") UUID userId,
    		@RequestBody @Validated(UserDto.UserView.ImagePut.class) 
    		@JsonView(UserDto.UserView.ImagePut.class) UserDto userDto){
        Optional<UserModel> userModelOptional = userService.findById(userId);
        if(!userModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado!");
        }else{
        	var userModel = userModelOptional.get();
        	userModel.setImageUrl(userDto.getImageUrl());
        	userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        	
            userService.save(userModel);
            return ResponseEntity.status(HttpStatus.OK).body(userModel);
        }
    }

}
