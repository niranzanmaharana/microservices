/**
 * 
 */
package com.niranzan.photoapp.api.users.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author niranjanmaharana
 *
 */

@Repository
public interface UsersRepository extends CrudRepository<UserEntity, Long> {
	UserEntity findByEmail(String email);
}
