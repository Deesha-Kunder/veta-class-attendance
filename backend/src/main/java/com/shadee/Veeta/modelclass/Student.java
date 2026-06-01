package com.shadee.Veeta.modelclass;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    String name;
    @Column(nullable = false, unique = true)
    String email;
    @Column(nullable = false)
    int courseHour;
    @Column(nullable = false)
    String batch;
    String profession;
    @Column(nullable = false)
    LocalDate joinedDate;
    @Column(name = "registered_by", nullable = false)
    private String registeredBy;
}
