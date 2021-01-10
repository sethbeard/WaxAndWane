package com.seth.waxandwanerecords.repositories;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.seth.waxandwanerecords.entities.Mood;

public interface MoodRepository extends JpaRepository<Mood, Integer> {
 

@Query(value="select * from mood where user_id=:userid", nativeQuery = true)
List<Mood> findMoods(@Param("userid") int userId);

@Query(value="select * from mood where user_id=:userid AND name=:moodName", nativeQuery = true)
Mood findMoodByName(@Param("userid") int userId, @Param("moodName") String moodName);
}