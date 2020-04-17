package org.wipf.jasmarty.datatypes;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author wipf Achtung ARR x und y sind vertauscht!
 */
public class LcdPage {

	private List<String> saLines;
	private String sName;
	private String sOptions;
	private int nId;

	public LcdPage() {
		this.saLines = new ArrayList<String>();
	}

//	public void setLine(int nLine, String sLine, boolean bMittig, int nLength) {
//	int spaces = nLength-sLine.length();
//	
//	this.saLines.add(nLine, sLine);
//}

	/**
	 * @param jnRoot
	 * @return
	 */
	public LcdPage setByJson(String jnRoot) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jn;
			jn = mapper.readTree(jnRoot);

			this.sName = jn.get("name").asText();
			this.sOptions = jn.get("options").asText();
			this.nId = jn.get("id").asInt();
			setStringToPage(jn.get("lines").asText());
			return this;
		} catch (Exception e) {
			return null;
		}
	}

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
	public void setStringToPage(String sInput) {
		String[] sAr = sInput.split("\n", -1);
		int nLine = 0;
		for (String s : sAr) {
			this.saLines.add(nLine, s);
			nLine++;
		}
	}

	/**
	 * @return
	 */
	public String toJson() {
		JSONObject jo = new JSONObject();
		jo.put("name", sName);
		jo.put("id", nId);
		jo.put("options", sOptions);
		jo.put("lines", getPageAsDBString());
		return jo.toString();
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
	 * @param sLine
	 */
	public void setLine(int nLine, String sLine) {
		this.saLines.add(nLine, sLine);
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

	public String getOptions() {
		return sOptions;
	}

	public void setOptions(String sOptions) {
		this.sOptions = sOptions;
	}

}
