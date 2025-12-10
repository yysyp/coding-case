package com.poc.redis.entity;

/**
 * User entity representing a user stored in Redis.
 */
public class User extends AuditableEntity {
    
    private String id;
    private String username;
    private String email;
    private Integer age;
    
    // Constructors
    public User() {
        super();
    }
    
    public User(String id, String username, String email, Integer age) {
        super();
        this.id = id;
        this.username = username;
        this.email = email;
        this.age = age;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
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
    
    public Integer getAge() {
        return age;
    }
    
    public void setAge(Integer age) {
        this.age = age;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                '}';
    }
}
