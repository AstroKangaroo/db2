package it.polimi.db2.gunalanodabasi.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the admin database table.
 * 
 */
@Entity
@Table(name = "admin", schema = "gamifiedquestionnaire")
@NamedQuery(name = "Admin.checkCredentials", query = "SELECT r FROM Admin r  WHERE r.username = ?1 and r.password = ?2")
public class Admin implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String password;

	private String username;

	public Admin() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}