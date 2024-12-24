package com.carefirst.nexus.smmd.enrollment.helper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.carefirst.nexus.enrollments.gen.model.Address;
import com.carefirst.nexus.enrollments.gen.model.Application;
import com.carefirst.nexus.enrollments.gen.model.Choice;
import com.carefirst.nexus.enrollments.gen.model.MaritalStatus;
import com.carefirst.nexus.enrollments.gen.model.Medicare;
import com.carefirst.nexus.enrollments.gen.model.Member;
import com.carefirst.nexus.enrollments.gen.model.OtherInfo;
import com.carefirst.nexus.enrollments.gen.model.PCP;
import com.carefirst.nexus.enrollments.gen.model.Phone;
import com.carefirst.nexus.enrollments.gen.model.PhoneType;
import com.carefirst.nexus.enrollments.gen.model.ProductCategory;
import com.carefirst.nexus.enrollments.gen.model.ProductCoverage;
import com.carefirst.nexus.enrollments.gen.model.RelationshipCode;
import com.carefirst.nexus.smmd.enrollment.constants.EdifecsFileConstants;
import com.carefirst.nexus.smmd.enrollment.constants.EnrollmentConstants;
import com.carefirst.nexus.smmd.enrollment.edifecsfilemodel.AccountHeader;
import com.carefirst.nexus.smmd.enrollment.edifecsfilemodel.AccountTrailer;
import com.carefirst.nexus.smmd.enrollment.edifecsfilemodel.DisabilityDataLg;
import com.carefirst.nexus.smmd.enrollment.edifecsfilemodel.EmployeeSpecificDataLg;
import com.carefirst.nexus.smmd.enrollment.edifecsfilemodel.EmployeeTransactionData;
import com.carefirst.nexus.smmd.enrollment.edifecsfilemodel.FileHeader;
import com.carefirst.nexus.smmd.enrollment.edifecsfilemodel.FileTrailer;
import com.carefirst.nexus.smmd.enrollment.edifecsfilemodel.ProcessorPayload1;
import com.carefirst.nexus.smmd.enrollment.edifecsfilemodel.TefraInformationLg;

@Service
public class Custom1350LayoutGenerator {

	private EnrollmentUtil enrollmentUtil;

	public Custom1350LayoutGenerator(EnrollmentUtil enrollmentUtil) {
		this.enrollmentUtil = enrollmentUtil;
	}

	static DateTimeFormatter srcDateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	static DateTimeFormatter localDateFormat = DateTimeFormatter.ofPattern("uuuu-MM-dd");
	static DateTimeFormatter regMemberDateFormat = DateTimeFormatter.ofPattern("MMddyyyy");

	public String formatEmplyeeTransactionData(ProcessorPayload1 processorPayload) {
		StringBuilder customerMAFile = new StringBuilder();
		customerMAFile.append(getFileHeaderData(processorPayload.getFileHeader()));
		customerMAFile.append(EdifecsFileConstants.NEWLINE);
		customerMAFile.append(getAccountHeaderData(processorPayload.getAccountHeader()));
		customerMAFile.append(EdifecsFileConstants.NEWLINE);
		customerMAFile.append(getDetailRecord(processorPayload.getEmployeeTransactionDatas()));
		customerMAFile.append(getAccountTrailer(processorPayload.getAccountTrailer()));
		customerMAFile.append(EdifecsFileConstants.NEWLINE);
		customerMAFile.append(getFileTrailer(processorPayload.getFileTrailer()));
		return customerMAFile.toString();
	}

	private StringBuilder getFileHeaderData(FileHeader fileHeader) {
		StringBuilder fileHeaderBuffer = new StringBuilder();
		if (fileHeader.getRecordType() != null && !fileHeader.getRecordType().isEmpty()) {
			fileHeaderBuffer.append(
					EmployeesFileGenerator.padRight(fileHeader.getRecordType(), EdifecsFileConstants.RECORDTYPE_SIZE));
		} else {
			fileHeaderBuffer.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.RECORDTYPE_SIZE));
		}
		fileHeaderBuffer.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
				EdifecsFileConstants.RELEASE_VERSION_SIZE));
		if (fileHeader.getCreatedDate() != null && !fileHeader.getCreatedDate().isEmpty()) {
			fileHeaderBuffer.append(
					EmployeesFileGenerator.padRight(fileHeader.getCreatedDate(), EdifecsFileConstants.CREATEDATE_SIZE));
		} else {
			fileHeaderBuffer.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.CREATEDATE_SIZE));
		}
		if (fileHeader.getSourceId() != null && !fileHeader.getSourceId().isEmpty()) {
			fileHeaderBuffer.append(
					EmployeesFileGenerator.padRight(fileHeader.getSourceId(), EdifecsFileConstants.SOURCEID_SIZE));
		} else {
			fileHeaderBuffer.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.SOURCEID_SIZE));
		}
		fileHeaderBuffer.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
				EdifecsFileConstants.BATCHNUMBER_SIZE));
		if (fileHeader.getSubmitterRole() != null && !fileHeader.getSubmitterRole().isEmpty()) {
			fileHeaderBuffer.append(EmployeesFileGenerator.padRight(fileHeader.getSubmitterRole(),
					EdifecsFileConstants.SUBMITTERROLE_SIZE));
		} else {
			fileHeaderBuffer.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.SUBMITTERROLE_SIZE));
		}
		fileHeaderBuffer.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
				EdifecsFileConstants.FILEHEADER_FILLER_SIZE));
		fileHeaderBuffer.append(
				EmployeesFileGenerator.padRight(EdifecsFileConstants.HOLDF_FIELD, EdifecsFileConstants.HOLDFIELD_SIZE));
		return fileHeaderBuffer;
	}

	private StringBuilder getFileTrailer(FileTrailer fileTrailer) {
		StringBuilder fileHeaderBuffer = new StringBuilder();
		if (fileTrailer.getRecordType() != null && !fileTrailer.getRecordType().isEmpty()) {
			fileHeaderBuffer.append(
					EmployeesFileGenerator.padRight(fileTrailer.getRecordType(), EdifecsFileConstants.RECORDTYPE_SIZE));
		} else {
			fileHeaderBuffer.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.RECORDTYPE_SIZE));
		}
		if (fileTrailer.getTotalAccountorGroupCount() != null && !fileTrailer.getTotalAccountorGroupCount().isEmpty()) {
			fileHeaderBuffer.append(EmployeesFileGenerator.padRight(fileTrailer.getTotalAccountorGroupCount(),
					EdifecsFileConstants.TOTAL_ACCOUNT_OR_GROUP_COUNT));
		} else {
			fileHeaderBuffer.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.TOTAL_ACCOUNT_OR_GROUP_COUNT));
		}
		if (fileTrailer.getTotalrecordCount() != null && !fileTrailer.getTotalrecordCount().isEmpty()) {
			fileHeaderBuffer.append(EmployeesFileGenerator.padRight(fileTrailer.getTotalrecordCount(),
					EdifecsFileConstants.TOTAL_COUNT));
		} else {
			fileHeaderBuffer.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.TOTAL_COUNT));
		}
		fileHeaderBuffer.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.FILLER_WITH_EMPTY_VALUE,
				EdifecsFileConstants.FILE_TRAILER_FILLER));
		fileHeaderBuffer.append(
				EmployeesFileGenerator.padRight(EdifecsFileConstants.HOLDF_FIELD, EdifecsFileConstants.HOLDFIELD_SIZE));
		return fileHeaderBuffer;
	}

	private Object getAccountTrailer(AccountTrailer accountTrailer) {
		StringBuilder accounTrailer = new StringBuilder();
		if (accountTrailer != null) {
			if (accountTrailer.getRecordType() != null && !accountTrailer.getRecordType().isEmpty()) {
				accounTrailer.append(EmployeesFileGenerator.padRight(accountTrailer.getRecordType(),
						EdifecsFileConstants.RECORDTYPE_SIZE));
			} else {
				accounTrailer.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
						EdifecsFileConstants.RECORDTYPE_SIZE));
			}
			if (accountTrailer.getFileType() != null && !accountTrailer.getFileType().isEmpty()) {
				accounTrailer.append(EmployeesFileGenerator.padRight(accountTrailer.getFileType(),
						EdifecsFileConstants.ACCOUNT_TRAILER_FILE_TYPE_SIZE));
			} else {
				accounTrailer.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
						EdifecsFileConstants.ACCOUNT_TRAILER_FILE_TYPE_SIZE));
			}
			if (accountTrailer.getDetailEmployeeRecordCount() != null
					&& !accountTrailer.getDetailEmployeeRecordCount().isEmpty()) {
				accounTrailer.append(EmployeesFileGenerator.padRight(accountTrailer.getDetailEmployeeRecordCount(),
						EdifecsFileConstants.TOTAL_NUMBER_OF_RECORDS));
			} else {
				accounTrailer.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
						EdifecsFileConstants.TOTAL_NUMBER_OF_RECORDS));
			}
			accounTrailer.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.FILLER_WITH_EMPTY_VALUE,
					EdifecsFileConstants.ACCOUNT_TRAILER_FILLER));
			accounTrailer.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.HOLDF_FIELD,
					EdifecsFileConstants.HOLDFIELD_SIZE));
		}
		return accounTrailer;
	}

	private StringBuilder getDetailRecord(List<EmployeeTransactionData> employeeTransactionDatas) {
		StringBuilder detailRecord = new StringBuilder();
		if (CollectionUtils.isNotEmpty(employeeTransactionDatas)) {
			for (EmployeeTransactionData employeeTransactionData : employeeTransactionDatas) {
				List<Member> members = employeeTransactionData
						.getMembers();
				memberIterator(detailRecord, employeeTransactionData, members);
			}
		}
		return detailRecord;
	}

	private void memberIterator(StringBuilder detailRecord, EmployeeTransactionData employeeTransactionDataLg,
			List<Member> members) {
		for (Member member : members) {
			String keyDemoGraMberEntityId = member.getMemberEntityId();
			StringBuilder keyDemographicDataStr = new StringBuilder();
			detailRecord.append(EmployeesFileGenerator.padRight(employeeTransactionDataLg.getRecordType(),
					EdifecsFileConstants.RECORDTYPE_SIZE));
			detailRecord.append(EmployeesFileGenerator.padRight(employeeTransactionDataLg.getTransactionCode(),
					EdifecsFileConstants.TRANSACTIONCODE_SIZE));
			detailRecord.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.FILLER_WITH_EMPTY_VALUE,
					EdifecsFileConstants.DETAILRECORDFILLER1_SIZE));
			memberSsnAndLnameAndFnameAndMiAndSuffix(member, keyDemographicDataStr);
			keyDemographicDataStr
					.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.FILLER_WITH_EMPTY_VALUE,
							EdifecsFileConstants.DEMOGRAPHIC_DATA_FILLER_SIZE));
			detailRecord.append(keyDemographicDataStr);
			detailRecord.append(getEmployeeSpecificData(employeeTransactionDataLg.getEmployeeSpecificDatalg(),
					member.getMaritalStatus()));
			detailRecord.append(getAdditionalMemberInformation(member));
			detailRecord.append(getProductChoices(member, employeeTransactionDataLg.getApplication()));
			detailRecord.append(
					getOtherInsuranceData(member, keyDemoGraMberEntityId));
			detailRecord.append(EdifecsFileConstants.NEWLINE);
		}
	}

	private void memberSsnAndLnameAndFnameAndMiAndSuffix(Member member, StringBuilder keyDemographicDataStr) {
		if (member.getSocialSecurityNumber() != null && !member.getSocialSecurityNumber().isEmpty()) {
			keyDemographicDataStr.append(
					EmployeesFileGenerator.padRight(member.getSocialSecurityNumber(),
							EdifecsFileConstants.EMPLOYEESSN_SIZE));
		} else {
			keyDemographicDataStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.EMPLOYEESSN_SIZE));
		}
		if (null != member.getName() && member.getName().getLastName() != null
				&& !member.getName().getLastName().isEmpty()) {
			keyDemographicDataStr.append(
					EmployeesFileGenerator.padRight(member.getName().getLastName(),
							EdifecsFileConstants.LASTNAME_SIZE));
		} else {
			keyDemographicDataStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.LASTNAME_SIZE));
		}
		if (null != member.getName() && member.getName().getFirstName() != null
				&& !member.getName().getFirstName().isEmpty()) {
			keyDemographicDataStr.append(
					EmployeesFileGenerator.padRight(member.getName().getFirstName(),
							EdifecsFileConstants.FIRSTNAME_SIZE));
		} else {
			keyDemographicDataStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.FIRSTNAME_SIZE));
		}
		if (null != member.getName() && member.getName().getMiddleName() != null
				&& !member.getName().getMiddleName().isEmpty()) {
			keyDemographicDataStr.append(EmployeesFileGenerator.padRight(member.getName().getMiddleName(),
					EdifecsFileConstants.MIDDLEINTIAL_SIZE));
		} else {
			keyDemographicDataStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.MIDDLEINTIAL_SIZE));
		}
		if (null != member.getName() && member.getName().getSuffix() != null
				&& !member.getName().getSuffix().isEmpty()) {
			keyDemographicDataStr.append(
					EmployeesFileGenerator.padRight(member.getName().getSuffix(),
							EdifecsFileConstants.SUFFIX_SIZE));
		} else {
			keyDemographicDataStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.SUFFIX_SIZE));
		}
		memberDobAndSeAndRc(member, keyDemographicDataStr);
	}

	private void memberDobAndSeAndRc(Member member, StringBuilder keyDemographicDataStr) {
		if (member.getDateOfBirth() != null && !member.getDateOfBirth().isEmpty()) {
			keyDemographicDataStr.append(
					EmployeesFileGenerator.padRight(LocalDate.parse(member.getDateOfBirth(), localDateFormat)
							.format(regMemberDateFormat),
							EdifecsFileConstants.DATEOFBIRTH_SIZE));
		} else {
			keyDemographicDataStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.DATEOFBIRTH_SIZE));
		}
		if (member.getGender() != null) {
			keyDemographicDataStr
					.append(EmployeesFileGenerator.padRight(member.getGender().getValue(),
							EdifecsFileConstants.SEX_SIZE));
		} else {
			keyDemographicDataStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.SEX_SIZE));
		}
		setRelationShipCode(member, keyDemographicDataStr);
	}

	private void setRelationShipCode(Member member, StringBuilder keyDemographicDataStr) {
		if (member.getRelationshipCode() != null) {
			if (member.getRelationshipCode() != null) {
				if (RelationshipCode.SELF.equals(member.getRelationshipCode())) {
					keyDemographicDataStr
							.append(EmployeesFileGenerator.padRight(EnrollmentConstants.SUB_RELATIONSHOP_CODE,
									EdifecsFileConstants.RELATIONSHIPCODE_SIZE));
				} else if (RelationshipCode.SPOUSE.equals(member.getRelationshipCode())) {
					keyDemographicDataStr
							.append(EmployeesFileGenerator.padRight(EnrollmentConstants.SPOUSE_RELATIONSHOP_CODE,
									EdifecsFileConstants.RELATIONSHIPCODE_SIZE));
				} else if (RelationshipCode.CHILD.equals(member.getRelationshipCode())) {
					keyDemographicDataStr
							.append(EmployeesFileGenerator.padRight(EnrollmentConstants.CHILD_RELATIONSHOP_CODE,
									EdifecsFileConstants.RELATIONSHIPCODE_SIZE));
				} else if (RelationshipCode.LIFE_PARTNER.equals(member.getRelationshipCode())) {
					keyDemographicDataStr.append(
							EmployeesFileGenerator.padRight(EnrollmentConstants.DEMESTICPARTENER_RELATIONSHOP_CODE,
									EdifecsFileConstants.RELATIONSHIPCODE_SIZE));
				} else {
					keyDemographicDataStr
							.append(EmployeesFileGenerator.padRight(EnrollmentConstants.OTHER_RELATIONSHOP_CODE,
									EdifecsFileConstants.RELATIONSHIPCODE_SIZE));
				}
			}
		} else {
			keyDemographicDataStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.RELATIONSHIPCODE_SIZE));
		}
	}

	private String getOtherInsuranceData(Member member, String keyDemoGrapicMberEntityId) {
		StringBuilder otherInsuranceDataStr = new StringBuilder();
		if (CollectionUtils.isNotEmpty(member.getCobs())
				&& !member.getCobs().get(0).getOtherInsuranceIndicator().isEmpty() &&
				Choice.Yes.getValue().equals(member.getCobs().get(0).getOtherInsuranceIndicator())) {
			otherInsuranceDataStr
					.append(EmployeesFileGenerator.padRight(member.getCobs().get(0).getOtherInsuranceIndicator(),
							EdifecsFileConstants.COBINDICATOR_SIZE));
		} else {
			otherInsuranceDataStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.COBINDICATOR_SIZE));
		}
		otherInsuranceDataStr
				.append(getOtherInsuranceinfomationData(member));
		TefraInformationLg tefraInformationLg = new TefraInformationLg();
		otherInsuranceDataStr.append(getTefraInformationData(tefraInformationLg));
		otherInsuranceDataStr.append(
				getMedicareInsuranceInfo(member, keyDemoGrapicMberEntityId));
		otherInsuranceDataStr.append(getDisabilityData(new DisabilityDataLg()));
		otherInsuranceDataStr.append(getStudentData(member));
		return otherInsuranceDataStr.toString();
	}

	private String getTefraInformationData(TefraInformationLg tefraInformationLg) {
		StringBuilder terFrainfoDataStr = new StringBuilder();
		if (tefraInformationLg.getTefraEffectiveDate() != null
				&& !tefraInformationLg.getTefraEffectiveDate().isEmpty()) {
			terFrainfoDataStr.append(EmployeesFileGenerator.padRight(tefraInformationLg.getTefraEffectiveDate(),
					EdifecsFileConstants.TEFRA_EFFECTIVEDATE_SIZE));
		} else {
			terFrainfoDataStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.TEFRA_EFFECTIVEDATE_SIZE));
		}
		if (tefraInformationLg.getTefraTerminationDate() != null
				&& !tefraInformationLg.getTefraTerminationDate().isEmpty()) {
			terFrainfoDataStr.append(EmployeesFileGenerator.padRight(tefraInformationLg.getTefraTerminationDate(),
					EdifecsFileConstants.TEFRA_TERMINATIONDATE_SIZE));
		} else {
			terFrainfoDataStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.TEFRA_TERMINATIONDATE_SIZE));
		}
		return terFrainfoDataStr.toString();
	}

	private String getStudentData(Member member) {
		StringBuilder studentDataStr = new StringBuilder();
		studentDataStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
				EdifecsFileConstants.STUDENT_DATA_SIZE));
		studentDataStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
				EdifecsFileConstants.STUDENT_EFFECTIVEDATE_SIZE));
		studentDataStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
				EdifecsFileConstants.STUDENT_TERMINATIONDATE_SIZE));
		if (null != member.getContact() && null != member.getContact().getEmail()
				&& null != member.getContact().getEmail().get(0) &&
				member.getContact().getEmail().get(0).getEmailAddress() != null
				&& !member.getContact().getEmail().get(0).getEmailAddress().isEmpty()) {
			studentDataStr.append(
					EmployeesFileGenerator.padRight(member.getContact().getEmail().get(0).getEmailAddress(),
							EdifecsFileConstants.EMAIL_SIZE));
		} else {
			studentDataStr.append(
					EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE, EdifecsFileConstants.EMAIL_SIZE));
		}
		studentDataStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.DEPARTMENTID,
				EdifecsFileConstants.DEPARTMENTID_SIZE));
		studentDataStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.CLIENT_SPECFIC_REPORTING_VALUE,
				EdifecsFileConstants.CLIENT_SPECIFIC_REPORTING_VALUE_SIZE));
		studentDataStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.FILLER_WITH_EMPTY_VALUE,
				EdifecsFileConstants.STUDENT_FILLER_SIZE));
		studentDataStr.append(
				EmployeesFileGenerator.padRight(EdifecsFileConstants.HOLDF_FIELD, EdifecsFileConstants.HOLDFIELD_SIZE));
		return studentDataStr.toString();
	}

	private String getDisabilityData(DisabilityDataLg disabilityData) {
		StringBuilder disabilityDataStr = new StringBuilder();
		if (disabilityData.getDisabilityIndicator() != null && !disabilityData.getDisabilityIndicator().isEmpty()) {
			disabilityDataStr.append(EmployeesFileGenerator.padRight(disabilityData.getDisabilityIndicator(),
					EdifecsFileConstants.DISABILITY_INDICATOR_SIZE));
		} else {
			disabilityDataStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.DISABILITY_INDICATOR_SIZE));
		}
		if (disabilityData.getDisabilityEffectiveDate() != null
				&& !disabilityData.getDisabilityEffectiveDate().isEmpty()) {
			disabilityDataStr.append(EmployeesFileGenerator.padRight(disabilityData.getDisabilityEffectiveDate(),
					EdifecsFileConstants.DISABILITY_EFFECTIVEDATE_SIZE));
		} else {
			disabilityDataStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.DISABILITY_EFFECTIVEDATE_SIZE));
		}
		return disabilityDataStr.toString();
	}

	private String getMedicareInsuranceInfo(Member member,
			String keyDemoMbrEntityId) {
		StringBuilder medicareInsuranceInfostr = new StringBuilder();
		String medicareEligibilityIndicator = null;
		if (member != null && CollectionUtils.isNotEmpty(member.getMedicare())) {
			for (Medicare medicareInsuranceInfo : member.getMedicare()) {
				if (StringUtils.hasText(medicareInsuranceInfo.getHasPartA())
						&& "Y".equals(medicareInsuranceInfo.getHasPartA())
						&& StringUtils.hasText(medicareInsuranceInfo.getHasPartB())
						&& "Y".equals(medicareInsuranceInfo.getHasPartB())) {
					medicareEligibilityIndicator = "3";
				} else if (StringUtils.hasText(medicareInsuranceInfo.getHasPartA())
						&& "Y".equals(medicareInsuranceInfo.getHasPartA())) {
					medicareEligibilityIndicator = "1";
				} else {
					medicareEligibilityIndicator = "2";
				}
				medicareAAndBDatesandHICNumAndEligibiltyIndDetails(medicareInsuranceInfostr, medicareInsuranceInfo,
						medicareEligibilityIndicator);
				mediInfo(medicareInsuranceInfo, medicareInsuranceInfostr, member);
				medicareInsuranceInfostr
						.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.FILLER_WITH_EMPTY_VALUE,
								EdifecsFileConstants.MEDICAREINSURANCEINFO_FILLER2_SIZE));
			}
		} else {
			medicareEligibilityIndicator = "9";
			Medicare medicareInsuranceInfo = new Medicare();
			medicareAAndBDatesandHICNumAndEligibiltyIndDetails(medicareInsuranceInfostr, medicareInsuranceInfo,
					medicareEligibilityIndicator);
			mediInfo(medicareInsuranceInfo, medicareInsuranceInfostr, member);
			medicareInsuranceInfostr
					.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.FILLER_WITH_EMPTY_VALUE,
							EdifecsFileConstants.MEDICAREINSURANCEINFO_FILLER2_SIZE));

		}
		return medicareInsuranceInfostr.toString();
	}

	private void medicareAAndBDatesandHICNumAndEligibiltyIndDetails(StringBuilder medicareInsuranceInfostr,
			Medicare medicareInsuranceInfo, String medicareEligibilityIndicator) {
		if (medicareEligibilityIndicator != null
				&& !medicareEligibilityIndicator.isEmpty()) {
			medicareInsuranceInfostr
					.append(EmployeesFileGenerator.padRight(medicareEligibilityIndicator,
							EdifecsFileConstants.MEDICAR_ELIGIBILITY_INDICATOR_SIZE));
		} else {
			medicareInsuranceInfostr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.MEDICAR_ELIGIBILITY_INDICATOR_SIZE));
		}
		if (medicareInsuranceInfo.getMedicareNumber() != null &&
				!medicareInsuranceInfo.getMedicareNumber().isEmpty()) {
			medicareInsuranceInfostr.append(EmployeesFileGenerator.padRight(medicareInsuranceInfo.getMedicareNumber(),
					EdifecsFileConstants.HIC_NUMBER_SIZE));
		} else {
			medicareInsuranceInfostr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.HIC_NUMBER_SIZE));
		}
		if (medicareInsuranceInfo.getPartAEntitlementDate() != null) {
			medicareInsuranceInfostr
					.append(EmployeesFileGenerator.padRight(
							formatDateValue(medicareInsuranceInfo.getPartAEntitlementDate()),
							EdifecsFileConstants.MEDICARA_EFFECTIVEDATE_SIZE));
		} else {
			medicareInsuranceInfostr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.MEDICARA_EFFECTIVEDATE_SIZE));
		}
		if (StringUtils.hasText(medicareInsuranceInfo.getMedicareEfftiveEndDate())) {
			medicareInsuranceInfostr
					.append(EmployeesFileGenerator.padRight(
							formatDateString(medicareInsuranceInfo.getMedicareEfftiveEndDate()),
							EdifecsFileConstants.MEDICARA_TERMINATIONDATE_SIZE));
		} else {
			medicareInsuranceInfostr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.MEDICARA_TERMINATIONDATE_SIZE));
		}
	}

	private void mediInfo(Medicare medicareInsuranceInfo, StringBuilder medicareInsuranceInfostr, Member member) {
		if (medicareInsuranceInfo.getPartAEntitlementDate() != null) {
			medicareInsuranceInfostr
					.append(EmployeesFileGenerator.padRight(
							formatDateValue(medicareInsuranceInfo.getPartAEntitlementDate()),
							EdifecsFileConstants.MEDICARB_EFFECTIVEDATE_SIZE));
		} else {
			medicareInsuranceInfostr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.MEDICARB_EFFECTIVEDATE_SIZE));
		}
		if (medicareInsuranceInfo.getMedicareEfftiveEndDate() != null
				&& !medicareInsuranceInfo.getMedicareEfftiveEndDate().isEmpty()) {
			medicareInsuranceInfostr
					.append(EmployeesFileGenerator.padRight(medicareInsuranceInfo.getMedicareEfftiveEndDate(),
							EdifecsFileConstants.MEDICARB_TERMINATIONDATE_SIZE));
		} else {
			medicareInsuranceInfostr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.MEDICARB_TERMINATIONDATE_SIZE));
		}
		medicareInsuranceInfostr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.FILLER_WITH_EMPTY_VALUE,
				EdifecsFileConstants.MEDICAREINSURANCEINFO_FILLER1_SIZE));
		if (null != medicareInsuranceInfo.getMedicareNumber() && member.getSocialSecurityNumber() != null
				&& !member.getSocialSecurityNumber().isEmpty()) {
			medicareInsuranceInfostr.append(EmployeesFileGenerator.padRight(
					member.getSocialSecurityNumber(), EdifecsFileConstants.CRASS_REFERENCE_SSN_SIZE));
		} else {
			medicareInsuranceInfostr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.CRASS_REFERENCE_SSN_SIZE));
		}
		// if (medicareInsuranceInfo.getRateModifier() != null &&
		// !medicareInsuranceInfo.getRateModifier().isEmpty()) {
		// medicareInsuranceInfostr.append(EmployeesFileGenerator.padRight(medicareInsuranceInfo.getRateModifier(),
		// EdifecsFileConstants.RATE_MODIFIER_SIZE));
		// } else {
		medicareInsuranceInfostr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
				EdifecsFileConstants.RATE_MODIFIER_SIZE));
		// }
	}

	private String getOtherInsuranceinfomationData(Member member) {
		StringBuilder otherInsuranceInfomationStr = new StringBuilder();
		OtherInfo otherInsuranceInfomation = null;
		if (CollectionUtils.isNotEmpty(member.getCobs())) {
			otherInsuranceInfomation = member.getCobs().get(0).getOtherInfo().get(0);
		}
		if (null != otherInsuranceInfomation && otherInsuranceInfomation.getInsuranceCompanyName() != null
				&& !otherInsuranceInfomation.getInsuranceCompanyName().isEmpty()) {
			otherInsuranceInfomationStr
					.append(EmployeesFileGenerator.padRight(otherInsuranceInfomation.getInsuranceCompanyName(),
							EdifecsFileConstants.COB_INSURANCE_COMPANY_NAME_SIZE));
		} else {
			otherInsuranceInfomationStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.COB_INSURANCE_COMPANY_NAME_SIZE));
		}
		if (null != otherInsuranceInfomation
				&& CollectionUtils.isNotEmpty(otherInsuranceInfomation.getMemberIdentifier())
				&& !otherInsuranceInfomation.getMemberIdentifier().get(0).getMemberEntityId().isEmpty()) {
			otherInsuranceInfomationStr.append(EmployeesFileGenerator.padRight(
					otherInsuranceInfomation.getMemberIdentifier().get(0).getMemberEntityId(),
					EdifecsFileConstants.MEMBERS_COB_MEMBERID_SIZE));
		} else {
			otherInsuranceInfomationStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.MEMBERS_COB_MEMBERID_SIZE));
		}
		if (null != otherInsuranceInfomation && otherInsuranceInfomation.getEffectiveDate() != null
				&& !otherInsuranceInfomation.getEffectiveDate().isEmpty()) {
			otherInsuranceInfomationStr.append(EmployeesFileGenerator.padRight(
					formatDateString(otherInsuranceInfomation.getEffectiveDate()),
					EdifecsFileConstants.COB_EFFECTIVEDATE_SIZE));
		} else {
			otherInsuranceInfomationStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.COB_EFFECTIVEDATE_SIZE));
		}
		if (null != otherInsuranceInfomation && otherInsuranceInfomation.getTerminationDate() != null
				&& !otherInsuranceInfomation.getTerminationDate().isEmpty()) {
			otherInsuranceInfomationStr.append(EmployeesFileGenerator.padRight(
					formatDateString(otherInsuranceInfomation.getTerminationDate()),
					EdifecsFileConstants.COB_TERMINATIONDATE_SIZE));
		} else {
			otherInsuranceInfomationStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.COB_TERMINATIONDATE_SIZE));
		}
		if (null != otherInsuranceInfomation && StringUtils.hasText(otherInsuranceInfomation.getEmployerName())) {
			otherInsuranceInfomationStr.append(EmployeesFileGenerator.padRight(
					otherInsuranceInfomation.getEmployerName(), EdifecsFileConstants.COB_EMPLOYERNAME_SIZE));
		} else {
			otherInsuranceInfomationStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.COB_EMPLOYERNAME_SIZE));
		}
		otherInsurancInfo(otherInsuranceInfomation, otherInsuranceInfomationStr, member);
		otherInsuranceInfomationStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.FILLER_WITH_EMPTY_VALUE,
				EdifecsFileConstants.OTHERINSURANCEDATA_FILLER_SIZE));
		return otherInsuranceInfomationStr.toString();
	}

	private String formatDateString(String dateValue) {
		try {
			return LocalDate.parse(dateValue, srcDateFormat)
					.format(regMemberDateFormat);
		} catch (Exception e) {
			return dateValue;
		}
	}

	private String formatDateValue(LocalDate date) {
		try {
			return date
					.format(regMemberDateFormat);
		} catch (Exception e) {
			return null;
		}

	}

	private void otherInsurancInfo(OtherInfo otherInsuranceInfomation,
			StringBuilder otherInsuranceInfomationStr, Member member) {
		if (null != otherInsuranceInfomation && otherInsuranceInfomation.getPolicyholderNumber() != null
				&& !otherInsuranceInfomation.getPolicyholderNumber().isEmpty()) {
			otherInsuranceInfomationStr.append(EmployeesFileGenerator.padRight(
					otherInsuranceInfomation.getPolicyholderNumber(), EdifecsFileConstants.COB_POLICYNUMBER_SIZE));
		} else {
			otherInsuranceInfomationStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.COB_POLICYNUMBER_SIZE));
		}
		if (null != otherInsuranceInfomation && otherInsuranceInfomation.getPolicyholderName() != null) {
			StringBuilder fullName = new StringBuilder();
			getPolicyHolderName(otherInsuranceInfomation, fullName);
			otherInsuranceInfomationStr.append(EmployeesFileGenerator.padRight(
					fullName.toString(),
					EdifecsFileConstants.COB_POLICYHOLDERNAME_SIZE));
		} else {
			otherInsuranceInfomationStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.COB_POLICYHOLDERNAME_SIZE));
		}
		if (member.getDateOfBirth() != null
				&& !member.getDateOfBirth().isEmpty()) {
			otherInsuranceInfomationStr.append(EmployeesFileGenerator.padRight(
					LocalDate.parse(member.getDateOfBirth(), localDateFormat)
							.format(regMemberDateFormat),
					EdifecsFileConstants.COB_POLICYHOLDERDOB_SIZE));
		} else {
			otherInsuranceInfomationStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.COB_POLICYHOLDERDOB_SIZE));
		}
		if (null != otherInsuranceInfomation && otherInsuranceInfomation.getPolicyholderId() != null
				&& !otherInsuranceInfomation.getPolicyholderId().isEmpty()) {
			otherInsuranceInfomationStr.append(EmployeesFileGenerator.padRight(
					otherInsuranceInfomation.getPolicyholderId(),
					EdifecsFileConstants.COB_POLICYHOLDERID_SIZE));
		} else {
			otherInsuranceInfomationStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.COB_POLICYHOLDERID_SIZE));
		}
		if (member.getSocialSecurityNumber() != null
				&& !member.getSocialSecurityNumber().isEmpty()) {
			otherInsuranceInfomationStr.append(EmployeesFileGenerator.padRight(
					member.getSocialSecurityNumber(), EdifecsFileConstants.COB_POLICYHOLDERSSN_SIZE));
		} else {
			otherInsuranceInfomationStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.COB_POLICYHOLDERSSN_SIZE));
		}
	}

	private void getPolicyHolderName(OtherInfo otherInsuranceInfomation, StringBuilder fullName) {
		if (StringUtils.hasText(otherInsuranceInfomation.getPolicyholderName().getFirstName())) {
			fullName.append(otherInsuranceInfomation.getPolicyholderName().getFirstName());
		}
		if (StringUtils.hasText(otherInsuranceInfomation.getPolicyholderName().getMiddleName())) {
			fullName.append(EdifecsFileConstants.SPACE_CHAR);
			fullName.append(otherInsuranceInfomation.getPolicyholderName().getMiddleName());
		}
		if (StringUtils.hasText(otherInsuranceInfomation.getPolicyholderName().getLastName())) {
			fullName.append(EdifecsFileConstants.SPACE_CHAR);
			fullName.append(otherInsuranceInfomation.getPolicyholderName().getLastName());
		}
	}

	private String getProductChoices(Member member, Application application) {
		StringBuilder productChoicesStr = new StringBuilder();
		Map<ProductCategory, List<ProductCoverage>> mapProductCoverages = member.getProductCoverages().stream()
				.collect(Collectors.groupingBy(ProductCoverage::getProductCategory));
		productChoicesStr.append(getMedicalCoverageDataLg(mapProductCoverages, member, application));
		productChoicesStr.append(getPcpSelectionData(mapProductCoverages, member, application));
		productChoicesStr.append(getDentalProductData(mapProductCoverages, member, application));
		productChoicesStr.append(getVisionProductData(mapProductCoverages, member, application));
		productChoicesStr.append(getDrugProductDataLg(mapProductCoverages, member, application));
		return productChoicesStr.toString();
	}

	private String getVisionProductData(Map<ProductCategory, List<ProductCoverage>> mapProductCoverages,
			Member member, Application application) {
		StringBuilder visionProductDataLgStr = new StringBuilder();
		ProductCoverage visionProductCoverage = null;
		List<ProductCoverage> visionProductCoverages = mapProductCoverages.get(ProductCategory.VISION);
		if (CollectionUtils.isNotEmpty(visionProductCoverages)) {
			visionProductCoverage = visionProductCoverages.get(0);
		}
		if (application != null && application.getGroup() != null
				&& !application.getGroup().getGroupId().isEmpty()) {
			visionProductDataLgStr.append(EmployeesFileGenerator.padRight(application.getGroup().getGroupId(),
					EdifecsFileConstants.VISION_GROUP_NUMBER_SIZE));
		} else {
			visionProductDataLgStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.VISION_GROUP_NUMBER_SIZE));
		}
		if (member != null && member.getSubGroupId() != null
				&& !member.getSubGroupId().isEmpty()) {
			visionProductDataLgStr
					.append(EmployeesFileGenerator.padRight(member.getSubGroupId(),
							EdifecsFileConstants.VISION_SUBGROUP_NUMBER_ORPACKAGE_CODE_SIZE));
		} else {
			visionProductDataLgStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.VISION_SUBGROUP_NUMBER_ORPACKAGE_CODE_SIZE));
		}
		if (visionProductCoverage != null && visionProductCoverage.getClassId() != null
				&& !visionProductCoverage.getClassId().isEmpty()) {
			visionProductDataLgStr.append(
					EmployeesFileGenerator.padRight(visionProductCoverage.getClassId(),
							EdifecsFileConstants.VISION_CONTRACT_CODE_OR_DEPARTMENTNUMBER_OR_CLASSID_SIZE));
		} else {
			visionProductDataLgStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.VISION_CONTRACT_CODE_OR_DEPARTMENTNUMBER_OR_CLASSID_SIZE));
		}
		visionProdData(visionProductCoverage, visionProductDataLgStr);
		visionProductDataLgStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.FILLER_WITH_EMPTY_VALUE,
				EdifecsFileConstants.VISION_FILLER_SIZE));
		return visionProductDataLgStr.toString();
	}

	private void visionProdData(ProductCoverage visionProductCoverage, StringBuilder visionProductDataLgStr) {
		if (visionProductCoverage != null
				&& enrollmentUtil.getCoverageLevelByCoverageType(visionProductCoverage.getCoverageLevel()) != null) {
			visionProductDataLgStr.append(EmployeesFileGenerator.padRight(
					enrollmentUtil.getCoverageLevelByCoverageType(visionProductCoverage.getCoverageLevel()),
					EdifecsFileConstants.VISION_COVERAGE_LEVEL_SIZE));
		} else {
			visionProductDataLgStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.VISION_COVERAGE_LEVEL_SIZE));
		}
		if (visionProductCoverage != null && visionProductCoverage.getEffectiveDate() != null) {
			visionProductDataLgStr
					.append(EmployeesFileGenerator.padRight(
							formatDateValue(visionProductCoverage.getEffectiveDate()),
							EdifecsFileConstants.VISION_COVERAGE_EFFECTIVEDATE_SIZE));
		} else {
			visionProductDataLgStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.VISION_COVERAGE_EFFECTIVEDATE_SIZE));
		}
		if (visionProductCoverage != null && visionProductCoverage.getTerminationDate() != null) {
			visionProductDataLgStr
					.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
							EdifecsFileConstants.VISION_COVERAGE_TERMINATION_CODE_SIZE));
		} else {
			visionProductDataLgStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.VISION_COVERAGE_TERMINATION_CODE_SIZE));
		}
		if (visionProductCoverage != null && visionProductCoverage.getTerminationDate() != null) {
			visionProductDataLgStr
					.append(EmployeesFileGenerator.padRight(
							formatDateValue(visionProductCoverage.getTerminationDate()),
							EdifecsFileConstants.VISION_COVERAGE_TERMINATIONDATE_SIZE));
		} else {
			visionProductDataLgStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.VISION_COVERAGE_TERMINATIONDATE_SIZE));
		}
		// need clarification
		if (visionProductCoverage != null && CollectionUtils.isNotEmpty(visionProductCoverage.getPcp())
				&& StringUtils.hasText(visionProductCoverage.getPcp().get(0).getProviderId())) {
			visionProductDataLgStr.append(EmployeesFileGenerator.padRight(EnrollmentConstants.PRODUCTID,
					EdifecsFileConstants.VISION_PRODUCTID_SIZE));
		} else {
			visionProductDataLgStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.VISION_PRODUCTID_SIZE));
		}
	}

	private String getDentalProductData(Map<ProductCategory, List<ProductCoverage>> mapProductCoverages,
			Member member, Application application) {
		ProductCoverage dentalProductCoverage = null;
		List<ProductCoverage> dentalProductCoverages = mapProductCoverages.get(ProductCategory.DENTAL);
		if (CollectionUtils.isNotEmpty(dentalProductCoverages)) {
			dentalProductCoverage = dentalProductCoverages.get(0);
		}
		StringBuilder dentalProductDataEgwpStr = new StringBuilder();
		if (application != null && application.getGroup() != null
				&& !application.getGroup().getGroupId().isEmpty()) {
			dentalProductDataEgwpStr.append(EmployeesFileGenerator.padRight(application.getGroup().getGroupId(),
					EdifecsFileConstants.DENTAL_GROUP_NUMBER_SIZE));
		} else {
			dentalProductDataEgwpStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.DENTAL_GROUP_NUMBER_SIZE));
		}
		if (member != null && member.getSubGroupId() != null
				&& !member.getSubGroupId().isEmpty()) {
			dentalProductDataEgwpStr
					.append(EmployeesFileGenerator.padRight(member.getSubGroupId(),
							EdifecsFileConstants.DENTAL_SUBGROUP_NUMBER_SIZE));
		} else {
			dentalProductDataEgwpStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.DENTAL_SUBGROUP_NUMBER_SIZE));
		}
		if (dentalProductCoverage != null && dentalProductCoverage.getClassId() != null
				&& !dentalProductCoverage.getClassId().isEmpty()) {
			dentalProductDataEgwpStr.append(
					EmployeesFileGenerator.padRight(dentalProductCoverage.getClassId(),
							EdifecsFileConstants.DENTAL_CONTRACT_CODE_OR_CLASSID_SIZE));
		} else {
			dentalProductDataEgwpStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.DENTAL_CONTRACT_CODE_OR_CLASSID_SIZE));
		}
		dentalProdData(dentalProductCoverage, dentalProductDataEgwpStr);
		dentalProductDataEgwpStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.FILLER_WITH_EMPTY_VALUE,
				EdifecsFileConstants.DENTAL_FILLER_SIZE));
		return dentalProductDataEgwpStr.toString();
	}

	private void dentalProdData(ProductCoverage dentalProductCoverage, StringBuilder dentalProductDataEgwpStr) {
		if (dentalProductCoverage != null
				&& enrollmentUtil.getCoverageLevelByCoverageType(dentalProductCoverage.getCoverageLevel()) != null) {
			dentalProductDataEgwpStr.append(EmployeesFileGenerator.padRight(
					enrollmentUtil.getCoverageLevelByCoverageType(dentalProductCoverage.getCoverageLevel()),
					EdifecsFileConstants.DENTAL_COVERAGE_LEVEL_SIZE));
		} else {
			dentalProductDataEgwpStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.DENTAL_COVERAGE_LEVEL_SIZE));
		}
		if (dentalProductCoverage != null && dentalProductCoverage.getEffectiveDate() != null) {
			dentalProductDataEgwpStr
					.append(EmployeesFileGenerator.padRight(
							formatDateValue(dentalProductCoverage.getEffectiveDate()),
							EdifecsFileConstants.DENTAL_COVERAGE_EFFECTIVEDATE_SIZE));
		} else {
			dentalProductDataEgwpStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.DENTAL_COVERAGE_EFFECTIVEDATE_SIZE));
		}
		if (dentalProductCoverage != null && dentalProductCoverage.getTerminationDate() != null) {
			dentalProductDataEgwpStr
					.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
							EdifecsFileConstants.DENTAL_COVERAGE_TERMINATION_CODE_SIZE));
		} else {
			dentalProductDataEgwpStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.DENTAL_COVERAGE_TERMINATION_CODE_SIZE));
		}
		dentalProd(dentalProductCoverage, dentalProductDataEgwpStr);
	}

	private void dentalProd(ProductCoverage dentalProductCoverage, StringBuilder dentalProductDataEgwpStr) {
		if (dentalProductCoverage != null && dentalProductCoverage.getTerminationDate() != null) {
			dentalProductDataEgwpStr
					.append(EmployeesFileGenerator.padRight(
							formatDateValue(dentalProductCoverage.getTerminationDate()),
							EdifecsFileConstants.DENTAL_COVERAGE_TERMINATIONDATE_SIZE));
		} else {
			dentalProductDataEgwpStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.DENTAL_COVERAGE_TERMINATIONDATE_SIZE));
		}
		if (dentalProductCoverage != null && CollectionUtils.isNotEmpty(dentalProductCoverage.getPcp())
				&& null != dentalProductCoverage.getPcp().get(0).getProviderName()) {
			StringBuilder fullName = new StringBuilder();
			getPcpProviderName(dentalProductCoverage, fullName);
			dentalProductDataEgwpStr.append(EmployeesFileGenerator.padRight(
					fullName.toString(),
					EdifecsFileConstants.DENTAL_PROVIDER_NAME_SIZE));
		} else {
			dentalProductDataEgwpStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.DENTAL_PROVIDER_NAME_SIZE));
		}
		if (dentalProductCoverage != null && CollectionUtils.isNotEmpty(dentalProductCoverage.getPcp())
				&& StringUtils.hasText(dentalProductCoverage.getPcp().get(0).getProviderId())) {
			dentalProductDataEgwpStr.append(EmployeesFileGenerator.padRight(
					dentalProductCoverage.getPcp().get(0).getProviderId(),
					EdifecsFileConstants.DENTAL_PROVIDER_NUMBER_SIZE));
		} else {
			dentalProductDataEgwpStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.DENTAL_PROVIDER_NUMBER_SIZE));
		}
		if (dentalProductCoverage != null) {
			dentalProductDataEgwpStr.append(EmployeesFileGenerator.padRight(EnrollmentConstants.PRODUCTID,
					EdifecsFileConstants.DENTAL_PRODUCTID_SIZE));
		} else {
			dentalProductDataEgwpStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.DENTAL_PRODUCTID_SIZE));
		}
	}

	private void getPcpProviderName(ProductCoverage dentalProductCoverage, StringBuilder fullName) {
		if (StringUtils.hasText(dentalProductCoverage.getPcp().get(0).getProviderName().getFirstName())) {
			fullName.append(dentalProductCoverage.getPcp().get(0).getProviderName().getFirstName());
		}
		if (StringUtils.hasText(dentalProductCoverage.getPcp().get(0).getProviderName().getMiddleName())) {
			fullName.append(EdifecsFileConstants.SPACE_CHAR);
			fullName.append(dentalProductCoverage.getPcp().get(0).getProviderName().getMiddleName());
		}
		if (StringUtils.hasText(dentalProductCoverage.getPcp().get(0).getProviderName().getLastName())) {
			fullName.append(EdifecsFileConstants.SPACE_CHAR);
			fullName.append(dentalProductCoverage.getPcp().get(0).getProviderName().getLastName());
		}
	}

	private String getDrugProductDataLg(Map<ProductCategory, List<ProductCoverage>> mapProductCoverages,
			Member member, Application application) {
		StringBuilder drugProductDataLgStr = new StringBuilder();
		ProductCoverage drugProductCoverage = null;
		List<ProductCoverage> drugProductCoverages = mapProductCoverages.get(ProductCategory.DRUG);
		if (CollectionUtils.isNotEmpty(drugProductCoverages)) {
			drugProductCoverage = drugProductCoverages.get(0);
		}
		if (application != null && application.getGroup() != null
				&& !application.getGroup().getGroupId().isEmpty()) {
			drugProductDataLgStr.append(EmployeesFileGenerator.padRight(application.getGroup().getGroupId(),
					EdifecsFileConstants.DRUG_GROUP_NUMBER_SIZE));
		} else {
			drugProductDataLgStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.DRUG_GROUP_NUMBER_SIZE));
		}
		if (member != null && member.getSubGroupId() != null
				&& !member.getSubGroupId().isEmpty()) {
			drugProductDataLgStr
					.append(EmployeesFileGenerator.padRight(member.getSubGroupId(),
							EdifecsFileConstants.DRUG_SUBGROUP_NUMBER_ORPACKAGE_CODE_SIZE));
		} else {
			drugProductDataLgStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.DRUG_SUBGROUP_NUMBER_ORPACKAGE_CODE_SIZE));
		}
		if (drugProductCoverage != null && drugProductCoverage.getClassId() != null
				&& !drugProductCoverage.getClassId().isEmpty()) {
			drugProductDataLgStr.append(
					EmployeesFileGenerator.padRight(drugProductCoverage.getClassId(),
							EdifecsFileConstants.DRUG_CONTRACT_CODE_OR_DEPARTMENTNUMBER_OR_CLASSID_SIZE));
		} else {
			drugProductDataLgStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.DRUG_CONTRACT_CODE_OR_DEPARTMENTNUMBER_OR_CLASSID_SIZE));
		}
		drugProdData(drugProductCoverage, drugProductDataLgStr);
		drugProductDataLgStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.FILLER_WITH_EMPTY_VALUE,
				EdifecsFileConstants.DRUG_FILLER_SIZE));
		return drugProductDataLgStr.toString();
	}

	private void drugProdData(ProductCoverage drugProductCoverage, StringBuilder drugProductDataLgStr) {
		if (drugProductCoverage != null
				&& enrollmentUtil.getCoverageLevelByCoverageType(drugProductCoverage.getCoverageLevel()) != null) {
			drugProductDataLgStr.append(EmployeesFileGenerator.padRight(
					enrollmentUtil.getCoverageLevelByCoverageType(drugProductCoverage.getCoverageLevel()),
					EdifecsFileConstants.DRUG_COVERAGE_LEVEL_SIZE));
		} else {
			drugProductDataLgStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.DRUG_COVERAGE_LEVEL_SIZE));
		}
		if (drugProductCoverage != null && drugProductCoverage.getEffectiveDate() != null) {
			drugProductDataLgStr
					.append(EmployeesFileGenerator.padRight(
							formatDateValue(drugProductCoverage.getEffectiveDate()),
							EdifecsFileConstants.DRUG_COVERAGE_EFFECTIVEDATE_SIZE));
		} else {
			drugProductDataLgStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.DRUG_COVERAGE_EFFECTIVEDATE_SIZE));
		}
		// if (drugProductCoverage != null && drugProductCoverage.getTerminationCode()
		// != null
		// && !drugProductCoverage.getTerminationCode().isEmpty()) {
		// drugProductDataLgStr
		// .append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
		// EdifecsFileConstants.DRUG_COVERAGE_TERMINATION_CODE_SIZE));
		// } else {
		drugProductDataLgStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
				EdifecsFileConstants.DRUG_COVERAGE_TERMINATION_CODE_SIZE));
		// }
		if (drugProductCoverage != null && drugProductCoverage.getTerminationDate() != null) {
			drugProductDataLgStr
					.append(EmployeesFileGenerator.padRight(
							formatDateValue(drugProductCoverage.getTerminationDate()),
							EdifecsFileConstants.DRUG_COVERAGE_TERMINATIONDATE_SIZE));
		} else {
			drugProductDataLgStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.DRUG_COVERAGE_TERMINATIONDATE_SIZE));
		}
		if (drugProductCoverage != null && drugProductCoverage.getProductId() != null
				&& !drugProductCoverage.getProductId().isEmpty()) {
			drugProductDataLgStr.append(EmployeesFileGenerator.padRight(EnrollmentConstants.PRODUCTID,
					EdifecsFileConstants.DRUG_PRODUCTID_SIZE));
		} else {
			drugProductDataLgStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.DRUG_PRODUCTID_SIZE));
		}
	}

	private String getPcpSelectionData(Map<ProductCategory, List<ProductCoverage>> mapProductCoverages,
			Member member, Application application) {
		StringBuilder pcpSelectionDataStr = new StringBuilder();
		ProductCoverage medicalProductCoverage = null;
		List<ProductCoverage> medicalProductCoverages = mapProductCoverages.get(ProductCategory.MEDICAL);
		if (CollectionUtils.isNotEmpty(medicalProductCoverages)) {
			medicalProductCoverage = medicalProductCoverages.get(0);
		}
		PCP medicalPcp = null;
		if (null != medicalProductCoverage) {
			medicalPcp = medicalProductCoverage.getPcp().get(0);
		}
		if (medicalPcp != null && medicalPcp.getProviderId() != null
				&& !medicalPcp.getProviderId().isEmpty()) {
			pcpSelectionDataStr.append(EmployeesFileGenerator.padRight(medicalPcp.getProviderId(),
					EdifecsFileConstants.PCP_NUMBER_SIZE));
		} else {
			pcpSelectionDataStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.PCP_NUMBER_SIZE));
		}
		if (null != medicalPcp && !ObjectUtils.isEmpty(medicalPcp.getEffectiveDate())) {
			pcpSelectionDataStr
					.append(EmployeesFileGenerator.padRight(
							formatDateValue(medicalPcp.getEffectiveDate()),
							EdifecsFileConstants.PCP_EEFECTIVE_OR_TERMINATION_DATE_SIZE));
		} else {
			pcpSelectionDataStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.PCP_EEFECTIVE_OR_TERMINATION_DATE_SIZE));
		}
		pcpSelectionDataStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.FILLER_WITH_EMPTY_VALUE,
				EdifecsFileConstants.PCP_FILLER1_SIZE));
		pcpSelectionDataStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
				EdifecsFileConstants.EXISTING_PATIENT_INDICATOR1_SIZE));
		// }
		pcpData(medicalPcp, pcpSelectionDataStr);
		pcpSelectionDataStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.FILLER_WITH_EMPTY_VALUE,
				EdifecsFileConstants.PCP_FILLER3_SIZE));
		return pcpSelectionDataStr.toString();
	}

	private void pcpData(PCP medicalPcp, StringBuilder pcpSelectionDataStr) {
		pcpSelectionDataStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
				EdifecsFileConstants.SECOND_CHOICE_PCP_NUMBER_SIZE));
		pcpSelectionDataStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
				EdifecsFileConstants.EXISTING_PATIENT_INDICATOR2_SIZE));
		pcpSelectionDataStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.FILLER_WITH_EMPTY_VALUE,
				EdifecsFileConstants.PCP_FILLER2_SIZE));
		if (medicalPcp != null && StringUtils.hasText(medicalPcp.getProviderId())) {
			pcpSelectionDataStr.append(EmployeesFileGenerator.padRight(medicalPcp.getProviderId(),
					EdifecsFileConstants.SPECIALIST_PCP_NUMBER_SIZE));
		} else {
			pcpSelectionDataStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.SPECIALIST_PCP_NUMBER_SIZE));
		}
		pcpSelectionDataStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
				EdifecsFileConstants.EXISTING_PATIENT_INDICATOR3_SIZE));
	}

	private String getMedicalCoverageDataLg(Map<ProductCategory, List<ProductCoverage>> mapProductCoverages,
			Member member, Application application) {
		StringBuilder medicalCoverageDataEgwpStr = new StringBuilder();
		ProductCoverage medicalProductCoverage = null;
		List<ProductCoverage> medicalProductCoverages = mapProductCoverages.get(ProductCategory.MEDICAL);
		if (CollectionUtils.isNotEmpty(medicalProductCoverages)) {
			medicalProductCoverage = medicalProductCoverages.get(0);
		}
		if (null != application && null != application.getGroup()
				&& StringUtils.hasText(application.getGroup().getGroupId())) {
			medicalCoverageDataEgwpStr
					.append(EmployeesFileGenerator.padRight(application.getGroup().getGroupId(),
							EdifecsFileConstants.MEDICAL_GROUP_NUMBER_SIZE));
		} else {
			medicalCoverageDataEgwpStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.MEDICAL_GROUP_NUMBER_SIZE));
		}
		if (member != null && StringUtils.hasText(member.getSubGroupId())) {
			medicalCoverageDataEgwpStr.append(
					EmployeesFileGenerator.padRight(member.getSubGroupId(),
							EdifecsFileConstants.SUB_GROUP_NUMBER_SIZE));
		} else {
			medicalCoverageDataEgwpStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.SUB_GROUP_NUMBER_SIZE));
		}
		if (medicalProductCoverage != null && StringUtils.hasText(medicalProductCoverage.getClassId())) {
			medicalCoverageDataEgwpStr.append(EmployeesFileGenerator.padRight(
					medicalProductCoverage.getClassId(), EdifecsFileConstants.CLASS_CODE_SIZE));
		} else {
			medicalCoverageDataEgwpStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.CLASS_CODE_SIZE));
		}
		if (medicalProductCoverage != null
				&& enrollmentUtil.getCoverageLevelByCoverageType(medicalProductCoverage.getCoverageLevel()) != null) {
			medicalCoverageDataEgwpStr.append(EmployeesFileGenerator.padRight(
					enrollmentUtil.getCoverageLevelByCoverageType(medicalProductCoverage.getCoverageLevel()),
					EdifecsFileConstants.MEDICAL_COVERAGE_LEVEL_SIZE));
		} else {
			medicalCoverageDataEgwpStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.MEDICAL_COVERAGE_LEVEL_SIZE));
		}
		medicalCoverage(medicalProductCoverage, medicalCoverageDataEgwpStr);
		medicalCoverageDataEgwpStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.FILLER_WITH_EMPTY_VALUE,
				EdifecsFileConstants.MEDICAL_COVERAGE_DATA_FILLER_SIZE));
		return medicalCoverageDataEgwpStr.toString();
	}

	private void medicalCoverage(ProductCoverage medicalProductCoverage,
			StringBuilder medicalCoverageDataEgwpStr) {
		if (medicalProductCoverage != null && medicalProductCoverage.getEffectiveDate() != null) {
			medicalCoverageDataEgwpStr
					.append(EmployeesFileGenerator.padRight(formatDateValue(medicalProductCoverage.getEffectiveDate()),
							EdifecsFileConstants.MEDICAL_COVERAGE_EFFECTIVE_DATE_SIZE));
		} else {
			medicalCoverageDataEgwpStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.MEDICAL_COVERAGE_EFFECTIVE_DATE_SIZE));
		}
		if (medicalProductCoverage != null && null != medicalProductCoverage.getTerminationDate()) {
			medicalCoverageDataEgwpStr
					.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
							EdifecsFileConstants.MEDICAL_COVERAGE_TERMINATION_CODE_SIZE));
		} else {
			medicalCoverageDataEgwpStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.MEDICAL_COVERAGE_TERMINATION_CODE_SIZE));
		}
		if (medicalProductCoverage != null && null != medicalProductCoverage.getTerminationDate()) {
			medicalCoverageDataEgwpStr
					.append(EmployeesFileGenerator.padRight(
							formatDateValue(medicalProductCoverage.getTerminationDate()),
							EdifecsFileConstants.MEDICAL_COVERAGE_TERMINATION_DATE_SIZE));
		} else {
			medicalCoverageDataEgwpStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.MEDICAL_COVERAGE_TERMINATION_DATE_SIZE));
		}
		mediCvrgData(medicalProductCoverage, medicalCoverageDataEgwpStr);
	}

	private void mediCvrgData(ProductCoverage medicalProductCoverage, StringBuilder medicalCoverageDataEgwpStr) {
		medicalCoverageDataEgwpStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
				EdifecsFileConstants.HOME_PLAN_CODE_SIZE));
		medicalCoverageDataEgwpStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
				EdifecsFileConstants.CONTROL_PLAN_CODE_SIZE));
		if (medicalProductCoverage != null && EnrollmentConstants.PRODUCTID != null
				&& !medicalProductCoverage.getProductId().isEmpty()) {
			medicalCoverageDataEgwpStr.append(EmployeesFileGenerator.padRight(
					EnrollmentConstants.PRODUCTID, EdifecsFileConstants.MEDICAL_PRODUCTID_SIZE));
		} else {
			medicalCoverageDataEgwpStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.MEDICAL_PRODUCTID_SIZE));
		}
	}

	private String getAdditionalMemberInformation(Member member) {
		StringBuilder additionalMemberInformationStr = new StringBuilder();
		Address address = null;
		if (CollectionUtils.isNotEmpty(member.getAddress())) {
			address = member.getAddress().stream().filter(addr -> Choice.Yes.equals(addr.getIsPrimary()))
					.findFirst().orElse(null);
		}
		if (null != address) {
			setAddressLine1AndLine2(additionalMemberInformationStr, address);
			if (address.getCity() != null && !address.getCity().isEmpty()) {
				additionalMemberInformationStr.append(EmployeesFileGenerator.padRight(address.getCity(),
						EdifecsFileConstants.CITY_SIZE));
			} else {
				additionalMemberInformationStr.append(
						EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
								EdifecsFileConstants.CITY_SIZE));
			}
			if (address.getStateCode() != null && !address.getStateCode().isEmpty()) {
				additionalMemberInformationStr.append(EmployeesFileGenerator
						.padRight(address.getStateCode(), EdifecsFileConstants.STATE_SIZE));
			} else {
				additionalMemberInformationStr.append(
						EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
								EdifecsFileConstants.STATE_SIZE));
			}
			additionalMembInfo(member, additionalMemberInformationStr, address);
		}
		additionalMemberInformationStr.append(EmployeesFileGenerator.padRight(
				EdifecsFileConstants.FILLER_WITH_EMPTY_VALUE, EdifecsFileConstants.ADDITIONALMEMBERINFO_FILER_SIZE));
		additionalMemberInformationStr
				.append(EmployeesFileGenerator.padRight(member.getSocialSecurityNumber(),
						EdifecsFileConstants.MEMBER_SOCIAL_SECURITY_NUMBER_SIZE));
		return additionalMemberInformationStr.toString();
	}

	private void setAddressLine1AndLine2(StringBuilder additionalMemberInformationStr, Address address) {
		if (address.getLine1() != null
				&& !address.getLine1().isEmpty()) {
			additionalMemberInformationStr.append(EmployeesFileGenerator
					.padRight(address.getLine1(), EdifecsFileConstants.ADDRESSLINE1_SIZE));
		} else {
			additionalMemberInformationStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.ADDRESSLINE1_SIZE));
		}
		if (address.getLine2() != null
				&& !address.getLine2().isEmpty()) {
			additionalMemberInformationStr.append(EmployeesFileGenerator
					.padRight(address.getLine2(), EdifecsFileConstants.ADDRESSLINE2_SIZE));
		} else {
			additionalMemberInformationStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.ADDRESSLINE2_SIZE));
		}
	}

	private void additionalMembInfo(Member member,
			StringBuilder additionalMemberInformationStr, Address address) {
		if (address.getZipCode() != null && !address.getZipCode().isEmpty()) {
			additionalMemberInformationStr.append(EmployeesFileGenerator.padRight(address.getZipCode(),
					EdifecsFileConstants.ZIP_SIZE));
		} else {
			additionalMemberInformationStr.append(
					EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE, EdifecsFileConstants.ZIP_SIZE));
		}
		Phone homePhone = null;
		Phone workPhone = null;
		if (null != member.getContact()) {
			homePhone = member.getContact().getPhone().stream()
					.filter(phone -> PhoneType.H.equals(phone.getPhoneType())).findFirst().orElse(null);
			workPhone = member.getContact().getPhone().stream()
					.filter(phone -> PhoneType.W.equals(phone.getPhoneType())).findFirst().orElse(null);
		}
		setMemberContactPhone(member, additionalMemberInformationStr, homePhone, workPhone);
	}

	private void setMemberContactPhone(Member member, StringBuilder additionalMemberInformationStr, Phone homePhone,
			Phone workPhone) {
		if (member.getContact() != null
				&& CollectionUtils.isNotEmpty(member.getContact().getPhone()) && null != homePhone) {
			additionalMemberInformationStr.append(EmployeesFileGenerator.padRight(
					homePhone.getExtension(), EdifecsFileConstants.HOMEPHONE_AREACODE_SIZE));
		} else {
			additionalMemberInformationStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.HOMEPHONE_AREACODE_SIZE));
		}
		if (member.getContact() != null
				&& CollectionUtils.isNotEmpty(member.getContact().getPhone()) && null != homePhone) {
			additionalMemberInformationStr.append(EmployeesFileGenerator
					.padRight(homePhone.getPhoneNumber(), EdifecsFileConstants.HOMEPHONE_SIZE));
		} else {
			additionalMemberInformationStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.HOMEPHONE_SIZE));
		}
		if (member.getContact() != null
				&& CollectionUtils.isNotEmpty(member.getContact().getPhone()) && null != workPhone) {
			additionalMemberInformationStr.append(EmployeesFileGenerator.padRight(
					workPhone.getExtension(), EdifecsFileConstants.WORKPHONE_AREACODE_SIZE));
		} else {
			additionalMemberInformationStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.WORKPHONE_AREACODE_SIZE));
		}
		if (member.getContact() != null
				&& CollectionUtils.isNotEmpty(member.getContact().getPhone()) && null != workPhone) {
			additionalMemberInformationStr.append(EmployeesFileGenerator
					.padRight(workPhone.getPhoneNumber(), EdifecsFileConstants.WORKPHONE_SIZE));
		} else {
			additionalMemberInformationStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.WORKPHONE_SIZE));
		}
	}

	private String getEmployeeSpecificData(EmployeeSpecificDataLg employeeSpecificData, MaritalStatus maritalStatus) {
		StringBuilder employeeSpecificDataStr = new StringBuilder();
		if (employeeSpecificData.getEmployeeNumber() != null && !employeeSpecificData.getEmployeeNumber().isEmpty()) {
			employeeSpecificDataStr.append(EmployeesFileGenerator.padRight(employeeSpecificData.getEmployeeNumber(),
					EdifecsFileConstants.EMPLOYEENUMBER_SIZE));
		} else {
			employeeSpecificDataStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.EMPLOYEENUMBER_SIZE));
		}
		if (employeeSpecificData.getEmployeeLocation() != null
				&& !employeeSpecificData.getEmployeeLocation().isEmpty()) {
			employeeSpecificDataStr.append(EmployeesFileGenerator.padRight(employeeSpecificData.getEmployeeLocation(),
					EdifecsFileConstants.EMPLOYEELOCATION_SIZE));
		} else {
			employeeSpecificDataStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.EMPLOYEELOCATION_SIZE));
		}
		if (employeeSpecificData.getEmployeeSalaryAmount() != null
				&& !employeeSpecificData.getEmployeeSalaryAmount().isEmpty()) {
			employeeSpecificDataStr.append(EmployeesFileGenerator.padRight(
					employeeSpecificData.getEmployeeSalaryAmount(), EdifecsFileConstants.EMPLOYEE_SAL_AMOUNT_SIZE));
		} else {
			employeeSpecificDataStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.EMPLOYEE_SAL_AMOUNT_SIZE));
		}
		if (employeeSpecificData.getEmployeeSalaryEffectiveDate() != null
				&& !employeeSpecificData.getEmployeeSalaryEffectiveDate().isEmpty()) {
			employeeSpecificDataStr
					.append(EmployeesFileGenerator.padRight(employeeSpecificData.getEmployeeSalaryEffectiveDate(),
							EdifecsFileConstants.EMPLOYEE_SAL_EFFECTIVE_DATE));
		} else {
			employeeSpecificDataStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.EMPLOYEE_SAL_EFFECTIVE_DATE));
		}
		empSpecData(employeeSpecificData, employeeSpecificDataStr, maritalStatus);
		employeeSpecificDataStr.append(EmployeesFileGenerator.padRight(employeeSpecificData.getStatusIndicator(),
				EdifecsFileConstants.STATUS_INDICATOR_SIZE));
		employeeSpecificDataStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.FILLER_WITH_EMPTY_VALUE,
				EdifecsFileConstants.EMPLOYEE_SPECIFIC_DATA_FILLER_SIZE));
		return employeeSpecificDataStr.toString();
	}

	private void empSpecData(EmployeeSpecificDataLg employeeSpecificData, StringBuilder employeeSpecificDataStr,
			MaritalStatus maritalStatus) {
		if (employeeSpecificData.getEmploymentHireDate() != null
				&& !employeeSpecificData.getEmploymentHireDate().isEmpty()) {
			employeeSpecificDataStr.append(EmployeesFileGenerator.padRight(employeeSpecificData.getEmploymentHireDate(),
					EdifecsFileConstants.EMPLOYEE_HIRE_DATE_SIZE));
		} else {
			employeeSpecificDataStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.EMPLOYEE_HIRE_DATE_SIZE));
		}
		if (maritalStatus != null && StringUtils.hasText(maritalStatus.getValue())) {
			employeeSpecificDataStr
					.append(EmployeesFileGenerator.padRight(maritalStatus.getValue(),
							EdifecsFileConstants.MARITAL_STARUS_SIZE));
		} else {
			employeeSpecificDataStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.MARITAL_STARUS_SIZE));
		}
		if (employeeSpecificData.getMarriageDate() != null && !employeeSpecificData.getMarriageDate().isEmpty()) {
			employeeSpecificDataStr.append(EmployeesFileGenerator.padRight(employeeSpecificData.getMarriageDate(),
					EdifecsFileConstants.MARRIAGE_DATE_SIZE));
		} else {
			employeeSpecificDataStr.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.MARRIAGE_DATE_SIZE));
		}
	}

	private StringBuilder getAccountHeaderData(AccountHeader accountHeader) {
		StringBuilder fileHeaderBuffer = new StringBuilder();
		if (accountHeader.getRecordType() != null && !accountHeader.getRecordType().isEmpty()) {
			fileHeaderBuffer.append(EmployeesFileGenerator.padRight(accountHeader.getRecordType(),
					EdifecsFileConstants.RECORDTYPE_SIZE));
		} else {
			fileHeaderBuffer.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.RECORDTYPE_SIZE));
		}
		if (accountHeader.getFileType() != null && !accountHeader.getFileType().isEmpty()) {
			fileHeaderBuffer.append(
					EmployeesFileGenerator.padRight(accountHeader.getFileType(), EdifecsFileConstants.FILETYPE_SIZE));
		} else {
			fileHeaderBuffer.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.FILETYPE_SIZE));
		}
		accntHdr1(accountHeader, fileHeaderBuffer);
		accntHdr2(accountHeader, fileHeaderBuffer);
		fileHeaderBuffer.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.RENEWAL_INDICATOR,
				EdifecsFileConstants.RENEWAL_INDICATOR_SIZE));
		fileHeaderBuffer.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.GROUPTYPE_WITH_EMPTY_VALUE,
				EdifecsFileConstants.GROUP_TYPE_SIZE));
		fileHeaderBuffer.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.FILLER_WITH_EMPTY_VALUE,
				EdifecsFileConstants.ACCOUNTHEADER_FILLER_SIZE));
		fileHeaderBuffer.append(
				EmployeesFileGenerator.padRight(EdifecsFileConstants.HOLDF_FIELD, EdifecsFileConstants.HOLDFIELD_SIZE));
		return fileHeaderBuffer;
	}

	private void accntHdr1(AccountHeader accountHeader, StringBuilder fileHeaderBuffer) {
		if (accountHeader.getCompanyName() != null && !accountHeader.getCompanyName().isEmpty()) {
			fileHeaderBuffer.append(EmployeesFileGenerator.padRight(accountHeader.getCompanyName(),
					EdifecsFileConstants.COMPANYNAME_SIZE));
		} else {
			fileHeaderBuffer.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.COMPANYNAME_SIZE));
		}
		if (accountHeader.getUniqueCoIdentifierFedTaxId() != null
				&& !accountHeader.getUniqueCoIdentifierFedTaxId().isEmpty()) {
			fileHeaderBuffer.append(EmployeesFileGenerator.padRight(accountHeader.getUniqueCoIdentifierFedTaxId(),
					EdifecsFileConstants.UNIQUE_CO_IDENTIFIER_FEB_TAXID_SIZE));
		} else {
			fileHeaderBuffer.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.UNIQUE_CO_IDENTIFIER_FEB_TAXID_SIZE));
		}
		if (accountHeader.getContactName() != null && !accountHeader.getContactName().isEmpty()) {
			fileHeaderBuffer.append(EmployeesFileGenerator.padRight(accountHeader.getContactName(),
					EdifecsFileConstants.CONTACTNAME_SIZE));
		} else {
			fileHeaderBuffer.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.CONTACTNAME_SIZE));
		}
		if (accountHeader.getContactTelephoneNumber() != null && !accountHeader.getContactTelephoneNumber().isEmpty()) {
			fileHeaderBuffer.append(EmployeesFileGenerator.padRight(accountHeader.getContactTelephoneNumber(),
					EdifecsFileConstants.CONTACT_TELEPHONE_NUMBER_SIZE));
		} else {
			fileHeaderBuffer.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.CONTACT_TELEPHONE_NUMBER_SIZE));
		}
		if (accountHeader.getDateFileSubmitted() != null && !accountHeader.getDateFileSubmitted().isEmpty()) {
			fileHeaderBuffer.append(EmployeesFileGenerator.padRight(accountHeader.getDateFileSubmitted(),
					EdifecsFileConstants.DATEFILESUBMITTED_SIZE));
		} else {
			fileHeaderBuffer.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.DATEFILESUBMITTED_SIZE));
		}
	}

	private void accntHdr2(AccountHeader accountHeader, StringBuilder fileHeaderBuffer) {
		if (accountHeader.getMemberTermDateDefault() != null && !accountHeader.getMemberTermDateDefault().isEmpty()) {
			fileHeaderBuffer.append(EmployeesFileGenerator.padRight(accountHeader.getMemberTermDateDefault(),
					EdifecsFileConstants.MEMBER_TERM_DATE_DEFAULT_SIZE));
		} else {
			fileHeaderBuffer.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.MEMBER_TERM_DATE_DEFAULT_SIZE));
		}
		if (accountHeader.getSalesPersonNumber() != null && !accountHeader.getSalesPersonNumber().isEmpty()) {
			fileHeaderBuffer.append(EmployeesFileGenerator.padRight(accountHeader.getSalesPersonNumber(),
					EdifecsFileConstants.SALESPERSONNUMBER_SIZE));
		} else {
			fileHeaderBuffer.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.SALESPERSONNUMBER_SIZE));
		}
		if (accountHeader.getPcpIndicator() != null && !accountHeader.getPcpIndicator().isEmpty()) {
			fileHeaderBuffer.append(EmployeesFileGenerator.padRight(accountHeader.getPcpIndicator(),
					EdifecsFileConstants.PCP_INDICATOR_SIZE));
		} else {
			fileHeaderBuffer.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.PCP_INDICATOR_SIZE));
		}
		if (accountHeader.getResubmittedFileIndicator() != null
				&& !accountHeader.getResubmittedFileIndicator().isEmpty()) {
			fileHeaderBuffer.append(EmployeesFileGenerator.padRight(accountHeader.getResubmittedFileIndicator(),
					EdifecsFileConstants.RESUBMITTED_FILE_INDICATOR_SIZE));
		} else {
			fileHeaderBuffer.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.RESUBMITTED_FILE_INDICATOR_SIZE));
		}
		if (accountHeader.getResubmittedFileOriginalDate() != null
				&& !accountHeader.getResubmittedFileOriginalDate().isEmpty()) {
			fileHeaderBuffer.append(EmployeesFileGenerator.padRight(accountHeader.getResubmittedFileOriginalDate(),
					EdifecsFileConstants.RESUBMITTED_FILE_ORIGINALDATE_SIZE));
		} else {
			fileHeaderBuffer.append(EmployeesFileGenerator.padRight(EdifecsFileConstants.EMPTY_VALUE,
					EdifecsFileConstants.RESUBMITTED_FILE_ORIGINALDATE_SIZE));
		}
	}
}
