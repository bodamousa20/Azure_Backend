package com.wuzzafni.backend.Repositary;

import com.wuzzafni.backend.Model.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job ,Long> {
}
