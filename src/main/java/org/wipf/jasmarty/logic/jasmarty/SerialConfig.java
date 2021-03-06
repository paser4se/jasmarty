package org.wipf.jasmarty.logic.jasmarty;

import java.sql.Statement;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.wipf.jasmarty.datatypes.LcdConfig;
import org.wipf.jasmarty.logic.base.SqlLite;

import com.fazecast.jSerialComm.SerialPort;

@ApplicationScoped
public class SerialConfig {

	private static final Logger LOGGER = Logger.getLogger("SerialConfig");

	/**
	 * @return
	 */
	public LcdConfig getConfig() {
		try {
			LcdConfig conf = new LcdConfig();
			Statement stmt = SqlLite.getDB();
			conf.setPort(stmt.executeQuery("SELECT val FROM config WHERE key IS 'port';").getString("val"));
			conf.setRefreshRate(stmt.executeQuery("SELECT val FROM config WHERE key IS 'refreshrate';").getInt("val"));
			conf.setWidth(stmt.executeQuery("SELECT val FROM config WHERE key IS 'widht';").getInt("val"));
			conf.setHeight(stmt.executeQuery("SELECT val FROM config WHERE key IS 'height';").getInt("val"));
			conf.setBaudRate(stmt.executeQuery("SELECT val FROM config WHERE key IS 'baudrate';").getInt("val"));
			stmt.close();
			return conf;
		} catch (Exception e) {
			LOGGER.warn("Config nicht gefunden > default Config erstellen");
			return defaultConfig();
		}
	}

	/**
	 * @return
	 */
	private LcdConfig defaultConfig() {
		LcdConfig lcDef = new LcdConfig();
		lcDef.setPort("");
		lcDef.setHeight(4);
		lcDef.setWidth(20);
		lcDef.setBaudRate(9600);
		lcDef.setRefreshRate(200);

		if (!setConfig(lcDef)) {
			LOGGER.warn("Config konnte nicht gespeichert werden!");
		}
		return lcDef;
	}

	/**
	 * @param conf
	 * @return
	 */
	public boolean setConfig(LcdConfig conf) {
		try {
			Statement stmt = SqlLite.getDB();
			stmt.execute("INSERT OR REPLACE INTO config (key, val) VALUES ('port','" + conf.getPort() + "')");
			stmt.execute(
					"INSERT OR REPLACE INTO config (key, val) VALUES ('refreshrate','" + conf.getRefreshRate() + "')");
			stmt.execute("INSERT OR REPLACE INTO config (key, val) VALUES ('widht','" + conf.getWidth() + "')");
			stmt.execute("INSERT OR REPLACE INTO config (key, val) VALUES ('height','" + conf.getHeight() + "')");
			stmt.execute("INSERT OR REPLACE INTO config (key, val) VALUES ('baudrate','" + conf.getBaudRate() + "')");
			LOGGER.info("Config gespeichert");
			stmt.close();
			return true;
		} catch (Exception e) {
			LOGGER.warn("Config konnte nicht gespeichert werden!");
			return false;
		}
	}

	/**
	 * @param jnRoot
	 * @return
	 */
	public boolean setConfig(String jnRoot) {
		return setConfig(new LcdConfig().setByJson(jnRoot));
	}

	/**
	 * @return
	 */
	public JSONObject getPorts() {
		SerialPort[] spa = SerialPort.getCommPorts();

		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();
		for (SerialPort item : spa) {
			JSONObject jItem = new JSONObject();

			jItem.put("name", item.getDescriptivePortName());
			ja.put(jItem);
		}
		jo.put("list", ja);
		return jo;
	}

}
