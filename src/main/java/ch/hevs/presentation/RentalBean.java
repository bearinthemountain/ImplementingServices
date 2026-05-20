package ch.hevs.presentation;

import java.io.Serializable;
import java.util.List;

import ch.hevs.businessobject.User;
import ch.hevs.service.MediaService;
import ch.hevs.service.RentalService;
import ch.hevs.service.UserService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@SessionScoped
@Named("rentalBean")
public class RentalBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<User> users;

    private Long selectedViewerId;
    private Long selectedMediaId;

    private String transactionResult;

    @Inject
    private RentalService rentalService;

    @Inject
    private MediaService mediaService;

    @Inject
    private UserService userService;




    public String performRental() {
        try {
            if (selectedViewerId == null || selectedMediaId == null) {
                this.transactionResult = "Erreur : Veuillez sélectionner un spectateur et un média.";
                return "showRentalResult";
            }
            rentalService.rentMedia(selectedViewerId, selectedMediaId);
            mediaService.refreshMedia();
            this.transactionResult = "Location effectuée avec succès ! Bon visionnage.";

        } catch (Exception e) {
            this.transactionResult = "Échec de la location : " + e.getMessage();
        }

        return "showRentalResult";
    }






    public List<User> getViewers() { return users; }

    public Long getSelectedViewerId() { return selectedViewerId; }
    public void setSelectedViewerId(Long selectedViewerId) { this.selectedViewerId = selectedViewerId; }

    public Long getSelectedMediaId() { return selectedMediaId; }
    public void setSelectedMediaId(Long selectedMediaId) { this.selectedMediaId = selectedMediaId; }

    public String getTransactionResult() { return transactionResult; }
    public void setTransactionResult(String transactionResult) { this.transactionResult = transactionResult; }
}