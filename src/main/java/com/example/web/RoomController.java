package com.example.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.example.entity.CandidateRoomRelation;
import com.example.entity.CandidateRoomRelationPK;
import com.example.entity.Room;
import com.example.repository.RoomRepository;

public class RoomController {

	@Autowired
	RoomRepository roomRepository;

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

	@RequestMapping(value = "/applyForRoom/{idRoom}/{idCandidate}", method = RequestMethod.POST)
	public void applyForJob(@PathVariable int idRoom, @PathVariable int idCandidate) throws Exception {
		CandidateRoomRelation candidateRoomRelation = new CandidateRoomRelation();
		CandidateRoomRelationPK candidateRoomRelationPK = new CandidateRoomRelationPK();
		candidateRoomRelationPK.setIdCandidate(idCandidate);
		candidateRoomRelationPK.setIdRoom(idRoom);
		candidateRoomRelation.setCandidateRoomRelationPK(candidateRoomRelationPK);
		candidateRoomRelation.setApplied(true);
		candidateRoomRelation.setAppliedDate(new Date());

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
	}

	/****************************************************************/
	/**
	 * @author Wala Ben Amor
	 * 
	 *         Method to upload the room csv file
	 * @param request
	 */
	@RequestMapping(value = "/api/uploadRoomsFile", method = RequestMethod.POST)
	public void uploadRoomsFile(MultipartHttpServletRequest request) {
		try {
			// String UPLOADED_FOLDER = "/opt/apache-tomcat-8.5.24/csv/";
			String UPLOADED_FOLDER = "/home/mohamed/Documents/";
			Iterator<String> itr = request.getFileNames();
			MultipartFile fileExcel = null;
			while (itr.hasNext()) {
				String uploadedFile = itr.next();
				fileExcel = request.getFile(uploadedFile);
				byte[] bytes = fileExcel.getBytes();
				Path path = Paths.get(UPLOADED_FOLDER + fileExcel.getOriginalFilename());
				Files.write(path, bytes);
				String fileName = fileExcel.getOriginalFilename();
			}
			InputStream inputStream = fileExcel.getInputStream();

			OutputStream out = new FileOutputStream(new File(UPLOADED_FOLDER + fileExcel.getOriginalFilename()));
			int reader = 0;
			byte[] bytes = new byte[(int) fileExcel.getSize()];
			while ((reader = inputStream.read(bytes)) != -1) {
				out.write(bytes, 0, reader);
			}
			inputStream.close();
			out.flush();
			out.close();

			/*******************************************/
			String line = "";

			System.out.println("----------Room--OK---START---------");

			System.out.println(
					"----------UPLOADED_FOLDER----Room-------" + UPLOADED_FOLDER + fileExcel.getOriginalFilename());
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(new File(UPLOADED_FOLDER + fileExcel.getOriginalFilename()))));
			br.readLine();
			System.out.println("----------START---Room File--------");
			int i = 1;

			while ((line = br.readLine()) != null) {
				System.out.println("----------COMULM NUM-----------" + i);
				i = i + 1;
				// room_id name_building post_code detail price management_fee keymoney deposit
				// insurance

				String room_id = null; // 0
				String name_building = null; // 1
				String post_code = null; // 2
				String detail = null; // 3
				String price = null; // 4
				String management_fee = null; // 5
				/***********/
				String keymoney = null; // 6
				String deposit = null; // 7
				String insurance = null; // 8

				String[] column = line.split(",");

				if (column[0] != null && !column[0].equals("")) {
					room_id = column[0];
					System.out.println("/******** 0 *******room_id***");

				}
				if (column[1] != null && !column[1].equals("")) {
					name_building = column[1];
					System.out.println("/********1 *******name_building*******");

				}
				if (column[2] != null && !column[2].equals("")) {
					post_code = column[2];
					System.out.println("/******** 2 ***post_code*******" + post_code);

				}
				if (column[3] != null && !column[3].equals("")) {
					detail = column[3];
					System.out.println("/******** 3 *******detail*****" + detail);

				}
				if (column[4] != null && !column[4].equals("")) {
					price = column[4];
					System.out.println("/******** 4 *******price*****" + price);

				}
				if (column[5] != null) {
					management_fee = column[5];
					System.out.println("/******** 5*******management_fee*****" + management_fee);
					if (!column[5].equals("null")) {

					}

				}
				/************/
				if (column[6] != null && !column[6].equals("")) {
					keymoney = column[6];
					System.out.println("/******** 6 *******keymoney*****" + keymoney);

				}
				if (column[7] != null && !column[7].equals("")) {
					deposit = column[7];
					System.out.println("/******** 7 *******deposit*****" + deposit);

				}
				if (column[8] != null && !column[8].equals("")) {
					insurance = column[8];
					System.out.println("/******** 8 *******insurance*****" + insurance);

				}

				System.out.println("/*********************");
				Room room = new Room();
				if (name_building != "" && name_building != null) {
					room.setNameBuilding(name_building);
					System.out.println("/***set*****name_building**************");

				}
				if (post_code != "" && post_code != null) {
					room.setPostCode(post_code);
					System.out.println("/****set****post_code**************");

				}

				if (detail != "" && detail != null) {
					room.setDetail(detail);
					System.out.println("/***set*****detail**************");

				}
				if (price != "" && price != null) {
					room.setPrice(Integer.parseInt(price));
				}

				if (management_fee != "" && management_fee != null) {
					room.setManagementFee(Integer.parseInt(management_fee));
					System.out.println("/****set****management_fee**************");

				}

				if (keymoney != "" && keymoney != null) {
					room.setKeyMoney(Integer.parseInt(keymoney));
					System.out.println("/****set****keyMoney**************");

				}
				/**************/
				if (deposit != "" && deposit != null) {
					room.setDeposit(Integer.parseInt(deposit));
					System.out.println("/****set****deposit**************");

				}
				/***************/
				if (insurance != "" && insurance != null) {
					room.setInsurance(Integer.parseInt(insurance));
					System.out.println("/****set****management_fee**************");

				}

				roomRepository.saveAndFlush(room);

			}
			br.close();
			System.out.println("---ROOM------CLOSE--FINISH---UPLOAD------");

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("----ROOM-----CLOSE-----Exception------");
		}
	}

	/************************************************************/

}
