package net.ivoa.pdr.business;

import java.sql.SQLException;
import java.util.Map;

import net.ivoa.oc.dao.ParametersDao;
import net.ivoa.pdr.commons.ParamConfiguration;

/**
 * @author Carlo Maria Zwolf
 * Observatoire de Paris
 * LERMA
 */

public class ParametersBusiness {
	private static final ParametersBusiness instance = new ParametersBusiness();

	public static ParametersBusiness getInstance() {
		return instance;
	}

	private ParametersBusiness() {
	}
	
	public Integer persistConfigurationAndGetId(ParamConfiguration configuration, Integer userId,String gridID,
			String jobNickName, Boolean MailRequested) throws SQLException, ClassNotFoundException{
		return ParametersDao.getInstance().persistConfigurationAndGetId(configuration, userId,gridID,jobNickName,MailRequested);
	}
	
	public Map<String,String> getConfigurationMap(Integer idConfiguration) throws SQLException, ClassNotFoundException{
		return ParametersDao.getInstance().getConfigurationMap(idConfiguration);
	}
}
