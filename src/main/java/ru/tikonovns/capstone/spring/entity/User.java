package ru.tikonovns.capstone.spring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "email")
    private String email;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    /*
    Это связь с таблицей roles.
    FetchType.LAZY - чтобы не тянуть роли и группы всегда.
    */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /*
    Связь many2many с work_groups через промежуточную таблицу user_work_groups.
    - mappedBy = "user" - указывает,
    что владелец не user, а внешний ключ хранится в UserWorkGroups
    - cascade = CascadeType.ALL - все операци над User распространяются на UserWorkGroups.
    - orphanRemoval = true - если объект удаляется из коллекции, он удаляется из БД.
    */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserWorkGroup> userWorkGroups = new HashSet<>();
}
