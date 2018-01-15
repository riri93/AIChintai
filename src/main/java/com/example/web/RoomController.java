package com.example.web;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.entity.Candidate;
import com.example.entity.CandidateRoomRelation;
import com.example.entity.CandidateRoomRelationPK;

public class RoomController {

	/**
	 * Method to apply for room and send email to address
	 * 
	 * @param idCandidate
	 *            represents the identifier of candidate passed as a request
	 *            parameter
	 * @param idJob
	 *            represents the identifier of job that candidate applied for and
	 *            which is passed as a request parameter
	 * 
	 * @author Nour
	 * @throws Exception
	 * 
	 */
	@RequestMapping(value = "/applyForRoom", method = RequestMethod.POST)
	public void applyForJob(@RequestParam(name = "idRoom") int idRoom, @RequestBody @Valid Candidate candidate)
			throws Exception {
		CandidateRoomRelation candidateRoomRelation = new CandidateRoomRelation();
		CandidateRoomRelationPK candidateRoomRelationPK = new CandidateRoomRelationPK();
		candidateRoomRelationPK.setIdCandidate(candidate.getIdUserInformation());
		candidateRoomRelationPK.setIdRoom(idRoom);
		candidateRoomRelation.setCandidateRoomRelationPK(candidateRoomRelationPK);
		candidateRoomRelation.setApplied(true);
		candidateRoomRelation.setAppliedDate(new Date());
		
//		String subjectEmail = "新規応募： " + job.getShop().getNameShop() + "  " + job.getPositionName();
//		String toShop = job.getShop().getEmailShop();
//		String bodyEmail = "<html><body><br></br><br></br>" + job.getShop().getNameShop()
//				+ " 担当者様<br></br><br></br>新しい応募があります。<br></br> <br></br> 応募者: " + candidate.getUserName()
//				+ "<br></br>生年月日: " + candidate.getBirthday() + "<br></br>電話番号: " + candidate.getPhone()
//				+ "<br></br>JLPT: " + candidate.getjLPT() + "<br></br>滞在期間: " + candidate.getDurationInJapan()
//				+ "<br></br><br></br>応募ポジション: " + job.getPositionName()
//				+ "<br></br><br></br>応募者と面接の日程を設定できましたら、こちらで入力してください<br></br><a href='https://app.baitobot.jp/interviewDate"
//				+ "/" + candidate.getIdUser() + "/" + idJob + "'>https://app.baitobot.jp/interviewDate" + "/"
//				+ candidate.getIdUser() + "/" + idJob + " </a>" + "<br></br><br></br> Find Works JAPAN</body></html>";
//		if (toShop != null && !toShop.equals("")) {
//			try {
//				emailHtmlSender.send(toShop, subjectEmail, bodyEmail);
//			} catch (MessagingException e) {
//				e.printStackTrace();
//			}
//		}
	}

}
