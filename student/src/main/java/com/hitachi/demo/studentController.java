package com.hitachi.demo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/student")
public class studentController {

		@Autowired
		private studentRep studentRepository;
		
		@GetMapping("getall")
		public List<student> getAllStudent()
		{
			return this.studentRepository.findAll();
		}
		
		@GetMapping("/get/{id}")
		public ResponseEntity<student> getstudnetById(@PathVariable(value = "id") Integer id) throws ResourceNotFoundException
		{
			student stud=studentRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Student not found with id : "+id));
			return ResponseEntity.ok().body(stud);
			
		}
		
		@PostMapping("insertStudent")
		public student createStudent(@RequestBody student Student)
		{
			return this.studentRepository.save(Student);
		}
		
		@PutMapping("update/{id}")
		public ResponseEntity<student> updateStudent(@PathVariable("id") Integer id,@Valid @RequestBody student studnetdetail) throws ResourceNotFoundException
		{
			student Student= studentRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Data to be updated not found"));
			Student.setId(studnetdetail.getId());
			Student.setClas(studnetdetail.getClas());
			Student.setMarks(studnetdetail.getMarks());
			Student.setName(studnetdetail.getName());
			
			return ResponseEntity.ok(this.studentRepository.save(Student));
			
		}
		
		@DeleteMapping("del/{id}")
		public Map<String, Boolean> deleteStudent(@PathVariable("id") Integer id) throws ResourceNotFoundException
		{
			student Student= studentRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Data to be updated not found"));
			
			this.studentRepository.delete(Student);
			Map<String,Boolean> resp=new HashMap<String, Boolean>();
			resp.put("deleted", Boolean.TRUE);

			return resp;
		}
		
}
