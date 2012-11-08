package net.ivoa.pdr.business;

import java.sql.SQLException;
import java.util.Map;

import net.ivoa.oc.dao.RawParameterDao;

import CommonsObjects.GeneralParameter;


public class RawParameterBusiness {
	private static final RawParameterBusiness instance = new RawParameterBusiness();

	public static RawParameterBusiness getInstance() {
		return instance;
	}

	private RawParameterBusiness() {
	}
	
	public Map<String,GeneralParameter> getRawParameters() throws SQLException, ClassNotFoundException{
		return RawParameterDao.getInstance().getRawParameters();
	}
	
}
