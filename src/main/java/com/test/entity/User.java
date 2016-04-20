package com.test.entity;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by Hafiz on 4/19/2016.
 */

@Document
public class User {

    @Id
    private String id;
    @Size(min = 2, max =50)
    @Pattern(regexp = "[A-Za-z. ]*", message = "First name requires valid character")
    private String firstname;

    @Size(min = 2, max = 50)
    @Pattern(regexp = "[A-Za-z. ]*", message = "Last name requires contain valid character")
    private String lastname;

    @Indexed( unique=true, sparse=true )
    @NotNull(message =  "Email requires valid value")
    @NotEmpty(message = "Email requires non empty value")
    @Email(message =    "Email requires valid format")
    private String email;

    @Pattern(regexp = "[0-9.\\-+ ]*", message = "Phone requires valid alphanumaric characters")
    private String phone;
    @Pattern(regexp = "[0-9.\\-+ ]*", message = "Cell requires valid alphanumaric characters")
    private String cell ;

    private Integer age;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
