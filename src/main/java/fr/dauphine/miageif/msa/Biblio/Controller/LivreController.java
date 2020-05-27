package fr.dauphine.miageif.msa.Biblio.Controller;

import fr.dauphine.miageif.msa.Biblio.Livre;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import fr.dauphine.miageif.msa.Biblio.Repository.LivreRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@Transactional
public class LivreController {

    @Autowired
    private Environment environment;

    @Autowired
    private LivreRepository repository;

    @GetMapping("livres/isbn/{isbn}")
    public Livre findByIsbn(@PathVariable String isbn){
        Livre livre = repository.findByIsbn(isbn);
        return livre;
    }

    @GetMapping("livres/auteur/{auteur}")
    public List<Livre> findByAuteur(@PathVariable String auteur){
        List<Livre> livres = repository.findAllByAuteur(auteur);
        return livres;
    }

    @DeleteMapping("livres/isbn/{isbn}")
    public String deleteByIsbn(@PathVariable String isbn){
        repository.deleteByIsbn(isbn);
        return "Le livre ayant l'ISBN "+isbn+" a été supprimé de la base de données";
    }


    //HEADER : 'Content-type: application/json'
    @PutMapping("livres/isbn/{isbn}")
    public String updateLivre(@RequestBody Livre livre, @PathVariable String isbn) {
        if (!repository.existsByIsbn(isbn)){
            return "Le livre n'existe pas dans la base de données !";
        }else{
            Livre livreEnBase = repository.findByIsbn(isbn);
            if(livre.getAuteur() == null){
                livre.setAuteur(livreEnBase.getAuteur());
            }
            if(livre.getEditeur() == null){
                livre.setEditeur(livreEnBase.getEditeur());
            }
            if(livre.getEdition() == null){
                livre.setEdition(livreEnBase.getEdition());
            }
            if(livre.getTitre() == null){
                livre.setTitre(livreEnBase.getTitre());
            }
            repository.save(livre);
            return "Le livre ayant l'ISBN : "+isbn+" a été mis à jour avec succès";
        }




    }

    @PostMapping("livres/")
    public String addLivre(@RequestBody Livre livre){
        if (repository.existsByIsbn(livre.getIsbn()))
            return "Le livre existe déjà dans la base de données !";

        repository.save(livre);
        return "Le livre a été enregistré avec succès";
    }


}
