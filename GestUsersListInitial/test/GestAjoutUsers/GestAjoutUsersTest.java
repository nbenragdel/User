package GestAjoutUsers;

import java.io.IOException;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import fr.renater.idegest.tu.GestAjoutUsers;
import fr.renater.idegest.tu.User;

public class GestAjoutUsersTest  extends TestCase {
 
		  /** l'OUT */
		  private GestAjoutUsers gau;                           // 1
		 
		  @Before
		  protected void setUp() throws Exception {
		    try {
		      this.gau = new GestAjoutUsers("testusers.xml");   // 2
		    } catch (IOException e) {
		      fail("Création de l'OUT impossible !");
		    }
		  }
		 
		  @Test
		  public void test2PremiersCarsGenUid() {
		    String uid = this.gau.genUid("Bob", "Martin");                      // 3 
		    assertTrue("Les 2 premiers caractères sont valides", uid.startsWith("bm"));  // 4
		  }
		  @Test
		  public void test2PremiersCarsGenUidBis() {
		    String uid = this.gau.genUid("Bob", "Martin");                 // 1
		    String premscar = uid.substring(0, 2);                         // 2
		    assertEquals("Les 2 premiers caractères sont valides", "bm", premscar); // 3
		  }
		  
		  @Test
		  public void test1UidMinuscule() {
		    String uid = this.gau.genUid("Bob", "Martin");                 // 1
		                             // 2
		    assertEquals("Les 2 premiers caractères sont valides", "bmartin", uid); // 3
		  }
		  
		  @Test 
		  public void test2Longeur(){
			  String uid= this.gau.genUid("Bob","Martin");
			  int Longeur=uid.length();
			  assertTrue("le longeur est valide ",(Longeur<=9 && Longeur>=5));
		  }
		 
		  @Test
		  public void test3caratereNonValable(){
			 boolean testCaractere = false;
			  String uid= this.gau.genUid("abcdef","nizar1");
			  
			  for ( int i = 0; i < uid.length(); i++ ){ 
	               if (((int) uid.charAt(i)<97) || ((int) uid.charAt(i)>122) ){
	            	   testCaractere=true;   
	               }
	              } 
			  	
			assertFalse("L'uid ne contient pas de caractères indésirables",testCaractere==false);
		  }
			@Test
			public void test4AddUser () {
			 User uid1 = gau.addUser("nizar", "ben ragdel");
			 User uid2 = gau.addUser("nizar","ben ragdel");
			assertTrue("Les UID sont differents",uid1.getLogin() != uid2.getLogin());

			}		  

			@Test
			public void test5demotdepasse () {
			User uid = gau.addUser("Bob", "Martin");
			assertTrue("le longeur de mot de passe est bien",uid.getPass().length() == 8);
			}
			@Test
			public void test6motdepassedeferent () {
			String pass1 = gau.genPassword(8);
			String pass2 = gau.genPassword(8);
			assertTrue("les deux mot de passe sont deferent",pass1 != pass2);
			}


}
