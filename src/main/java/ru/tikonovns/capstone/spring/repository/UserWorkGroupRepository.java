package ru.tikonovns.capstone.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tikonovns.capstone.spring.entity.UserWorkGroup;
import ru.tikonovns.capstone.spring.entity.UserWorkGroupId;

public interface UserWorkGroupRepository extends JpaRepository<UserWorkGroup, UserWorkGroupId> {

}
