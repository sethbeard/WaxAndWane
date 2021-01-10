package com.seth.waxandwanerecords.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.seth.waxandwanerecords.entities.MoodSetter;

public interface MoodSetterRepository extends JpaRepository<MoodSetter, Integer> {
	
	@Query(value="select * from moodsetter where user_id=:userid AND  mood_id=:moodid", nativeQuery = true)
	List<MoodSetter> findRecord(@Param("userid") int userId, @Param("moodid") int moodId);	
	
	
	@Query(value="select * from moodsetter where record_id=:recordid AND  mood_id=:moodid", nativeQuery = true)
	List<MoodSetter> ifPairExists(@Param("recordid") long recordid, @Param("moodid") int moodId);	
	
	
	@Query(value="select * from moodsetter where record_id=:recordid", nativeQuery = true)
	List<MoodSetter> findByRecordId(@Param("recordid") long recordid);	
	
	
}
