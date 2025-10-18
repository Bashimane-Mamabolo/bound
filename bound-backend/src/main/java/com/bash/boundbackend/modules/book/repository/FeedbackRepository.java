package com.bash.boundbackend.modules.book.repository;

import com.bash.boundbackend.modules.book.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
}
