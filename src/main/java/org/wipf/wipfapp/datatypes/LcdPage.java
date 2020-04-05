package org.wipf.wipfapp.datatypes;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wipf Achtung ARR x und y sind vertauscht!
 */
public class LcdPage {

	private List<String> saLines;
	private String sName;
	private int nId;
	private int nOptions;

	public LcdPage() {
		this.saLines = new ArrayList<String>();
	}

	public int getId() {
		return nId;
	}

	public void setId(int nId) {
		this.nId = nId;
	}

	public String getName() {
		return sName;
	}

	public void setName(String sPagename) {
		this.sName = sPagename;
	}

	/**
	 * @param nLine
	 * @return
	 */
	public String getLine(int nLine) {
		try {
			return this.saLines.get(nLine);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * @param nLine
	 * @return
	 */
	public char[] getLine(int nLine, int lengh) {
		String sOut = getLine(nLine);
		return sOut.substring(0, Math.min(sOut.length(), lengh)).toCharArray();
	}

	/**
	 * @param nLine
	 * @param sLine
	 */
	public void setLine(int nLine, String sLine) {
		this.saLines.add(nLine, sLine);
	}

//	public void setLine(int nLine, String sLine, boolean bMittig, int nLength) {
//		int spaces = nLength-sLine.length();
//		
//		this.saLines.add(nLine, sLine);
//	}

	/**
	 * @return
	 */
	public String getPageAsDBString() {
		StringBuilder sb = new StringBuilder();
		for (String line : saLines) {
			if (sb.length() != 0) {
				sb.append('\n');
			}
			sb.append(line);
		}
		return sb.toString();
	}

	/**
	 * @param sInput
	 */
	public void stringToPage(String sInput) {
		String[] sAr = sInput.split("\n", -1);
		int nLine = 0;
		for (String s : sAr) {
			this.saLines.add(nLine, s);
			nLine++;
		}
	}

	public int getOptions() {
		return nOptions;
	}

	public void setOptions(int nOptions) {
		this.nOptions = nOptions;
	}
}