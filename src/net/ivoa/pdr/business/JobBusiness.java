package net.ivoa.pdr.business;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.ivoa.oc.dao.JobDAO;
import net.ivoa.pdr.commons.JobBean;

/**
 * @author Carlo Maria Zwolf Observatoire de Paris LERMA
 */

public class JobBusiness {
	private static final JobBusiness instance = new JobBusiness();

	public static JobBusiness getInstance() {
		return instance;
	}

	private JobBusiness() {
	}

	public List<Integer> getNotProcessedJobs() throws SQLException,
			ClassNotFoundException {
		return JobDAO.getInstance().getNotProcessedJobs();
	}

	public void markJobAsProcessed(Integer idConfiguration)
			throws SQLException, ClassNotFoundException {
		JobDAO.getInstance().markJobAsProcessed(idConfiguration);
	}

	public List<Integer> getProcessedJobs() throws SQLException,
			ClassNotFoundException {
		return JobDAO.getInstance().getProcessedJobs();
	}

	public void insertResults(Integer idConfiguration, String urlResult,
			String resultName) throws SQLException, ClassNotFoundException {
		JobDAO.getInstance().insertResults(idConfiguration, urlResult,
				resultName);
	}

	public void markJobAsFinished(Integer idConfiguration) throws SQLException,
			ClassNotFoundException {
		JobDAO.getInstance().markJobAsFinished(idConfiguration);
	}

	public List<Integer> getFinishedJobs() throws SQLException,
			ClassNotFoundException {
		return JobDAO.getInstance().getFinishedJobs();
	}

	public Map<String, String> getResultsFromIdJob(Integer idConfiguration)
			throws SQLException, ClassNotFoundException {
		return JobDAO.getInstance().getResultsFromIdJob(idConfiguration);
	}

	public JobBean getJobBeanFromIdJob(Integer idConfiguration)
			throws SQLException, ClassNotFoundException {
		return JobDAO.getInstance().getJobBeanFromIdJob(idConfiguration);
	}

	public String getDateWhereUserAskedTheJob(Integer idUser, Integer idJob)
			throws SQLException, ClassNotFoundException {
		return JobDAO.getInstance().getDateWhereUserAskedTheJob(idUser, idJob);
	}

	public String getDateWhereUserReceiveNotificationForJob(Integer idUser,
			Integer idJob) throws SQLException, ClassNotFoundException {
		return JobDAO.getInstance().getDateWhereUserReceiveNotificationForJob(
				idUser, idJob);
	}

	public List<Integer> getListOfJobsAskedByUser(Integer idUser)
			throws SQLException, ClassNotFoundException {
		return JobDAO.getInstance().getListOfJobsAskedByUser(idUser);
	}

	public String describeJobInTextMode(JobBean job) {
		String toReturn = "The Id of this job in our database =="
				+ job.getIdJob() + "\n\n";
		if (job.isJobValid()) {
			toReturn = toReturn
					+ "Job characterized by the following parameters:\n\n";
			for (Entry<String, String> param : job.getJobConfiguration()
					.entrySet()) {
				toReturn = toReturn + param.getKey() + "=" + param.getValue()
						+ "\n\n";
			}
			if (null != job.getJobErrors() && job.getJobErrors().size() > 0) {
				toReturn += "This job has the following errors: \n";
				for (String currentError : job.getJobErrors()) {
					toReturn += currentError + "\n";
				}
			}
		} else {
			toReturn = toReturn
					+ "this job is not valid because (at least) one of its parameter does not satisfy the constraint on input parameter.\n";
			toReturn = toReturn + "As a consequence, it has been deleted.";
		}

		return toReturn;
	}

	public String computeJobPhase(JobBean job) {
		if (null == job.getProcessingDate()
				|| job.getProcessingDate().equalsIgnoreCase("")) {
			return "pending";
		}
		if (null != job.getProcessingDate()
				&& !job.getProcessingDate().equalsIgnoreCase("")
				&& (null == job.getFinishingDate() || job.getFinishingDate()
						.equalsIgnoreCase(""))) {
			return "running";
		}
		if (null != job.getFinishingDate()
				&& !job.getFinishingDate().equalsIgnoreCase("")) {
			return "finished";
		}
		return "unknown";
	}

	public String describeJobInHtmlMode(JobBean job) {
		String toReturn = "<div id=\"jobHead\">\n";
		toReturn += "<p> Job Id =" + job.getIdJob() + "</p>";
		toReturn += "</div>";
		toReturn += "<div style=\"jobDetail\">\n";
		if (job.isJobValid()) {

			toReturn += "<div id=\"jobPhase\">\n";

			String jobPhase = JobBusiness.getInstance().computeJobPhase(job);

			toReturn += "<p>Job Phase: " + jobPhase + ".</p>";
			toReturn += "</div>\n";

			toReturn += "<p> This job is characterized by the following parameters </p>\n";

			toReturn += "<table border = \"1\" cellpadding=\"0\" cellspacing=\"0\">";
			toReturn += "<tr><th>Parameter Name</th><th> Parameter Value </th>  </tr>";
			for (Entry<String, String> param : job.getJobConfiguration()
					.entrySet()) {
				toReturn += "<tr>\n";
				toReturn += "<td>" + param.getKey() + "</td><td>"
						+ param.getValue() + "</td>\n";
				toReturn += "</tr>\n";
			}
			toReturn += "</table><br></br>";

			if (job.getJobResults().size() > 0) {
				toReturn += "<p> Results for this job are </p>\n";
				toReturn += "<table border = \"1\" cellpadding=\"0\" cellspacing=\"0\">";
				for (Entry<String, String> result : job.getJobResults()
						.entrySet()) {

					String resultUrl = result.getValue();
					String resultName = result.getKey();

					toReturn += "<tr>\n";
					
					toReturn += "<td>"+resultName+"<td>\n";
					
					toReturn += "<td> <a href=\"" + resultUrl + "\">"
							+ resultUrl + "</td>\n";

					toReturn += "</tr>\n";
				}
				toReturn += "</table><br>";
			}
		} else {
			toReturn += "<p> This job is not valid because (at least) one of its parameter does not satisfy the constraint on input parameter <br> As a consequence, it has been deleted. </p>\n";
		}
		toReturn += "</div>";

		return toReturn;
	}

}
