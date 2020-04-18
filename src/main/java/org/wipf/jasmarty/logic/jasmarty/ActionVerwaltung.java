package org.wipf.jasmarty.logic.jasmarty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.wipf.jasmarty.datatypes.ButtonAction;
import org.wipf.jasmarty.logic.base.MsqlLite;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class ActionVerwaltung {

	private static final Logger LOGGER = Logger.getLogger("ActionVerwaltung");
	private Integer currentPressed;

	@Inject
	LcdConnect lcdConnect;

	@Inject
	PageVerwaltung pageVerwaltung;

	/**
	 * @throws SQLException
	 */
	public void initDB() {
		try {
			Statement stmt = MsqlLite.getDB();
			stmt.executeUpdate(
					"CREATE TABLE IF NOT EXISTS actions (id INTEGER UNIQUE, button INTEGER , active INTEGER , action TEXT);");
		} catch (Exception e) {
			LOGGER.error("init DB");
		}
	}

	/**
	 * @param ba
	 * @throws SQLException
	 */
	public void actionToDB(ButtonAction ba) throws SQLException {
		Statement stmt = MsqlLite.getDB();
		stmt.execute("INSERT OR REPLACE INTO actions (id, button, active, action) VALUES ('" + ba.getId() + "','"
				+ ba.getButton() + "','" + ba.isActive() + "','" + ba.getAction() + "')");
	}

	/**
	 * @param nId
	 * @return
	 */
	public ButtonAction getActionFromDbByID(int nId) {
		try {
			ButtonAction ba = new ButtonAction();

			Statement stmt = MsqlLite.getDB();
			ResultSet rs = stmt.executeQuery("SELECT * FROM actions WHERE id = '" + nId + "';");
			ba.setId(rs.getInt("id"));
			ba.setButton(rs.getInt("button"));
			ba.setActive(rs.getBoolean("active"));
			ba.setAction(rs.getString("action"));
			return ba;
		} catch (Exception e) {
			LOGGER.warn("BA not found: " + nId);
			return new ButtonAction();
		}
	}

	/**
	 * @return
	 */
	public String getAllFromDBAsJson() {
		try {
			JSONArray json = new JSONArray();

			Statement stmt = MsqlLite.getDB();
			ResultSet rs = stmt.executeQuery("select * from actions;");
			while (rs.next()) {
				JSONObject entry = new JSONObject();
				entry.put("id", rs.getInt("id"));
				entry.put("button", rs.getInt("button"));
				entry.put("active", rs.getBoolean("active"));
				entry.put("action", rs.getString("action"));
				json.put(entry);
			}
			rs.close();
			return json.toString();

		} catch (Exception e) {
			LOGGER.warn("getAll json" + e);
		}
		return "{}";
	}

	/**
	 * @param nId
	 * @return
	 */
	public ButtonAction getActionFromDbByButton(int nButton) {
		try {
			ButtonAction ba = new ButtonAction();

			Statement stmt = MsqlLite.getDB();
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM actions WHERE button = '" + nButton + "' AND active = 'true';");
			ba.setId(rs.getInt("id"));
			ba.setButton(rs.getInt("button"));
			ba.setActive(rs.getBoolean("active"));
			ba.setAction(rs.getString("action"));
			return ba;
		} catch (Exception e) {
			LOGGER.warn("BA not found by BNR: " + nButton);
			return null;
		}
	}

	/**
	 * @param nButton
	 */
	public void doAction(Integer nButton) {
		this.currentPressed = nButton;
		if (nButton != null) {

			ButtonAction ba = getActionFromDbByButton(nButton);
			if (ba != null) {

				Integer nTrennlineFirst = ba.getAction().indexOf('|');
				Integer nTrennlineLast = ba.getAction().lastIndexOf('|');

				String sParameter1 = ba.getAction().substring(0, nTrennlineFirst);
				String sParameter2 = null;
				String sParameter3 = null;

				if (nTrennlineFirst != nTrennlineLast) {
					// es gibt einen 3. Parameter
					sParameter2 = ba.getAction().substring(nTrennlineFirst + 1, nTrennlineLast);
					sParameter3 = ba.getAction().substring(nTrennlineLast + 1);
				} else {
					// Es gibt nur 2 Parameter
					sParameter2 = ba.getAction().substring(nTrennlineFirst + 1);
				}

				switch (sParameter1) {
				case "led":
					switch (sParameter2) {
					case "on":
						lcdConnect.ledOn();
						return;
					case "off":
						lcdConnect.ledOff();
						return;
					case "toggle":
						lcdConnect.ledToggle();
						return;
					}
					return;
				case "page":
					switch (sParameter2) {
					case "next":
						pageVerwaltung.nextPage();
						return;
					case "last":
						pageVerwaltung.lastPage();
						return;
					case "number":
					case "select":
					case "to":
						pageVerwaltung.selectPage(sParameter3);
						return;
					}
					return;

				default:
					LOGGER.warn("Aktion nicht verfügbar: " + sParameter1);
					return;
				}
			}
		}
	}

	/**
	 * @param jnRoot
	 */
	public void setAction(String jnRoot) {
		try {
			actionToDB(new ButtonAction().setByJson(jnRoot));
		} catch (Exception e) {
			LOGGER.warn("Convert Page fehler");
		}
	}

	/**
	 * @return
	 */
	public Integer getCurrentPressed() {
		// TODO Auto-generated method stub
		return currentPressed;
	}

}