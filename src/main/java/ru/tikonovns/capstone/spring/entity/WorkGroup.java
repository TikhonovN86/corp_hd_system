package ru.tikonovns.capstone.spring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "work_groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    /*
     Нет связи с user_work_groups,
     потому что связь с user_work_groups будет осуществляться,
     возможно в UserRepository, чтобы не делать двунаправленную связь:
    */
}
