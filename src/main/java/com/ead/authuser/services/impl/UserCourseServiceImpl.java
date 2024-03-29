package com.ead.authuser.services.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ead.authuser.models.UserCourseModel;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.repositories.UserCourseRepository;
import com.ead.authuser.services.UserCourseService;

@Service
public class UserCourseServiceImpl implements UserCourseService{

	/**Outra forma de injetar dependência. Ela disse que vai falar mais depois no workshop de arquitetura hexagonal
	 * final 
		UserCourseRepository userCourseRepository;
		
		public UserCourseServiceImpl(UserCourseRepository userCourseRepository) {
			this.userCourseRepository = userCourseRepository;
		}
	 */
	
	@Autowired
	private UserCourseRepository userCourseRepository;

	@Override
	public boolean existsByUserAndCourseId(UserModel userModel, UUID courseId) {
		return userCourseRepository.existsByUserAndCourseId(userModel, courseId);
	}

	@Override
	public UserCourseModel save(UserCourseModel userCourseModel) {
		return userCourseRepository.save(userCourseModel);
	}
	
	
	
}
