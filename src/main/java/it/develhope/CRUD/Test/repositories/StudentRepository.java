package it.develhope.CRUD.Test.repositories;

import it.develhope.CRUD.Test.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> { }
