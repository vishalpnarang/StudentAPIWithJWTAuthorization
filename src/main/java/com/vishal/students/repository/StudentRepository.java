package com.vishal.students.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vishal.students.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer>{

	//@Query(value = "from Student s where s.firstName LIKE CONCAT('%',:name,'%')")
	public List<Student> findByFirstNameContaining(String name);
	
	@Query(value = "from Student s where s.lastName LIKE CONCAT('%',:name,'%')")
	public List<Student> findAllByLastNameIgnoreCaseContaining(String name);
	
	@Query(value = "from Student s where mathsMarks = (select max(mathsMarks) from Student)")
	public List<Student> getMaxMathsScorer();
	
	@Query(value = "from Student s where scienceMarks = (select max(scienceMarks) from Student)")
	public List<Student> getMaxScienceScorer();
	
	@Query(value = "from Student s where englishMarks = (select max(englishMarks) from Student)")
	public List<Student> getMaxEnglishScorer();
	
	@Query(value = "from Student s order by firstName ASC")
	public List<Student> getStudentsSortedFirstName();
	
	@Query(value = "from Student s order by lastName ASC")
	public List<Student> getStudentsSortedLastName();
	
	@Query(value = "from Student s order by firstName Desc")
	public List<Student> getStudentsSortedFirstNameDesc();
	
	@Query(value = "from Student s order by lastName Desc")
	public List<Student> getStudentsSortedLastNameDesc();
	
	public Student findByUsername(String name);
}
