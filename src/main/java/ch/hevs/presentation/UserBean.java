package ch.hevs.presentation;

import ch.hevs.businessobject.User;
import ch.hevs.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@SessionScoped
@Named("userBean")
public class UserBean implements Serializable {

    @Inject
    UserService userService;

    private List<User> users;

    @PostConstruct
    public void initialize() {
        try {
            this.users = userService.getAllViewers();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
