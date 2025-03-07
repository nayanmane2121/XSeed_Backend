package com.tcognition.recruiterservice.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcognition.recruiterservice.Factory.MeetingInviteFactory;
import com.tcognition.recruiterservice.dto.EmailAttachmentRequestDTO;
import com.tcognition.recruiterservice.dto.EmailRequestDTO;
import com.tcognition.recruiterservice.dto.InterviewDetailsDTO;
import com.tcognition.recruiterservice.dto.JobDetailsRequestDTO;
import com.tcognition.recruiterservice.dto.ResponseDTO;
import com.tcognition.recruiterservice.entity.InterviewDetailsEntity;
import com.tcognition.recruiterservice.entity.JobDetailsEntity;
import com.tcognition.recruiterservice.entity.PanelEntity;
import com.tcognition.recruiterservice.repository.InterviewDetailsRepository;
import com.tcognition.recruiterservice.repository.JobDetailsRepository;
import com.tcognition.recruiterservice.repository.PanelRepository;
import com.tcognition.recruiterservice.service.InterviewService;
import com.tcognition.recruiterservice.service.MeetingInviteSender;
import com.tcognition.recruiterservice.utils.AppConstants;
import com.tcognition.recruiterservice.utils.MessageConstants;
import com.tcognition.recruiterservice.utils.SQLConstants;
import com.tcognition.recruiterservice.service.EmailServiceClient;

import jakarta.transaction.Transactional;

@Service
public class InterviewServiceImpl implements InterviewService {

	private static final Logger logger = LoggerFactory.getLogger(InterviewServiceImpl.class);

	@Autowired
	private InterviewDetailsRepository interviewDetailsRepository;

	@Autowired
	private PanelRepository panelRepository;

	@Autowired
	JobDetailsRepository jobDetailsRepository;

	@Value("${recruiter.jobdescription.filepath}")
	private String JDFilepath;

	@Value("${recruiter.email.filespath}")
	private String emailFilesPath;

	@Autowired
	Connection connection;

	@Autowired
	EmailServiceClient emailServiceClient;

	@Override
	public ResponseDTO getinterviewdetails(JobDetailsRequestDTO JobDetailsRequestDTO) {

		InterviewDetailsDTO interviewDetailsDTO = new InterviewDetailsDTO();
		try (PreparedStatement preparedStatement = connection
				.prepareStatement(SQLConstants.PROCEDURE_FETCH_INTERVIEW_DETAILS_BY_JOB)) {

			preparedStatement.setLong(1, JobDetailsRequestDTO.getJobId());

			List<PanelEntity> panel = new ArrayList<>();

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {

					PanelEntity panelEntity = new PanelEntity();

					interviewDetailsDTO.setInterviewId(resultSet.getLong("interview_id"));
					interviewDetailsDTO.setJobId(resultSet.getLong("job_id"));
					interviewDetailsDTO.setJobTitle(resultSet.getString("job_title"));
					Timestamp interviewTimestamp = resultSet.getTimestamp("interview_date_time");
					if (interviewTimestamp != null) {
						interviewDetailsDTO.setInterviewDateTime(interviewTimestamp.toLocalDateTime());
					}
					interviewDetailsDTO.setPlatform(resultSet.getString("platform"));
					interviewDetailsDTO.setCandidateEmail(resultSet.getString("candidate_email"));
					interviewDetailsDTO.setCandidateName(resultSet.getString("candidate_name"));
					interviewDetailsDTO.setJobDescription(resultSet.getString("job_description"));

					panelEntity.setId(resultSet.getLong("panel_id"));
					panelEntity.setPanelName(resultSet.getString("panel_name"));
					panelEntity.setPanelEmail(resultSet.getString("panel_email"));

					panel.add(panelEntity);

				}

				interviewDetailsDTO.setPanel(panel);

				if (interviewDetailsDTO.getJobDescription() != null
						|| !interviewDetailsDTO.getJobDescription().isEmpty()) {

					System.out.println(interviewDetailsDTO.getJobDescription());

					interviewDetailsDTO.setJDFilePath(
							generateJDfile(interviewDetailsDTO.getJobTitle(), interviewDetailsDTO.getJobDescription()));
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return ResponseDTO.error(e.getMessage());
		}
		return ResponseDTO.success(interviewDetailsDTO);
	}

	private String generateJDfile(String jobTitle, String jobDescription) {

		String filePath = JDFilepath + AppConstants.JDFILE_PREFIX + jobTitle + AppConstants.FILE_FORMAT_PDF;

		try (PDDocument document = new PDDocument()) {
			PDPage page = new PDPage();
			document.addPage(page);

			PDPageContentStream contentStream = new PDPageContentStream(document, page);
			contentStream.setFont(PDType1Font.HELVETICA, 12);
			contentStream.beginText();
			contentStream.newLineAtOffset(70, 700);

			float pageWidth = page.getMediaBox().getWidth() - 140; // 70px margin on left and right
			float lineSpacing = 14f;

			jobDescription = jobDescription.replace("\r", ""); // Remove carriage returns
			List<String> wrappedLines = wrapText(jobDescription, PDType1Font.HELVETICA, 12, pageWidth);

			for (String line : wrappedLines) {
				contentStream.showText(line);
				contentStream.newLineAtOffset(0, -lineSpacing);
			}

			contentStream.endText();
			contentStream.close();

			document.save(filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return filePath;
	}

	// **Helper method for text wrapping**
	private List<String> wrapText(String text, PDType1Font font, int fontSize, float maxWidth) throws IOException {
		List<String> wrappedLines = new ArrayList<>();
		String[] lines = text.split("\n");

		for (String line : lines) {
			String[] words = line.split(" ");
			StringBuilder newLine = new StringBuilder();
			float lineWidth = 0;

			for (String word : words) {
				float wordWidth = font.getStringWidth(word) / 1000 * fontSize;
				if (lineWidth + wordWidth < maxWidth) {
					newLine.append(word).append(" ");
					lineWidth += wordWidth + font.getStringWidth(" ") / 1000 * fontSize;
				} else {
					wrappedLines.add(newLine.toString().trim());
					newLine = new StringBuilder(word).append(" ");
					lineWidth = wordWidth;
				}
			}
			wrappedLines.add(newLine.toString().trim());
		}

		return wrappedLines;
	}

	@Override
	@Transactional
	public ResponseDTO scheduleInterview(InterviewDetailsDTO interviewDetailsDTO, MultipartFile JDFile,
			MultipartFile CVFile) {
		logger.info("Updating interview details for candidate: {}", interviewDetailsDTO.getCandidateName());

		// Create a subfolder with today's date (e.g., 2025-02-04)
		String dailyFolder = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		String storageDir = emailFilesPath + File.separator + dailyFolder;

		File directory = new File(storageDir);
		if (!directory.exists() && !directory.mkdirs()) {
			logger.error("Failed to create daily folder for storing files.");
			return ResponseDTO.error("Unable to create daily storage directory.");
		}

		try {
			// Save JDFile
			String jdFilePath = saveFile(JDFile, storageDir, "JD_" + interviewDetailsDTO.getCandidateName());

			// Save CVFile
			String cvFilePath = saveFile(CVFile, storageDir, "CV_" + interviewDetailsDTO.getCandidateName());

			logger.info("Files saved successfully: JDFile={}, CVFile={}", jdFilePath, cvFilePath);

			try (PreparedStatement preparedStatement = connection
					.prepareStatement(SQLConstants.PROCEDURE_SCHEDULE_INTERVIEW)) {

				preparedStatement.setLong(1, interviewDetailsDTO.getInterviewId());
				preparedStatement.setLong(2, interviewDetailsDTO.getJobId());
				preparedStatement.setString(3, interviewDetailsDTO.getJobTitle());
				preparedStatement.setTimestamp(4, Timestamp.valueOf(interviewDetailsDTO.getInterviewDateTime()));
				preparedStatement.setString(5, interviewDetailsDTO.getPlatform());
				preparedStatement.setString(6, interviewDetailsDTO.getJobDescription());
				preparedStatement.setString(7, interviewDetailsDTO.getCandidateName());
				preparedStatement.setString(8, interviewDetailsDTO.getCandidateEmail());

				ObjectMapper objectMapper = new ObjectMapper();
				JsonNode panelJsonNode = objectMapper.valueToTree(interviewDetailsDTO.getPanel());
				preparedStatement.setObject(9, panelJsonNode, Types.OTHER);

			} catch (SQLException e) {
				e.printStackTrace();
				return ResponseDTO.error(e.getMessage());
			}

			MeetingInviteSender inviteSender = MeetingInviteFactory
					.getInterviewScheduler(interviewDetailsDTO.getPlatform());

			EmailRequestDTO emailRequestDTO = inviteSender.scheduleInterview(interviewDetailsDTO);

			String[] filePaths = new String[] { jdFilePath, cvFilePath };

			emailRequestDTO.getData().put("filePaths", String.join(",", filePaths));

			emailRequestDTO.setTemplateName(AppConstants.TEMPLATE_SCHEDULE_INTERVIEW);

			emailServiceClient.sendEmailWithAttachment(emailRequestDTO);

			return ResponseDTO.success(MessageConstants.INTERVIEW_SCHEDULED_SUCCESSFULLY);

		} catch (IOException e) {
			logger.error("File saving failed: {}", e.getMessage());
			return ResponseDTO.error("File saving error: " + e.getMessage());
		}
	}

	/**
	 * Saves the uploaded file to the specified daily directory.
	 * 
	 * @param file      Multipart file to be saved
	 * @param directory Directory where the file will be stored
	 * @param filename  Custom filename prefix
	 * @return Absolute path of the saved file
	 * @throws IOException If file saving fails
	 */
	private String saveFile(MultipartFile file, String directory, String filename) throws IOException {
		if (file == null || file.isEmpty()) {
			throw new IOException("File is empty or null: " + filename);
		}
		String fileExtension = Objects.requireNonNull(file.getOriginalFilename())
				.substring(file.getOriginalFilename().lastIndexOf("."));
		String newFileName = filename + "_" + System.currentTimeMillis() + fileExtension;
		Path filePath = Paths.get(directory, newFileName);
		Files.write(filePath, file.getBytes());
		return filePath.toAbsolutePath().toString();
	}

}
