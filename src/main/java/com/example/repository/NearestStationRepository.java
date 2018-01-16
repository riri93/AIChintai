package com.example.repository;

import java.io.Serializable;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.example.entity.NearestStation;

@Repository
@RepositoryRestResource
public interface NearestStationRepository extends JpaRepository<NearestStation, Serializable> {

	@Query("SELECT ns FROM NearestStation ns where lower(ns.japaneseRomajiStation) like lower(CONCAT('%',:station,'%')) or lower(ns.japaneseHiraganaStation) like lower(CONCAT('%',:station,'%')) or lower(ns.japaneseKatakanaStation) like lower(CONCAT('%',:station,'%'))  or lower(ns.japaneseStation) like lower(CONCAT('%',:station,'%'))")
	public Page<NearestStation> findNearestStationByStation(@Param("station") String station, Pageable pageable);

	@Query(value = "select ns from NearestStation ns where ns.idNearestStationCD=:idNearestStationCD")
	public NearestStation getNearStationByIdLineCD(@Param("idNearestStationCD") int idNearestStationCD);

	@Query("SELECT a FROM NearestStation a where a.idNearestStation = :id")
	public NearestStation findNearStationById(@Param("id") int id);

	@Query("SELECT ns FROM NearestStation ns where (lower(ns.japaneseRomajiStation) like lower(:station) or lower(ns.japaneseHiraganaStation) like lower(:station) or lower(ns.japaneseKatakanaStation) like lower(:station)  or lower(ns.japaneseStation) like lower(:station)) and (lower(ns.lineStation.lineNameJapanese) like lower(:line) or lower(ns.lineStation.lineNameKatakana) like lower(:line) or lower(ns.lineStation.lineNameHiragana) like lower(:line))")
	public NearestStation findStationByNameAndLine(@Param("station") String station, @Param("line") String line);

}
