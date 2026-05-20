package ch.hevs.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;

@ApplicationScoped
@DatabaseIdentityStoreDefinition(
        dataSourceLookup = "java:/labsDS", // <--- On utilise exactement le jta-data-source de ton MarmottePU
        callerQuery = "select password from APPLICATION_USER where email = ?",
        groupsQuery = "select role from APPLICATION_USER where email = ?",
        hashAlgorithm = Pbkdf2PasswordHash.class
)
public class SecurityConfig {
    // Rien d'autre à écrire ici. Jakarta Security va se lier tout seul à java:/labsDS !
}