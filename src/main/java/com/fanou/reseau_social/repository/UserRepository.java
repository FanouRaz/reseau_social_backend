package com.fanou.reseau_social.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fanou.reseau_social.model.User;

public interface UserRepository extends JpaRepository<User,Long>{}
