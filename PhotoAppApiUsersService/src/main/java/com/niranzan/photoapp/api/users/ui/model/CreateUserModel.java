/**
 * 
 */
package com.niranzan.photoapp.api.users.ui.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author niranjanmaharana
 *
 */
public class CreateUserModel {
	@NotNull(message = "First name must not be empty!")
	@Size(min = 3, message = "First name must not be less than two chars!")
	private String firstName;
	@NotNull(message = "Last name must not be empty!")
	@Size(min = 3, message = "Last name must not be less than two chars!")
	private String lastName;
	@NotNull(message = "Password name must not be empty!")
	@Size(min = 8, max = 16, message = "Password must be of 8 to 16 char!")
	private String password;
	@NotNull(message = "Email name must not be empty!")
	@Email(message = "Invalid email!")
	private String email;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
