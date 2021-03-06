package org.wipf.jasmarty.logic.telegram.messageEdit;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.wipf.jasmarty.datatypes.Telegram;
import org.wipf.jasmarty.logic.base.MainHome;
import org.wipf.jasmarty.logic.base.Wipf;
import org.wipf.jasmarty.logic.telegram.AdminUser;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class TeleMenue {

	@Inject
	Wipf wipf;
	@Inject
	TAppOthers appOthers;
	@Inject
	TAppTicTacToe appTicTacToe;
	@Inject
	TAppTodoList appTodoList;
	@Inject
	TAppEssen appEssen;
	@Inject
	TeleLog msglog;
	@Inject
	AdminUser adminUser;
	@Inject
	TAppMsg appMsg;
	@Inject
	TAppMotd appMotd;
	@Inject
	TAppFilme appFilme;

	/**
	 * @param t
	 * @return
	 */
	public String menueMsg(Telegram t) {
		// Admin Befehle
		if (adminUser.isAdminUser(t)) {
			switch (t.getMessageStringPartLow(0)) {
			case "admin":
				// @formatter:off
				return 
					"Admin Befehle:" + "\n\n" + 
					"AddAMsgToDB" + "\n" + 
					"GetAllMsg" + "\n" +
					"GetAllMotd" + "\n" +
					"AddAMotd" + "\n" +
					"SendMotd" + "\n" + 
					"SendInfo" + "\n" +
					"GetMotd" + "\n" + 
					"DelMotd ID" +  "\n" +
					"DelMsg ID"+  "\n" +
					"DoPing IP" + "\n" +
					"DoNmap" + "\n" +
					"getIp" + "\n" +
					"SendIp" + "\n" +
					"send ID msg" + "\n" +
					"Essen (Hilfe für essen)";
				// @formatter:on

			// Anbindung an msg datenbank
			case "addamsgtodb":
				return appMsg.addMsg(t);
			case "getallmsg":
				return appMsg.getAllMsg();
			case "delmsg":
				return appMsg.delMsg(t);

			// Anbindung an motd datenbank
			case "addamotd":
				return appMotd.addMotd(t);
			case "getallmotd":
				return appMotd.getAllMotd();
			case "delmotd":
				return appMotd.delMotd(t);
			case "getmotd":
				return appMotd.getRndMotd();

			case "doping":
				return wipf.ping(t.getMessageStringPartLow(1)).toString();
			case "shell":
				return wipf.shell(t.getMessageFullWithoutFirstWord());

			case "getip":
				return wipf.getExternalIp();

			default:
				break;
			}
		}
		// Alle festen Antworten
		switch (t.getMessageStringPartLow(0)) {
		case "start":
			return "Wipfbot Version:" + MainHome.VERSION + "\nInfos per 'info'";
		case "v":
		case "ver":
		case "version":
		case "info":
		case "about":
		case "hlp":
		case "hilfe":
		case "help":
		case "wipfbot":
			return "Wipfbot\nVersion " + MainHome.VERSION + "\nCreated by Tobias Fritsch\nwipf2@web.de";
		case "r":
		case "rnd":
		case "zufall":
			return appOthers.zufall(t.getMessageStringPartLow(1), t.getMessageStringPartLow(2));
		case "c":
		case "cr":
		case "en":
		case "encrypt":
			// TODO
			return wipf.encrypt(t.getMessageFullWithoutFirstWord(), "TODO_KEY");
		case "d":
		case "de":
		case "dc":
		case "decrypt":
			// TODO
			return wipf.decrypt(t.getMessageFullWithoutFirstWord(), "TODO_KEY");
		case "t":
		case "ttt":
		case "tictactoe":
		case "play":
		case "game":
			return appTicTacToe.input(t);
		case "time":
		case "date":
		case "datum":
		case "uhr":
		case "zeit":
		case "clock":
		case "z":
			return wipf.time("dd.MM.yyyy HH:mm:ss");
		case "witz":
		case "fun":
		case "w":
		case "joke":
			return appOthers.getWitz();
		case "countmsg":
			return appMsg.countMsg();
		case "countsend":
			return msglog.count();
		case "telestats":
			return wipf.time("dd.MM.yyyy HH:mm:ss") + "\n" + appMsg.countMsg() + "\n" + msglog.count();
		case "getmyid":
		case "id":
		case "whoami":
		case "pwd":
		case "me":
		case "i":
			return "From: " + t.getFrom() + "\n\nChat: " + t.getChatID() + " " + t.getType() + "\n\nM_id: "
					+ t.getMid();
		case "essen":
		case "e":
			return appEssen.menueEssen(t);
		case "to":
		case "todo":
			return appTodoList.telegramMenueTodoList(t);
		case "filme":
		case "f":
			return appFilme.telegramMenueFilme(t);
		default:
			// Alle db aktionen
			t = appMsg.getMsg(t, 0);
			// ob keine Antwort in db gefunden
			if (t.getAntwort() != null) {
				return t.getAntwort();
			} else {
				switch (wipf.getRandomInt(4)) {
				case 0:
					return "Keine Antwort vorhanden";
				case 1:
					return "Leider ist keine Antwort vorhanden";
				case 2:
					return "Hierzu gibt es keine Antwort";
				default:
					return "Antwort nicht vorhanden";
				}
			}
		}
	}

}
