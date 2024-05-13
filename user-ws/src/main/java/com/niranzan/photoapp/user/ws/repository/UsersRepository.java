package com.niranzan.photoapp.user.ws.repository;

import com.niranzan.photoapp.user.ws.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends CrudRepository<UserEntity, Long> {
}
