package net.ivoa.pdr.business;

import java.sql.SQLException;

import net.ivoa.oc.dao.GlobalTechConfigDAO;

/**
 * @author Carlo Maria Zwolf Observatoire de Paris LERMA
 * 
 */
public class GlobalTechConfigBusiness {
	private static final GlobalTechConfigBusiness instance = new GlobalTechConfigBusiness();

	public static GlobalTechConfigBusiness getInstance() {
		return instance;
	}

	private GlobalTechConfigBusiness() {
	}

	public String getServletContainer() throws SQLException,
			ClassNotFoundException {
		return GlobalTechConfigDAO.getInstance().getServletContainer();
	}

	public String getGWTContainer() throws SQLException, ClassNotFoundException {
		return GlobalTechConfigDAO.getInstance().getGWTContainer();
	}
}
