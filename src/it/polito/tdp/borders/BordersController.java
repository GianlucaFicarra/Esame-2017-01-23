/**
 * Skeleton for 'Borders.fxml' Controller Class
 */

package it.polito.tdp.borders;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.CountryAndNumber;
import it.polito.tdp.borders.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class BordersController {

	private Model model;

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="txtAnno"
	private TextField txtAnno; // Value injected by FXMLLoader

	@FXML // fx:id="boxNazione"
	private ComboBox<Country> boxNazione; // Value injected by FXMLLoader

	@FXML // fx:id="txtResult"
	private TextArea txtResult; // Value injected by FXMLLoader

	
	public void setModel(Model model) {
		this.model = model;
	}
	
	@FXML
	void doCalcolaConfini(ActionEvent event) {

		txtResult.clear();
		
		String annoS = txtAnno.getText();//prendo anno scritto da utnete
		
		try {
			int anno = Integer.parseInt(annoS);//converto anno in intero

			//se anno va bene creo grafo e mi faccio tornare lista di country e num confinanti
			model.creaGrafo(anno);

			List<CountryAndNumber> list = model.getCountryAndNumber();

			if (list.size() == 0) {
				txtResult.appendText("Non ci sono stati corrispondenti\n");
			} else {
				txtResult.appendText("Stati nell'anno "+anno+":\n");
				for (CountryAndNumber c : list) { // appendo stato e num confinanti
					txtResult.appendText(String.format("%s %d\n",
							c.getCountry().getStateName(), c.getNumber()));
				}
			}
			
			//dopo aver calcolato confini, aggiorno tendina con gli stati resenti nel grao 
			boxNazione.getItems().clear();
			boxNazione.getItems().addAll(model.getCountries()); //ordinati alfabeticamente 
			
		} catch (NumberFormatException e) { //se scrivo formato sbagliato
			txtResult.appendText("Errore di formattazione dell'anno\n");
			return;
		}

	}

	@FXML
	void doSimula(ActionEvent event) {

		//verificare che utente abbia selezionato uno stato
		Country partenza = boxNazione.getValue();

		if (partenza == null) {//null se  non ha selezionato nulla
			txtResult.appendText("ERRORE: selezionare una nazione\n");
			return;
		}

		//chiedere al modello di fare la simulazione con questo stato
		model.simula(partenza);
		
		//estraggo i risultati dalla simulazione, numero di sim e lista country and number
		int simT = model.getTsimulazione();
		List<CountryAndNumber> stanziali = model.getCountriesStanziali();

		txtResult.appendText("Simulazione dallo stato " + partenza + "\n");
		txtResult.appendText("Durata: " + simT + " passi\n");
		
		for (CountryAndNumber cn : stanziali) {
			if (cn.getNumber() != 0) { //stampo solo quelli in cui si è spostata gente
				txtResult.appendText("Nazione: " + cn.getCountry().getStateAbb() + "-" + cn.getCountry().getStateName()
						+ " Stanziali=" + cn.getNumber() + "\n");
			}
		}
	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Borders.fxml'.";
		assert boxNazione != null : "fx:id=\"boxNazione\" was not injected: check your FXML file 'Borders.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Borders.fxml'.";
	}

	

}
