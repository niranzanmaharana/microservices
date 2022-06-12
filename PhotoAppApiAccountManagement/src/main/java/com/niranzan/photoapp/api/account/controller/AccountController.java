/**
 * 
 */
package com.niranzan.photoapp.api.account.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author niranjanmaharana
 *
 */

@RestController
@RequestMapping("/account")
public class AccountController {

	@GetMapping("/status/check")
	public String status() {
		return "Working";
	}
}
