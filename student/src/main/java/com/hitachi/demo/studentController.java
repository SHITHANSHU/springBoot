package com.hitachi.demo;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLConnection;

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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;


@RestController
@RequestMapping("/student")
public class studentController {

		@Autowired
		private studentRep studentRepository;
		
		@GetMapping("getall")
		public List<student> getAllStudent()
		{
			System.out.println("getting data");
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
		
		@RequestMapping("/file/{fileName:.+}")
		public void downloadPDFResource(HttpServletRequest request, HttpServletResponse response,
				@PathVariable("fileName") String fileName) throws IOException {
			System.out.println("called");
			File file = new File("src/main/resources/temp_download/"+fileName);
			if (file.exists()) {

				//get the mimetype
				String mimeType = URLConnection.guessContentTypeFromName(file.getName());
				if (mimeType == null) {
					//unknown mimetype so set the mimetype to application/octet-stream
					mimeType = "application/octet-stream";
				}

				response.setContentType(mimeType);

				/**
				 * In a regular HTTP response, the Content-Disposition response header is a
				 * header indicating if the content is expected to be displayed inline in the
				 * browser, that is, as a Web page or as part of a Web page, or as an
				 * attachment, that is downloaded and saved locally.
				 * 
				 */

				/**
				 * Here we have mentioned it to show inline
				 */
				response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));

				 //Here we have mentioned it to show as attachment
				 //response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + file.getName() + "\""));

				response.setContentLength((int) file.length());

				InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

				FileCopyUtils.copy(inputStream, response.getOutputStream());

			
			}
		}
		
}
