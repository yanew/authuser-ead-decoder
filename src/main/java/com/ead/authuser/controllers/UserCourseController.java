package com.ead.authuser.controllers;

import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ead.authuser.clients.CourseClient;
import com.ead.authuser.dtos.UserCourseDto;
import com.ead.authuser.models.UserCourseModel;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserCourseService;
import com.ead.authuser.services.UserService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserCourseController {

	@Autowired
	CourseClient userClient;
	
	 @Autowired
	 UserService userService;
	 
	 @Autowired
	 UserCourseService userCourseService;
	
	@GetMapping("/users/{userId}/courses")
	public ResponseEntity<Object> getAllCoursesByUser(
			@PageableDefault(page = 0, size = 1, sort = "courseId", direction = Sort.Direction.ASC) Pageable pageable,
			@PathVariable(value = "userId") UUID userId){
		Optional<UserModel> userModelOptional = userService.findById(userId);
		if(!userModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado!");
		}
		return ResponseEntity.status(HttpStatus.OK).body(userClient.getAllCoursesByUser(userId, pageable));
	}
	
	
	@PostMapping("/users/{userId}/courses/subscription")
	public ResponseEntity<Object> saveSubscriptionUserInCourse(@PathVariable(value = "userId") UUID userId,
																@RequestBody @Valid UserCourseDto userCourseDto){
		Optional<UserModel> userModelOptional = userService.findById(userId);
        if(!userModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado!");
        }
        if(userCourseService.existsByUserAndCourseId(userModelOptional.get(), userCourseDto.getCourseId())) {
        	return ResponseEntity.status(HttpStatus.CONFLICT).body("Usuário já cadastrado no curso!");
        }
	    
        UserCourseModel userCourseModel = userCourseService.save(
        		userModelOptional.get().convertToUserCourseModel(userCourseDto.getCourseId()));
        
        return ResponseEntity.status(HttpStatus.CREATED).body(userCourseModel);
	}
	
	
	@DeleteMapping("/users/courses/{courseId}")
	public ResponseEntity<Object> deleteUserCourseByCourse(@PathVariable(value = "courseId") UUID courseId){
		if(!userCourseService.existsByCourseId(courseId)){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("UserCourse não encontrado!");
		}
		userCourseService.deleteUserCourseByCourse(courseId);
		return ResponseEntity.status(HttpStatus.OK).body("UserCourse criado com sucesso!");
	}
	
}
