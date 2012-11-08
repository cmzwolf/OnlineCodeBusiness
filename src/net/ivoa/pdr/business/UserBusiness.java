package net.ivoa.pdr.business;

import java.sql.SQLException;
import java.util.Map;

import net.ivoa.oc.dao.NotificationsDAO;
import net.ivoa.oc.dao.UserDAO;

/**
 * @author Carlo Maria Zwolf
 * Observatoire de Paris
 * LERMA
 */

public class UserBusiness {
	private static final UserBusiness instance = new UserBusiness();

	public static UserBusiness getInstance() {
		return instance;
	}

	private UserBusiness() {
	}
	
	public Integer getIdUserByMail(String mail) throws SQLException, ClassNotFoundException{
		return UserDAO.getInstance().getIdUserByMail(mail);
	}
	
	public Map<Integer, String> getMailToNotifyForGivenJob(Integer idConfig) throws SQLException, ClassNotFoundException{
		return UserDAO.getInstance().getMailToNotifyForGivenJob(idConfig);
	}
	
	public void markUserAsNotifiedForGivenJob(Integer idUser,
			Integer idConfiguration) throws SQLException, ClassNotFoundException{
		UserDAO.getInstance().markUserAsNotifiedForGivenJob(idUser, idConfiguration);
	}
	
	public String getMailFromUserId(Integer userId) throws SQLException, ClassNotFoundException{
		return UserDAO.getInstance().getMailFromUserId(userId);
	}
	
	public void cutLinkUserJob(Integer idUser, Integer idJob) throws SQLException, ClassNotFoundException{
		NotificationsDAO.getInstance().cutLinkUserJob(idUser, idJob);
	}
}
