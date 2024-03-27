package com.ead.authuser.services.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.ead.authuser.repositories.UserCourseRepository;
import com.ead.authuser.services.UserCourseService;

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
	
}
