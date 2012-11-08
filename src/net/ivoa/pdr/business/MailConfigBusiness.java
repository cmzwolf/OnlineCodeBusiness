package net.ivoa.pdr.business;

import java.sql.SQLException;

import net.ivoa.oc.dao.MailConfigDAO;
import net.ivoa.pdr.commons.MailConfig;

/**
 * @author Carlo Maria Zwolf
 * Observatoire de Paris
 * LERMA
 */

public class MailConfigBusiness {
	private static final MailConfigBusiness instance = new MailConfigBusiness();

	public static MailConfigBusiness getInstance() {
		return instance;
	}

	private MailConfigBusiness() {
	}
	
	public MailConfig getMailConfig() throws SQLException, ClassNotFoundException {
		return MailConfigDAO.getInstance().getMailConfig();
	}
}
