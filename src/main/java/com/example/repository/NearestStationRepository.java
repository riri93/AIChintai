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

	@Query(value = "SELECT n.* from nearest_station where lower(n.japanese_station) like lower(CONCAT('%',:station,'%')) or abs(cast( GREATEST(length(:station),length(n.japanese_station)) -levenshtein( lower(:station),lower(n.japanese_station)) as float))/length(:station)*100 >40 or lower(n.japanese_hiragana_station) like lower(CONCAT('%',:station,'%')) or abs(cast( GREATEST(length(:station), length(n.japanese_hiragana_station))- levenshtein(lower(:station),n.japanese_hiragana_station)as float))/length(:station)*100>40 or lower(n.japanese_katakana_station) like lower(CONCAT('%',:station,'%') ) or   abs(cast( GREATEST(length(:station),length(n.japanese_katakana_station))-levenshtein(lower(:station),lower(n.japanese_katakana_station))as float))/length(:station)*100>40 or lower(n.japanese_romaji_station) like lower(CONCAT('%',:station,'%') ) or  abs(cast(GREATEST(length(:station), length(n.japanese_romaji_station))-levenshtein( lower(:station),n.japanese_romaji_station) as  float))/length(:station) *100>40 GROUP BY   n.id_nearest_station ORDER BY GREATEST(abs(cast(GREATEST(length(:station), length(n.japanese_romaji_station))-levenshtein( lower(:station),n.japanese_romaji_station) as  float))/length(:station) *100,abs(cast( GREATEST(length(:station), length(n.japanese_hiragana_station))- levenshtein(lower(:station),n.japanese_hiragana_station)as float))/length(:station)*100,abs(cast( GREATEST(length(:station),length(n.japanese_station)) -levenshtein( lower(:station),lower(n.japanese_station)) as float))/length(:station)*100,abs(cast( GREATEST(length(:station),length(n.japanese_katakana_station))-levenshtein(lower(:station),lower(n.japanese_katakana_station))as float))/length(:station)*100) desc ,    ?#{#pageable} ", nativeQuery = true, countQuery = "SELECT count(n.*) from nearest_station where lower(n.japanese_station) like lower(CONCAT('%',:station,'%')) or abs(cast( GREATEST(length(:station),length(n.japanese_station)) -levenshtein( lower(:station),lower(n.japanese_station)) as float))/length(:station)*100 >40 or lower(n.japanese_hiragana_station) like lower(CONCAT('%',:station,'%'))   or abs(cast( GREATEST(length(:station), length(n.japanese_hiragana_station))- levenshtein(lower(:station),n.japanese_hiragana_station)as float))/length(:station)*100>40 or lower(n.japanese_katakana_station) like lower(CONCAT('%',:station,'%') ) or   abs(cast( GREATEST(length(:station),length(n.japanese_katakana_station))-levenshtein(lower(:station),lower(n.japanese_katakana_station))as float))/length(:station)*100>40 or lower(n.japanese_romaji_station) like lower(CONCAT('%',:station,'%') ) or  abs(cast(GREATEST(length(:station), length(n.japanese_romaji_station))-levenshtein( lower(:station),n.japanese_romaji_station) as  float))/length(:station) *100>40 GROUP BY   n.id_nearest_station ORDER BY GREATEST(abs(cast(GREATEST(length(:station), length(n.japanese_romaji_station))-levenshtein( lower(:station),n.japanese_romaji_station) as  float))/length(:station) *100,abs(cast( GREATEST(length(:station), length(n.japanese_hiragana_station))- levenshtein(lower(:station),n.japanese_hiragana_station)as float))/length(:station)*100,abs(cast( GREATEST(length(:station),length(n.japanese_station)) -levenshtein( lower(:station),lower(n.japanese_station)) as float))/length(:station)*100,abs(cast( GREATEST(length(:station),length(n.japanese_katakana_station))-levenshtein(lower(:station),lower(n.japanese_katakana_station))as float))/length(:station)*100) desc ,  ?#{#pageable}")
	public Page findStations(@Param("station") String station, Pageable pageable);

	@Query("SELECT ns FROM NearestStation ns where lower(ns.japaneseRomajiStation) like lower(CONCAT('%',:station,'%')) or lower(ns.japaneseHiraganaStation) like lower(CONCAT('%',:station,'%')) or lower(ns.japaneseKatakanaStation) like lower(CONCAT('%',:station,'%'))  or lower(ns.japaneseStation) like lower(CONCAT('%',:station,'%'))")
	public Page<NearestStation> findNearestStationByStation(@Param("station") String station, Pageable pageable);

	@Query("SELECT a FROM NearestStation a where a.idNearestStation =:id")
	public NearestStation findNearStationById(@Param("id") int id);

	@Query("SELECT ns FROM NearestStation ns where (lower(ns.japaneseRomajiStation) like lower(:station) or lower(ns.japaneseHiraganaStation) like lower(:station) or lower(ns.japaneseKatakanaStation) like lower(:station)  or lower(ns.japaneseStation) like lower(:station)) and (lower(ns.lineStation.lineNameJapanese) like lower(:line) or lower(ns.lineStation.lineNameKatakana) like lower(:line) or lower(ns.lineStation.lineNameHiragana) like lower(:line))")
	public NearestStation findStationByNameAndLine(@Param("station") String station, @Param("line") String line);

}
