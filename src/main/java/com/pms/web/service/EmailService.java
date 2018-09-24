package com.pms.web.service;

import org.springframework.mail.MailException;

import com.pms.app.mail.EmailTemplate;
import com.pms.app.view.vo.AppUserVO;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.ServiceProviderVO;
import com.pms.app.view.vo.TicketVO;
import com.pms.jpa.entities.SPEscalationLevels;
import com.pms.web.util.RestResponse;

public interface EmailService {


	public void sendEmail(EmailTemplate emailTemplate) throws MailException;
	public RestResponse sendEmail(String to, String message) throws Exception;
	public RestResponse sendSuccessSaveEmail(String emailId, AppUserVO appUserVO) throws Exception;
	public RestResponse successSaveSPEmail(ServiceProviderVO serviceProviderVO, LoginUser loginUser) throws Exception;
	public void successTicketCreationSPEmail(TicketVO ticketVO, String creationStatus, String company) throws Exception;
	public RestResponse sendForgotPasswordEmail(String email, String passwordResetLink) throws Exception;
	RestResponse successEscalationLevel(TicketVO ticketVO, SPEscalationLevels spEscalationLevel, String ccLevelEmail, String level) throws Exception;
}
