package ru.tikonovns.capstone.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tikonovns.capstone.spring.entity.TicketType;

public interface TicketTypeRepository extends JpaRepository<TicketType, Long> {
}
