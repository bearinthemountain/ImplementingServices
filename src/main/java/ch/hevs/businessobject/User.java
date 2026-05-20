package ch.hevs.businessobject;

import jakarta.persistence.*;

// 1. SUPPRESSION des anciens imports inutiles/faux (javax.swing et javax.management)

@Entity
@Table(name = "APPLICATION_USER") // Optionnel mais fortement recommandé car "USER" est un mot réservé en SQL
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private long id;
	private String lastname;
	private String firstname;
	private String email;
	private double balance;

	// Si tu n'as pas créé d'objet ou d'Enum 'Role', utilise simplement un String !
	// C'est le plus simple pour débuter et stocker "client", "manager", etc.
	private String role;

	// Variable String pour stocker le mot de passe haché sécurisé
	private String password;

	// --- CONSTRUCTEURS ---

	public User() {}

	// Mise à jour du constructeur pour accepter le mot de passe et le rôle à la création [cite: 46]
	public User(String lastname, String firstname, String email, double balance, String password, String role) {
		this.lastname = lastname;
		this.firstname = firstname;
		this.email = email;
		this.balance = balance;
		this.password = password; // Correction : On affecte le paramètre reçu
		this.role = role;         // Correction : On affecte le paramètre reçu
	}

	// --- GETTERS ET SETTERS ---

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

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

	@Override
	public String toString() {
		return id + "-" + lastname + "-" + firstname + " (" + role + ")";
	}
}