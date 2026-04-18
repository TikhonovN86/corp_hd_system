package ru.tikonovns.capstone.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tikonovns.capstone.spring.entity.ServiceDirection;

public interface ServiceDirectionRepository extends JpaRepository<ServiceDirection, Long> {
}
