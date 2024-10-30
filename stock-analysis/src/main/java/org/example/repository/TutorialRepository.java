package org.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.example.model.Tutorial;

public interface TutorialRepository extends JpaRepository<Tutorial, Long> {
  List<Tutorial> findByPublished(boolean published);

  List<Tutorial> findByTitleContainingIgnoreCase(String title);
}
