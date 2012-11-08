package net.ivoa.pdr.business;

import java.sql.SQLException;
import java.util.List;

import net.ivoa.oc.dao.InputFileDao;
import net.ivoa.pdr.commons.IOFile;

/**
 * @author Carlo Maria Zwolf
 * Observatoire de Paris
 * LERMA
 *
 */

public class InputFileBusiness {
	private static final InputFileBusiness instance = new InputFileBusiness();

	public static InputFileBusiness getInstance() {
		return instance;
	}

	private InputFileBusiness() {
	}
	
	public List<IOFile> getPatternInputFile() throws SQLException, ClassNotFoundException{
		return InputFileDao.getInstance().getInputFilesList();
	}
	
	
	
}
