package com.testHibernate.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.testHibernate.converts.diplome.DiplomeToDiplomeForm;
import com.testHibernate.model.diplome.ListesDiplome;
import com.testHibernate.model.diplome.ListesDiplomeForm;
import com.testHibernate.model.diplome.NiveauDiplome;
import com.testHibernate.service.diplome.ListesDiplomeService;
import com.testHibernate.service.diplome.NiveauDiplomeService;

@Controller
public class FicheDemandeController {
	 private ListesDiplomeService listesDiplomeService;
	 private NiveauDiplomeService niveauDiplomeService;
	 private DiplomeToDiplomeForm diplomeToDiplomeForm;
	 
	 @Autowired
	 public void setDiplomeToDiplomeForm(DiplomeToDiplomeForm diplomeToDiplomeForm) {
		this.diplomeToDiplomeForm = diplomeToDiplomeForm;
	 }
	 
	 @Autowired
	 public void setNiveauDiplomeService(NiveauDiplomeService niveauDiplomeService) {
		this.niveauDiplomeService = niveauDiplomeService;
	 }

	 @Autowired
	 public void setListesDiplomeService(ListesDiplomeService listesDiplomeService) {
        this.listesDiplomeService = listesDiplomeService;
	 }
	 
	 @GetMapping({"/diplomaList", "/diplomes"})
	 public String listDiplome(Model model){
		List<ListesDiplome> ret = listesDiplomeService.listAll();
		List<NiveauDiplome> nivaux = niveauDiplomeService.listAll();
        model.addAttribute("listesDiplome", ret);
        model.addAttribute("niveaux", nivaux);
       // System.out.println("\n ret.Length = " + ret.size());
        return "pages/enregistrement/diplomaList";
	 }	
	 
	 @GetMapping("/showDiploma/{id}")
	 public String getDiploma(@PathVariable String id, Model model){
		 model.addAttribute("diploma", listesDiplomeService.getById(Long.valueOf(id)));
		// System.out.println("GEGE");
		 return "pages/enregistrement/showDiploma";
	 }

	 @GetMapping("/editDiploma/{id}")
	 public String edit(@PathVariable String id, Model model){
        ListesDiplome liste = listesDiplomeService.getById(Long.valueOf(id));
        ListesDiplomeForm listesDiplome = diplomeToDiplomeForm.convert(liste);

		List<ListesDiplome> listeDiploma = listesDiplomeService.listAll();
        List<NiveauDiplome> niveauxDiploma = niveauDiplomeService.listAll();
        model.addAttribute("listNiveauDiploma", niveauxDiploma);
        model.addAttribute("listDiploma", listeDiploma);
        model.addAttribute("listesDiplome", listesDiplome);
        model.addAttribute("isEdit", "1");
        return "pages/enregistrement/newDiploma";
	 }
	
	 @GetMapping("/newDiploma")
	 public String ajouterDiplome(Model model) {
		 //initial
		// Map<String, String> listNiveau = new HashMap<String, String>();
		 
		 //Get Lists
		 List<ListesDiplome> listeDiploma = listesDiplomeService.listAll();
		 List<NiveauDiplome> niveauxDiploma = niveauDiplomeService.listAll();
		 
		 //traitement	
		 /*for(NiveauDiplome niv : niveauxDiploma) {
			 listNiveau.put(niv.getCategorie(), value);
		 }*/
		 model.addAttribute("listDiploma", listeDiploma);
		 model.addAttribute("listNiveauDiploma", niveauxDiploma);
		 model.addAttribute("listesDiplome", new ListesDiplomeForm());
		 
		 return "pages/enregistrement/newDiploma";		
	 }
	
	 public List<NiveauDiplome> getNiveauByCateg(String categ){
		 return this.niveauDiplomeService.findNiveauByCategorie(categ);
	 }
	 
	 @PostMapping(value = "/saveDiploma")
	 public String saveOrUpdateDiploma(@Valid  @ModelAttribute ListesDiplomeForm listesDiplome, BindingResult bindingResult){
		 
		 if(bindingResult.hasErrors()){
			 return "pages/enregistrement/newDiploma";
		 }
		 
	
		 ListesDiplome listesSaved = listesDiplomeService.saveOrUpdateListesDiplomeForm(listesDiplome);

		 return "redirect:/showDiploma/" + listesSaved.getId();
	 }
	 
	@GetMapping("/diploma/delete/{id}")
	 public String delete(@PathVariable String id){
		listesDiplomeService.delete(Long.valueOf(id));
        return "redirect:/diplomaList";
	 }
	

}