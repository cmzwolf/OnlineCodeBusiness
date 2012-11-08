package net.ivoa.pdr.business;

import java.sql.SQLException;

import net.ivoa.oc.dao.ServiceDao;
import net.ivoa.pdr.commons.Service;


public class ServiceBusiness {
	private static final ServiceBusiness instance = new ServiceBusiness();

	public static ServiceBusiness getInstance() {
		return instance;
	}

	private ServiceBusiness() {
	}
	
	public Service getCurrentService() throws SQLException, ClassNotFoundException{
		return ServiceDao.getInstance().getCurrentService();
	}
	

}
