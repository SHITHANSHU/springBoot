package com.hitachi.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface studentRep extends JpaRepository<student, Integer>{

}
