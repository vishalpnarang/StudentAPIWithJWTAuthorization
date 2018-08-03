package com.vishal.students.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@SuppressWarnings("serial")
@Entity
@Table(name = "students")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, allowGetters = true)
public class Student implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Roll_No")
	private int rollno;
	
	@NotBlank(message = "First Name cannot be blank")
	@Column(name = "First_Name")
	private String firstName;
	
	@NotNull(message = "Last Name cannot be Null")
	@Column(name = "Last_Name")
	private String lastName;
	
	//@Size(min=1, max=2,message = "Marks should be between 0 and 99")
	@Column(name = "Marks_Maths")
	private int mathsMarks;
	
	//@Size(min=1, max=2,message = "Marks should be between 0 and 99")
	@Column(name = "Marks_Science")
	private int scienceMarks;
	
	//@Size(min=1, max=2,message = "Marks should be between 0 and 99")
	@Column(name = "Marks_English")
	private int englishMarks;
	
	@Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;
    
    @Column(name = "username")
    private String username;

    /* 
        DecimalMax
		DecimalMin
		Digits
		Email
		Future
		FutureOrPresent
		Max
		Min
		Negative
		NegativeOrZero
		NotBlank
		NotEmpty
		NotNull
		Null
		Past
		PastOrPresent
		Pattern
		Positive
		PositiveOrZero
     * */

	public int getRollno() {
		return rollno;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setRollno(int rollno) {
		this.rollno = rollno;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getMathsMarks() {
		return mathsMarks;
	}

	public void setMathsMarks(int mathsMarks) {
		this.mathsMarks = mathsMarks;
	}

	public int getScienceMarks() {
		return scienceMarks;
	}

	public void setScienceMarks(int scienceMarks) {
		this.scienceMarks = scienceMarks;
	}

	public int getEnglishMarks() {
		return englishMarks;
	}

	public void setEnglishMarks(int englishMarks) {
		this.englishMarks = englishMarks;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
}
