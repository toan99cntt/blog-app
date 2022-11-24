package com.example.blog.model;

import java.lang.reflect.Array;
import java.util.List;

public class UserRO {
    private int id;
    private String name;
    private String username;
    private String email;
    private String created_at;
    private String password;
    private String phone_number;
    private String dob;
    private int gender;
    private Image[] avatar;
    private String[] _name;
    private String[] _phone_number;
    private String[] _dob;
    private String[] _gender;
    private String[] _email;
    private String[] _password;

    public String[] get_name() {
        return _name;
    }

    public Image[] getAvatar() {
        return avatar;
    }

    public void setAvatar(Image[] avatar) {
        this.avatar = avatar;
    }

    public void set_name(String[] _name) {
        this._name = _name;
    }

    public String[] get_phone_number() {
        return _phone_number;
    }

    public void set_phone_number(String[] _phone_number) {
        this._phone_number = _phone_number;
    }

    public String[] get_dob() {
        return _dob;
    }

    public void set_dob(String[] _dob) {
        this._dob = _dob;
    }

    public String[] get_email() {
        return _email;
    }

    public void set_email(String[] _email) {
        this._email = _email;
    }

    public String[] get_password() {
        return _password;
    }

    public void set_password(String[] _password) {
        this._password = _password;
    }

    public String[] get_gender() {
        return _gender;
    }

    public void set_gender(String[] _gender) {
        this._gender = _gender;
    }



    public String[] getR_name() {
        return _name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    private List<BlogRO> blogs;

    public String getPhone() {
        return phone_number;
    }

    public void setPhone(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public List<BlogRO> getBlogs() {
        return blogs;
    }

    public void setBlogs(List<BlogRO> blogs) {
        this.blogs = blogs;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRO() {
    }

    public UserRO(String name, String username, String email) {
        this.name = name;
        this.username = username;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
