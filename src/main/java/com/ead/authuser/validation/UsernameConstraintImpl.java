package com.ead.authuser.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsernameConstraintImpl implements ConstraintValidator<UsernameConstraint, String>{

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(value == null || value.trim().isEmpty() || value.contains(" ")) {
			return false;
		}
		return true;
	}

}
