package ru.tikonovns.capstone.spring.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor

/*
 Класс необходим для хранения (представления) составного первичного ключа
 для класса UserWorkGroup.
 Объединяет два внешних ключа userId и workGroupId в единый объект-ключ.

 С этим классом напрямую не работаем.
 */
public class UserWorkGroupId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "work_group_id")
    private Long workGroupId;
}
