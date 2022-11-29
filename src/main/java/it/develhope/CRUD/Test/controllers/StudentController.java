package it.develhope.CRUD.Test.controllers;

import it.develhope.CRUD.Test.entities.Student;
import it.develhope.CRUD.Test.repositories.StudentRepository;
import it.develhope.CRUD.Test.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;

    @PostMapping("")
    public @ResponseBody Student create(@RequestBody Student student){
        return studentRepository.save(student);
    }

    @GetMapping("")
    public @ResponseBody List<Student> getList(){
        return studentRepository.findAll();
    }

    @GetMapping("/{id}")
    public @ResponseBody Student getId(@PathVariable Long id ){
        Optional<Student> student= studentRepository.findById(id);
        if (student.isPresent()){
            return student.get();
        }else
            return null;
    }
    // aggiorna l'id dello studente
    @PutMapping("/{id}")
    public @ResponseBody Student update(@PathVariable Long id, @RequestBody Student student){
        student.setId(id);
        return studentRepository.save(student);
    }
    // aggiorna la colonna isWorking di uno studente
    @PutMapping("/{id}/work")
    public @ResponseBody Student update(@PathVariable Long id, @RequestParam("working") boolean working){
        return studentService.setStudentIsWorkingStatus(id, working);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        studentRepository.deleteById(id);
    }

}
