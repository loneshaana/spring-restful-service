package com.springlearning.rest.webservices.restfulwebservices.respositories;

import com.springlearning.rest.webservices.restfulwebservices.beans.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
}
