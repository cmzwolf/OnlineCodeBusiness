package net.ivoa.pdr.business;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.ivoa.pdr.commons.JobBean;
import net.ivoa.pdr.commons.MailConfig;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

/**
 * @author Carlo Maria Zwolf
 * Observatoire de Paris
 * LERMA
 */

public class MailSenderBusiness {

	private static final MailSenderBusiness instance = new MailSenderBusiness();

	public static MailSenderBusiness getInstance() {
		return instance;
	}

	private MailSenderBusiness() {
	}

	public void notifyPurgeToUserAskedThisJob(JobBean job)
			throws SQLException, ClassNotFoundException, EmailException {

		MailConfig mailConfig = MailConfigBusiness.getInstance()
				.getMailConfig();

		List<String> userAskedForThisJob = job.getUserAskedThisJob().get(0);
		List<String> dateWhereUserAskedTheJob = job.getUserAskedThisJob()
				.get(1);

		for (int i = 0; i < userAskedForThisJob.size(); i++) {
			String userMail = userAskedForThisJob.get(i);
			String dateDemand = dateWhereUserAskedTheJob.get(i);
			MailSenderBusiness.getInstance().sendMailNotifingPurge(userMail,
					dateDemand, job, mailConfig);
		}
	}

	public void notifyUsersOfAvailableResults() throws SQLException,
			ClassNotFoundException {

		MailConfig mailConfig = MailConfigBusiness.getInstance()
				.getMailConfig();

		List<Integer> finishedJobs = JobBusiness.getInstance()
				.getFinishedJobs();

		for (Integer currentJobId : finishedJobs) {

			JobBean finishedJob = JobBusiness.getInstance()
					.getJobBeanFromIdJob(currentJobId);

			Map<Integer, String> mailToNotify = finishedJob.getUserToNotify();

			for (Entry<Integer, String> entry : mailToNotify.entrySet()) {
				try {
					MailSenderBusiness.getInstance().sendMailNotifingResults(
							entry.getValue(), finishedJob, mailConfig);
					UserBusiness.getInstance().markUserAsNotifiedForGivenJob(
							entry.getKey(), currentJobId);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void sendMailNotifingPurge(String userMail, String dateDemande,
			JobBean jobToDelete, MailConfig mailConfig) throws EmailException {
		SimpleEmail email = new SimpleEmail();
		email.setAuthentication(mailConfig.getUserName(),
				mailConfig.getPassword());
		email.setHostName(mailConfig.getServerName());
		email.addTo(userMail);
		email.setFrom(mailConfig.getFromAdress(), mailConfig.getFromLabel());
		email.setSubject(mailConfig.getSubject()
				+ " - Notification of job destruction");
		email.setMsg(MailSenderBusiness.getInstance()
				.buildMessageBodyForDeletingJob(jobToDelete, dateDemande));
		
		email.setSSL(true);
		email.setSmtpPort(465);
		
		email.send();
	}

	public void sendMailNotifingNewJobs(String userMail, Integer userId,
			String jobsDescription) throws SQLException, ClassNotFoundException {
		try {
			MailConfig mailConfig = MailConfigBusiness.getInstance()
					.getMailConfig();
			String message = "Hello,\n\n";

			message += "Please visit the link "
					+ GlobalTechConfigBusiness.getInstance()
							.getServletContainer() + "JobSummary?mail="
					+ userMail + "&userIdForUser=" + userId
					+ " for job administration.\n\n";

			message += "Computation demands have been just been recorded for the following jobs:\n";

			message = message + jobsDescription;

			SimpleEmail email = new SimpleEmail();
			email.setAuthentication(mailConfig.getUserName(),
					mailConfig.getPassword());
			email.setHostName(mailConfig.getServerName());
			email.addTo(userMail);
			email.setFrom(mailConfig.getFromAdress(), mailConfig.getFromLabel());
			email.setSubject(mailConfig.getSubject()
					+ " - Creation of new jobs");
			email.setMsg(message);
			
			
			email.setSSL(true);
			email.setSmtpPort(465);
			
			email.send();
		} catch (EmailException e) {
			System.out.println("*** cannot send mail ***");
			e.printStackTrace();
		}
	}

	private String buildMessageBodyForDeletingJob(JobBean jobToDelete,
			String dateDemand) {
		String toReturn = "";

		toReturn = toReturn + "Hello,\n";
		toReturn = toReturn
				+ "this mail notifies you the destruction of the job you asked on "
				+ dateDemand + "\n";
		toReturn = toReturn + "\n";
		toReturn = toReturn
				+ "We recall you that this job had the following properties:\n\n";
		toReturn = toReturn
				+ JobBusiness.getInstance().describeJobInTextMode(jobToDelete);

		return toReturn;
	}

	private void sendMailNotifingResults(String userMail, JobBean finishedJob,
			MailConfig mailConfig) throws EmailException {

		SimpleEmail email = new SimpleEmail();
		email.setAuthentication(mailConfig.getUserName(),
				mailConfig.getPassword());
		email.setHostName(mailConfig.getServerName());
		email.addTo(userMail);
		email.setFrom(mailConfig.getFromAdress(), mailConfig.getFromLabel());
		email.setSubject(mailConfig.getSubject()
				+ " - Notification of available results");
		email.setMsg(MailSenderBusiness.getInstance()
				.buildMessageBodyFromResults(finishedJob));
		
		email.setSSL(true);
		email.setSmtpPort(465);
		
		email.send();

	}

	private String buildMessageBodyFromResults(JobBean finishedJob) {
		String toReturn = "";

		toReturn = toReturn + "Hello,\n";
		toReturn = toReturn
				+ "the results of your computation are available:\n";
		for (Entry<String,String> result : finishedJob.getJobResults().entrySet()) {

			toReturn = toReturn + result.getKey() + ":= "+ result.getValue()+"\n";
		}
		toReturn = toReturn + "\n";
		toReturn = toReturn + "We recall you that :\n";
		toReturn = toReturn
				+ JobBusiness.getInstance().describeJobInTextMode(finishedJob);

		return toReturn;
	}

}

