package it.polimi.db2.gunalanodabasi.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the forbiddenwords database table.
 * 
 */
@Entity
@Table(name = "forbiddenwords", schema = "gamifiedquestionnaire")
@NamedQuery(name = "Forbiddenwords.findAll", query = "SELECT w FROM Forbiddenwords w")
public class Forbiddenwords implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String text;

	public Forbiddenwords() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}	

}