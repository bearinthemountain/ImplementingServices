package ch.hevs.presentation;

import java.io.Serializable;
import java.util.List;

import ch.hevs.businessobject.Viewer;
import ch.hevs.businessobject.Media;
import ch.hevs.service.RentalService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@SessionScoped
@Named("rentalBean")
public class RentalBean implements Serializable {

    private static final long serialVersionUID = 1L;

    // Listes complètes d'objets pour alimenter les composants de l'interface XHTML
    private List<Viewer> viewers;
    private List<Media> medias;

    // Variables pour stocker les IDs sélectionnés par l'utilisateur dans les menus déroulants
    private Long selectedViewerId;
    private Long selectedMediaId;

    // Chaîne de caractères pour afficher le message de succès ou d'erreur sur le site
    private String transactionResult;

    @Inject
    private RentalService rentalService;

    @PostConstruct
    public void initialize() {
        try {
            // 1. On récupère tous les médias via votre méthode métier existante
            this.medias = rentalService.getAllMedia();

            // 2. /!\ Attention : Assurez-vous d'avoir créé ou d'ajouter cette méthode "getAllViewers"
            // dans votre RentalService pour récupérer la liste des spectateurs.
            this.viewers = rentalService.getAllViewers();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Méthode action déclenchée par le bouton de validation de la vue XHTML
     */
    public String performRental() {
        try {
            // Vérification élémentaire côté présentation
            if (selectedViewerId == null || selectedMediaId == null) {
                this.transactionResult = "Erreur : Veuillez sélectionner un spectateur et un média.";
                return "showRentalResult";
            }

            // Appel de votre business logic transactionnelle !
            rentalService.rentMedia(selectedViewerId, selectedMediaId);

            // Si aucune exception n'a été levée par la logique métier :
            this.transactionResult = "Location effectuée avec succès ! Bon visionnage.";

        } catch (Exception e) {
            // Si le solde est insuffisant ou le média déjà loué, l'exception est capturée ici
            this.transactionResult = "Échec de la location : " + e.getMessage();
        }

        // Redirige vers la page de résultat JSF (ex: showRentalResult.xhtml)
        return "showRentalResult";
    }

    // --- GETTERS & SETTERS (Obligatoires pour la liaison avec les balises XHTML) ---

    public List<Viewer> getViewers() { return viewers; }
    public List<Media> getMedias() { return medias; }

    public Long getSelectedViewerId() { return selectedViewerId; }
    public void setSelectedViewerId(Long selectedViewerId) { this.selectedViewerId = selectedViewerId; }

    public Long getSelectedMediaId() { return selectedMediaId; }
    public void setSelectedMediaId(Long selectedMediaId) { this.selectedMediaId = selectedMediaId; }

    public String getTransactionResult() { return transactionResult; }
    public void setTransactionResult(String transactionResult) { this.transactionResult = transactionResult; }
}