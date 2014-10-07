package net.ivoa.pdr.business;

import java.sql.SQLException;
import java.util.List;

import net.ivoa.oc.dao.PurgeDAO;

/**
 * @author Carlo Maria Zwolf
 * Observatoire de Paris
 * LERMA
 */

public class PurgeBusiness {
	private static final PurgeBusiness instance = new PurgeBusiness();

	public static PurgeBusiness getInstance() {
		return instance;
	}

	private PurgeBusiness() {
	}
	
	public List<Integer> getIdJobsToOld() throws SQLException,
	ClassNotFoundException {
		return PurgeDAO.getInstance().getIdJobsToOld();
	}
	
	public void deleteJobsFromListIds(List<Integer> idToDelete) throws SQLException, ClassNotFoundException{
		PurgeDAO.getInstance().removeJobsFromDB(idToDelete);
	}
	
	
}
