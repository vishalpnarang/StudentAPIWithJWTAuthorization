package com.vishal.students.controller;

import java.util.List;
import java.util.Optional;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vishal.students.exception.ResourceNotFoundException;
import com.vishal.students.model.Student;
import com.vishal.students.repository.StudentRepository;
import com.vishal.students.security.JwtUser;
import com.vishal.students.utils.JwtTokenUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api")
@Api(value = "Student Controllers", description = "Student CRUD operations along with other API's.")
public class StudentController {

	@Autowired
	StudentRepository repository;

	@Value("${jwt.header}")
	private String tokenHeader;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	@Qualifier("jwtUserDetailsService")
	private UserDetailsService userDetailsService;

	// get all students
	@GetMapping("/students")
	@PreAuthorize("hasRole('ADMIN')")
	@ApiOperation(value = "Get All Students", notes = "Will return StudentVO")
	public List<Student> getAllStudents() {
		return repository.findAll();
	}

	// add new student
	@PostMapping("/student")
	@PreAuthorize("hasRole('ADMIN')")
	@ApiOperation(value = "To Add new Student", notes = "Can accept First Name, Last Name, and marks for Maths, Science and English")
	public Student createStudent(@Valid @RequestBody Student student) {
		return repository.save(student);
	}

	// get single student by id
	/*
	 * @GetMapping("student/{id}")
	 * 
	 * @ApiOperation(value = "Get Student Details", notes = "Requires Roll No")
	 * public Student getStudentByRollNo(@PathVariable(value = "id") int rollNo)
	 * { return repository.findById(rollNo).orElseThrow(() -> new
	 * ResourceNotFoundException("Student", "id", rollNo)); }
	 */

	@GetMapping("/student/{id}")
	@PreAuthorize("hasRole('USER')")
	// @PostAuthorize ("returnObject.rollno == #id")
	@ApiOperation(value = "Get Student Details", notes = "Requires Roll No")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully retrieved student"),
	        @ApiResponse(code = 401, message = "You are not authorized to view this student"),
	        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	})
	public Student retrieveStudent(@PathVariable int id, HttpServletRequest request) throws AuthenticationException {
		System.out.println("Student request : " + request.toString());
		String authToken = request.getHeader(tokenHeader);
		final String token = authToken.substring(7);
		String username = jwtTokenUtil.getUsernameFromToken(token);
		JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
		Optional<Student> student = repository.findById(id);

		System.err.println("User + " + user.getFirstname());

		Student user1 = repository.findByUsername(user.getUsername());

		Student student2 = student.get();

		if (user1 != null) {
			System.err.println("name : " + user1.getFirstName());
			if (user1.getUsername() != student2.getUsername())
				throw new AuthenticationException("Not Authorize to see this record");
		}

		if (!student.isPresent())
			throw new ResourceNotFoundException("id-", "student", id);
		
		return student2;
	}

	// get students by first name
	@GetMapping("students/name/{name}")
	@PreAuthorize("hasRole('ADMIN')")
	@ApiOperation(value = "Get All Students by the First Name", notes = "Requires Name")
	public List<Student> getStudentsByFirstName(@PathVariable(value = "name") String name) {
		return repository.findByFirstNameContaining("%" + name + "%");
	}

	// get students by last name
	@GetMapping("students/lastname/{name}")
	@PreAuthorize("hasRole('ADMIN')")
	@ApiOperation(value = "Get All Students by the last Name", notes = "Requires Name")
	public List<Student> getStudentsByLastName(@PathVariable(value = "name") String name) {
		return repository.findAllByLastNameIgnoreCaseContaining(name);
	}

	// get math's toper
	@GetMapping("student/maths")
	@PreAuthorize("hasRole('ADMIN')")
	@ApiOperation(value = "Get Maths Toper", notes = "Will return Student Record")
	public Student getMathsTopper() {
		Student student = (Student) repository.getMaxMathsScorer().get(0);
		return student;
	}

	// get Science toper
	@GetMapping("student/science")
	@PreAuthorize("hasRole('USER')")
	@ApiOperation(value = "Get Science Toper", notes = "Will return Student Record")
	public Student getScienceTopper() {
		Student student = (Student) repository.getMaxScienceScorer().get(0);
		return student;
	}

	// get English toper
	@GetMapping("student/english")
	@PreAuthorize("hasRole('USER')")
	@ApiOperation(value = "Get English Toper", notes = "Will return Student Record")
	public Student getEnglishTopper() {
		Student student = (Student) repository.getMaxEnglishScorer().get(0);
		return student;
	}

	// update a student
	@PutMapping("student/{id}")
	@PreAuthorize("hasRole('USER')")
	@ApiOperation(value = "Update Student record", notes = "Requires Roll No")
	public Student updateStudent(@PathVariable(value = "id") int rollNo, @Valid @RequestBody Student studentDetails,
			HttpServletRequest request) throws AuthenticationException {

		String authToken = request.getHeader(tokenHeader);
		final String token = authToken.substring(7);
		String username = jwtTokenUtil.getUsernameFromToken(token);
		JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
		Optional<Student> student1 = repository.findById(rollNo);

		Student user1 = repository.findByUsername(user.getUsername());

		Student student2 = student1.get();

		if (user1 != null) {
			System.err.println("name : " + user1.getFirstName());
			if (user1.getUsername() != student2.getUsername())
				throw new AuthenticationException("Not Authorize to update this record");
		}

		Student student = repository.findById(rollNo)
				.orElseThrow(() -> new ResourceNotFoundException("Student", "id", rollNo));

		student.setFirstName(studentDetails.getFirstName());
		student.setLastName(studentDetails.getLastName());
		student.setMathsMarks(studentDetails.getMathsMarks());
		student.setScienceMarks(studentDetails.getScienceMarks());
		student.setEnglishMarks(studentDetails.getEnglishMarks());

		Student updatedStudent = repository.save(student);
		return updatedStudent;
	}

	// delete a student
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@DeleteMapping("/student/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@ApiOperation(value = "Delete Student record", notes = "Requires Roll No")
	public ResponseEntity<?> deleteStudent(@PathVariable(value = "id") int rollNo) {
		Student student = repository.findById(rollNo)
				.orElseThrow(() -> new ResourceNotFoundException("Student", "id", rollNo));

		repository.delete(student);

		return new ResponseEntity("Student deleted successfully", HttpStatus.OK);
	}

	// sorted list by first name ascending
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/students/sorted/firstname/asc")
	@ApiOperation(value = "Gell all students sorted by First Name Ascending", notes = "Will return all students ascending sorted First Name")
	public List<Student> sortedListFirstNameAsc() {
		return repository.getStudentsSortedFirstName();
	}

	// sorted list by last name ascending
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/students/sorted/lastname/asc")
	@ApiOperation(value = "Gell all students sorted by First Name Decending", notes = "Will return all students decending sorted First Name")
	public List<Student> sortedListLastNameAsc() {
		return repository.getStudentsSortedLastName();
	}

	// sorted list by first name descending
	@GetMapping("/students/sorted/firstname/desc")
	@PreAuthorize("hasRole('ADMIN')")
	@ApiOperation(value = "Gell all students sorted by Last Name Ascending", notes = "Will return all students ascending sorted Last Name")
	public List<Student> sortedListFirstNameDesc() {
		return repository.getStudentsSortedFirstNameDesc();
	}

	// sorted list by last name descending
	@GetMapping("/students/sorted/lastname/desc")
	@PreAuthorize("hasRole('ADMIN')")
	@ApiOperation(value = "Gell all students sorted by Last Name Decending", notes = "Will return all students decending sorted Last Name")
	public List<Student> sortedListLastNameDesc() {
		return repository.getStudentsSortedLastNameDesc();
	}
}
