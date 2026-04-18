package ru.tikonovns.capstone.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tikonovns.capstone.spring.entity.Section;

public interface SectionRepository extends JpaRepository<Section, Long> {
}
