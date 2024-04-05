package com.example.cms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cms.model.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

}
