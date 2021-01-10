package com.seth.waxandwanerecords.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.seth.waxandwanerecords.entities.Record;

public interface RecordRepository extends JpaRepository<Record, Long> {

	
	@Query(value="select * from record where user_id=:userid AND  assigned=0", nativeQuery = true)
	List<Record> findRecord(@Param("userid") int userId);	
	
	
	@Query(value="select * from record where user_id=:userid AND artist=:artist AND title=:title ",nativeQuery = true)
	List<Record> checkIfRecordsExist(@Param("userid") int userid, @Param("artist") String artist, @Param("title")String title);


	@Query(value="select * from record where user_id=:userid AND match(artist,title) against('Optional[query]'=:query in Natural Language Mode)", nativeQuery = true)
	List<Record> searchRecords(@Param("userid") int userid, @Param("query") String query);
	
	Record findById(long recordId);
}

