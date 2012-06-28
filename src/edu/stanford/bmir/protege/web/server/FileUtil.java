package edu.stanford.bmir.protege.web.server;

public class FileUtil {
	private static String realPath;
		
	public static void init(String realPath) {
		FileUtil.realPath = realPath;
	}
	
	/**
	 * @return Real path of the webapp on the server
	 */
	public static String getRealPath() {
		return realPath;
	}	

}
