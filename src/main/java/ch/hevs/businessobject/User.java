package ch.hevs.businessobject;

import jakarta.persistence.*;

import java.math.BigDecimal;

/**
 * JPA entity representing an application user (viewer or administrator).
 * <p>
 * The table is explicitly named {@code APPLICATION_USER} because {@code USER} is a
 * reserved SQL keyword on most databases (Derby, PostgreSQL, MySQL, etc.), which would
 * cause DDL generation to fail without an explicit name override.
 * </p>
 * <p>
 * The {@code role} and {@code password} columns intentionally have no JPA mapping.
 * WildFly's login module reads them directly from SQL to handle authentication and
 * enforce the security constraints declared in {@code web.xml}. Application code should
 * check roles via {@code HttpServletRequest.isUserInRole()} or {@code @RolesAllowed}
 * rather than exposing credentials through this entity.
 * </p>
 */
@Entity
@Table(name = "APPLICATION_USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private long id;

    private String lastname;
    private String firstname;

    /** Current account balance in CHF, debited on each successful rental. */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal balance;


    /** Default no-arg constructor required by JPA. */
    public User() {}

    /**
     * Creates a fully initialised user.
     *
     * @param lastname  family name
     * @param firstname given name
     * @param balance   initial account balance in CHF
     */
    public User(String lastname, String firstname, BigDecimal balance) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.balance = balance;
    }

    /** @return the current account balance in CHF */
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    /** @return the database primary key */
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }

    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }

    @Override
    public String toString() {
        return id + "-" + lastname + "-" + firstname;
    }
}
