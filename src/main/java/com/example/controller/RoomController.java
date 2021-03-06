package com.example.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Candidate;
import com.example.entity.CandidateRoomRelation;
import com.example.entity.CandidateRoomRelationPK;
import com.example.entity.Room;
import com.example.repository.CandidateRepository;
import com.example.repository.CandidateRoomRepository;
import com.example.repository.RoomRepository;

import retrofit2.http.Body;

@RestController
public class RoomController {

	@Autowired
	private CandidateRoomRepository candidateRoomRepository;

	@Autowired
	private CandidateRepository candidateRepository;

	@Autowired
	private RoomRepository roomRepository;

	/**
	 * Method to apply for room and send email to address
	 * 
	 * @param idCandidate
	 *            represents the identifier of candidate passed as a request
	 *            parameter
	 * @param idRoom
	 * 
	 * @author Nour
	 * @throws Exception
	 * 
	 */
	@RequestMapping(value = "/applyForRoom", method = RequestMethod.POST)
	public LinkedHashMap<String, Object> applyForRoom(@RequestBody Map<String, String> js) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date preferedDate = new Date();
		try {
			preferedDate = formatter.parse(js.get("preferedDate"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String preferedTime = js.get("preferedTime");
		Room room = new Room();
		room = roomRepository.findOne(Integer.parseInt(js.get("idRoom")));
		Candidate candidate = new Candidate();
		candidate = candidateRepository.findOne(Integer.parseInt(js.get("idCandidate")));

		LinkedHashMap<String, Object> json = new LinkedHashMap<String, Object>();

		System.out.println("*******************************************************");
		CandidateRoomRelation candidateRoomRelation = new CandidateRoomRelation();
		CandidateRoomRelationPK candidateRoomRelationPK = new CandidateRoomRelationPK();
		candidateRoomRelationPK.setIdCandidate(candidate.getIdUserInformation());
		candidateRoomRelationPK.setIdRoom(room.getIdRoom());
		System.out.println("idCandidate: *****************" + candidate.getIdUserInformation());
		System.out.println("idRoom: **********************" + room.getIdRoom());
		candidateRoomRelation = candidateRoomRepository.findOne(candidateRoomRelationPK);

		if (candidateRoomRelation == null) {
			CandidateRoomRelation relation = new CandidateRoomRelation();
			relation.setCandidateRoomRelationPK(candidateRoomRelationPK);
			relation.setApplied(true);
			relation.setAppliedDate(new Date());
			relation.setPreferedDate(preferedDate);
			relation.setPreferedTime(preferedTime);
			candidateRoomRepository.saveAndFlush(relation);
			json.put("exist", false);
			// String subjectEmail = "新規応募： " + job.getShop().getNameShop() + " " +
			// job.getPositionName();
			// String toShop = job.getShop().getEmailShop();
			// String bodyEmail = "<html><body><br></br><br></br>" +
			// job.getShop().getNameShop()
			// + " 担当者様<br></br><br></br>新しい応募があります。<br></br> <br></br> 応募者: " +
			// candidate.getUserName()
			// + "<br></br>生年月日: " + candidate.getBirthday() + "<br></br>電話番号: " +
			// candidate.getPhone()
			// + "<br></br>JLPT: " + candidate.getjLPT() + "<br></br>滞在期間: " +
			// candidate.getDurationInJapan()
			// + "<br></br><br></br>応募ポジション: " + job.getPositionName()
			// + "<br></br><br></br>応募者と面接の日程を設定できましたら、こちらで入力してください<br></br><a
			// href='https://app.baitobot.jp/interviewDate"
			// + "/" + candidate.getIdUser() + "/" + idJob +
			// "'>https://app.baitobot.jp/interviewDate" + "/"
			// + candidate.getIdUser() + "/" + idJob + " </a>" + "<br></br><br></br> Find
			// Works JAPAN</body></html>";
			// if (toShop != null && !toShop.equals("")) {
			// try {
			// emailHtmlSender.send(toShop, subjectEmail, bodyEmail);
			// } catch (MessagingException e) {
			// e.printStackTrace();
			// }
			// }
		} else {
			candidateRoomRepository.saveAndFlush(candidateRoomRelation);
			json.put("exist", true);
		}
		return json;
	}

	@RequestMapping(value = "/initApply", method = RequestMethod.GET)
	public LinkedHashMap<String, Object> initApplyRoom(@RequestParam("idRoom") int idRoom,
			@RequestParam("idCandidate") int idCandidate) {
		LinkedHashMap<String, Object> json = new LinkedHashMap<String, Object>();
		CandidateRoomRelationPK candidateRoomRelationPK = new CandidateRoomRelationPK();
		candidateRoomRelationPK.setIdCandidate(idCandidate);
		candidateRoomRelationPK.setIdRoom(idRoom);

		CandidateRoomRelation relation = new CandidateRoomRelation();
		relation = candidateRoomRepository.findOne(candidateRoomRelationPK);
		if (relation != null) {
			json.put("candidateRoomRelation", relation);
			json.put("exist", true);
		} else {
			Room room = new Room();
			room = roomRepository.findOne(idRoom);
			Candidate candidate = new Candidate();
			candidate = candidateRepository.findOne(idCandidate);
			json.put("candidate", candidate);
			json.put("room", room);
			json.put("exist", false);
		}
		return json;
	}

}
