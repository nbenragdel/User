/**
 * Responsabilité :
 * 
 *  ajout d'items à un fichier d'utilisateurs (XML)
 *  
 */
package fr.renater.idegest.tu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.renater.idegest.util.XPathUtil;

public class GestAjoutUsers {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(GestAjoutUsers.class);

	/**
	 * le nom du fichier des users
	 */
	private String fileName;

	/**
	 * un dico : cle=>uid, val=>User
	 */
	private Map<String, User> lesUsers;
	int i = 0;
	/**
	 * 
	 * @param fileName
	 *            le fichier des users E/S
	 * @throws Exception
	 *             en cas de pb E/S
	 */
	public GestAjoutUsers(String fileName) throws IOException {
		this.fileName = fileName;
		this.lesUsers = new HashMap<String, User>();
		loadFromFile();
	}

	/**
	 * Place la liste XML des users dans une liste en mémoire.
	 * 
	 * @throws IOException
	 */
	public void loadFromFile() throws IOException {

		lesUsers.clear();

		File f = new File(fileName);
		if (!f.exists()) {
			f.createNewFile();
			saveToFile();
		}

		URL url = f.toURI().toURL();

		String expressionXPath = "users/user";

		NodeList nodes = XPathUtil.eval(url.openStream(), expressionXPath);

		// itération sur les <user>
		for (int e = 0; e < nodes.getLength(); e++) {
			Node node = nodes.item(e);
			NodeList enfants = node.getChildNodes();
			String name = "";
			String surname = "";
			String login = "";
			String pw = "";
			for (int i = 0; i < enfants.getLength(); i++) {
				String balise = enfants.item(i).getLocalName();
				String contenu = enfants.item(i).getTextContent().trim();
				if ("prenom".equals(balise))
					surname = contenu;
				else if ("nom".equals(balise))
					name = contenu;
				else if ("uid".equals(balise))
					login = contenu;
				else if ("pw".equals(balise))
					pw = contenu;
			}
			lesUsers.put(login, new User(surname, name, login, pw));
		}
	}

	/**
	 * Enregistre les users sur disque
	 * 
	 * @throws IOException
	 */
	public void saveToFile() throws IOException {
		FileOutputStream fo = new FileOutputStream(fileName);
		PrintWriter pw = new PrintWriter(fo);
		// toString redéfini pour générer du XML
		pw.print(this.toString());
		pw.close();
	}

	/**
	 * Ajoute un utilisateur à la liste en cours
	 * 
	 * @param firstname
	 *            prénom de l'utilisateur
	 * @param name
	 *            nom de l'utilisateur
	 * @return
	 */
	public User addUser(String firstname, String name) {
		String login = genUid(firstname, name);

		while (lesUsers.containsKey(login)) {
			i++;
			login = login + i;

		}
		String pw = genPassword(8);
		User user = new User(firstname, name, login, pw);
		lesUsers.put(login, user);
		logger.trace(user);
		return user;
	}

	/*
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer res = new StringBuffer();
		res.append("<?xml version='1.0' ?>\n");
		res.append("<users>\n");

		Iterator<User> users = lesUsers.values().iterator();

		while (users.hasNext()) {
			res.append(users.next());
			res.append("\n");
		}

		res.append("</users>");
		return res.toString();
	}

	/**
	 * Génération d'un uid unique (par rapport à la liste)
	 * 
	 * @param firstname
	 *            prénom
	 * @param name
	 *            nom
	 * @return un uid
	 */
	public String genUid(String firstname, String name) {
		String uid = firstname.substring(0, 1).toLowerCase()+ name.toLowerCase();		 
		
		uid = Normalizer.normalize(uid, Normalizer.Form.NFD);
		uid = uid.replaceAll("[^\\p{ASCII}]", "");
		uid = uid.replaceAll("\\W","");
		
		int longeur2=uid.length();
		if (longeur2 > 9) {
			uid = uid.substring(0, 9);
		} else if (longeur2 < 5) {
			uid = firstname.substring(0, (1 + (5 - longeur2))).toLowerCase()
					+ name.toLowerCase();
		}

		

		return uid;
	}

	/**
	 * Génération d'un mot de passe
	 * 
	 * @param givenLen
	 *            longueur souhaitée
	 * @return un mot de passe
	 */
	public String genPassword(int givenLen) {
		{
			String chars = "0123456789azertyuiopqsdfghjklmwxcvbnAZERTYUIOPQSDFGHJKLMWXCVBN";
			String pass = "";
			for (int x = 0; x < givenLen; x++) {
				int i = (int) Math.floor(Math.random() * 62);
				pass += chars.charAt(i);
			}
			return pass;
		}

}
}