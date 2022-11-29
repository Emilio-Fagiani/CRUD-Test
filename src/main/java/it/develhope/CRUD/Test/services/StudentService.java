package it.develhope.CRUD.Test.services;

import it.develhope.CRUD.Test.entities.Student;
import it.develhope.CRUD.Test.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;


    public Student setStudentIsWorkingStatus(Long id, boolean isWorking){
        Optional<Student> student = studentRepository.findById(id);
        if(!student.isPresent()) return null;
        student.get().setWorking(isWorking);
        return studentRepository.save(student.get());
    }

}

