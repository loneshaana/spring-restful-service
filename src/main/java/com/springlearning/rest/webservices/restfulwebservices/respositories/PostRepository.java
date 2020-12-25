package com.springlearning.rest.webservices.restfulwebservices.respositories;

import com.springlearning.rest.webservices.restfulwebservices.beans.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
}
