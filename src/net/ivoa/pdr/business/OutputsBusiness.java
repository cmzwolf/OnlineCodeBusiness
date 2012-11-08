package net.ivoa.pdr.business;

import java.sql.SQLException;
import java.util.List;

import net.ivoa.oc.dao.OutputsDAO;
import net.ivoa.pdr.commons.IOFile;

/**
 * @author Carlo Maria Zwolf
 * Observatoire de Paris
 * LERMA
 */

public class OutputsBusiness {
	private static final OutputsBusiness instance = new OutputsBusiness();

	public static OutputsBusiness getInstance() {
		return instance;
	}

	private OutputsBusiness() {
	}

	public List<IOFile> getPatternInputFile() throws SQLException,
			ClassNotFoundException {
		return OutputsDAO.getInstance().getOutputsFilesList();
	}
}
