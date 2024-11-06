package com.eccomerce.auth_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "USER")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "USERNAME")
    private String username;

    @JsonIgnore
    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "USER_ROLE",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID")
    )
    private Set<Role> roles = new HashSet<>();

    @Column(name = "STATUS")
    private Character status;

    @JsonIgnore
    @Column(name = "VERSION")
    private long version;
    @JsonIgnore
    @Column(name = "CREATED_BY")
    private String createdBy;
    @JsonIgnore
    @Column(name = "LAST_UPDATED_BY")
    private String lastUpdatedBy;
    @JsonIgnore
    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;
    @JsonIgnore
    @Column(name = "LAST_UPDATED_DATE")
    private Timestamp lastUpdatedDate;
}
