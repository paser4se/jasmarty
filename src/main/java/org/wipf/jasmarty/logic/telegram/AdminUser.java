package org.wipf.jasmarty.logic.telegram;

import javax.enterprise.context.ApplicationScoped;

import org.wipf.jasmarty.datatypes.Telegram;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class AdminUser {

	/**
	 * TODO ids zu db
	 * 
	 * @param t
	 * @return
	 */
	public Boolean isAdminUser(Telegram t) {
		return (t.getChatID() == 798200105 || t.getChatID() == 522467648);
	}

}
