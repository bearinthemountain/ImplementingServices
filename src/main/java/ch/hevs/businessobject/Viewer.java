package ch.hevs.businessobject;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;



@Entity
public class Viewer {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private long id;
	private String lastname;
	private String firstname;
	private String email;
	private double balance;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}



	public Viewer(){};
	public Viewer(String lastname, String firstname, String email, double balance) {
		this.lastname = lastname;
		this.firstname = firstname;
		this.email = email;
		this.balance = balance;
	}

	@Override
	public String toString() {
		String result = id + "-" + lastname + "-" + firstname;
		return result;
	}
}
