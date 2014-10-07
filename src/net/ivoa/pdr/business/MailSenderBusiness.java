package net.ivoa.pdr.business;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.ivoa.oc.dao.NotificationsDAO;
import net.ivoa.pdr.commons.JobBean;
import net.ivoa.pdr.commons.MailConfig;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

/**
 * @author Carlo Maria Zwolf Observatoire de Paris LERMA
 */

public class MailSenderBusiness {

	private static final MailSenderBusiness instance = new MailSenderBusiness();

	public static MailSenderBusiness getInstance() {
		return instance;
	}

	private MailSenderBusiness() {
	}

	public void notifyPurgeToUserAskedThisJob(JobBean job) throws SQLException,
			ClassNotFoundException, EmailException {

		MailConfig mailConfig = MailConfigBusiness.getInstance()
				.getMailConfig();

		List<String> userAskedForThisJob = job.getUserAskedThisJob().get(0);
		List<String> dateWhereUserAskedTheJob = job.getUserAskedThisJob()
				.get(1);
		List<String> userWantMail = job.getUserAskedThisJob().get(2);

		for (int i = 0; i < userAskedForThisJob.size(); i++) {
			// if the user want to receive a mail for notifying actions
			if(userWantMail.get(i).equalsIgnoreCase("true")){
				String userMail = userAskedForThisJob.get(i);
				String dateDemand = dateWhereUserAskedTheJob.get(i);
				MailSenderBusiness.getInstance().sendMailNotifingPurge(userMail,
						dateDemand, job, mailConfig);
			}		
		}
	}

	public void notifyUsersOfAvailableResults() throws SQLException,
			ClassNotFoundException {

		MailConfig mailConfig = MailConfigBusiness.getInstance()
				.getMailConfig();

		System.out
				.println("Getting the list of finished job that have to be notified to users");
		List<Integer> finishedJobsToNotify = JobBusiness.getInstance()
				.getFinishedJobsToBeNotified();

		for (Integer currentJobId : finishedJobsToNotify) {
			System.out.println("Trying to notify the job " + currentJobId);

			JobBean finishedJob = JobBusiness.getInstance()
					.getJobBeanFromIdJob(currentJobId);

			Map<Integer, String> mailToNotify = finishedJob.getUserToNotify();

			for (Entry<Integer, String> entry : mailToNotify.entrySet()) {
				System.out.println("Notifying the user " + entry.getKey()
						+ " : " + entry.getValue());

				try {
					// If the user is not notified and asked for a receipt mail
					for (Boolean currentFlag : NotificationsDAO.getInstance()
							.hasToSentMailToUserNotNotified(entry.getKey(),
									currentJobId)) {

						if (currentFlag) {
							System.out.println("sending mail");
							MailSenderBusiness.getInstance()
									.sendMailNotifingResults(entry.getValue(),
											finishedJob, mailConfig);
							System.out.println("mail successfully sent");
						}

					}

					// Mark the user as notified (in both case he asked or not
					// for a mail).
					UserBusiness.getInstance().markUserAsNotifiedForGivenJob(
							entry.getKey(), currentJobId);
					System.out
							.println("Job " + currentJobId
									+ "Marked as notified for user "
									+ entry.getValue());
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Fatal error in sanding to user "
							+ entry.getValue() + " notifications for job "
							+ currentJobId);
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
			String message) throws SQLException, ClassNotFoundException {
		try {
			MailConfig mailConfig = MailConfigBusiness.getInstance()
					.getMailConfig();
			

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
		for (Entry<String, String> result : finishedJob.getJobResults()
				.entrySet()) {

			toReturn = toReturn + result.getKey() + ":= " + result.getValue()
					+ "\n";
		}
		toReturn = toReturn + "\n";
		toReturn = toReturn + "We recall you that :\n";
		toReturn = toReturn
				+ JobBusiness.getInstance().describeJobInTextMode(finishedJob);

		return toReturn;
	}

}
