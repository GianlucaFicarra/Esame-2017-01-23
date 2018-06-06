package it.polito.tdp.borders.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {
	
	              //vertice, arco
	private Graph<Country, DefaultEdge> graph ; //grafo non orientato e pesato=Simple
	private List<Country> countries ; //lista di vertici
	private Map<Integer,Country> countriesMap ; //ad ogni id corrisponde lo stato
	
   //salvo simu perchè dopo che lo creo si vorrà sapere come è andata la simulazione
	private Simulator sim ;
	
	public Model() {
			}
	
	//dato valore di un anno ne creo grafo
	public void creaGrafo(int anno) {
		
		/*chiamo grafo da zero qui ogni volta che invoco il metodo
		 * se lo creassi nel model aggiungerei vertici a grafo che già
		 * aveva vertici, nel caso di più chiamate successive*/
		this.graph = new SimpleGraph<>(DefaultEdge.class) ;

		//collegamento dal dao perchiedergli la lista di vertici dato anno
		BordersDAO dao = new BordersDAO() ;
		
		//vertici
		this.countries = dao.getCountriesFromYear(anno) ;
		
		//creo mappa id/country per ogni country filtrato dal dao
		this.countriesMap = new HashMap<>() ;
		for(Country c: this.countries) { //se la chiamassero più funzioni converebbe farlo nel dao
			this.countriesMap.put(c.getcCode(), c) ;
		}
		
		//aggiungo i vertici al grafo
		Graphs.addAllVertices(graph, this.countries) ;
		
		// archi
		List<CoppiaNoStati> archi = dao.getCoppieAdiacenti(anno) ; //ogni elemento è una coppia
		for( CoppiaNoStati c: archi) { //per ogni elemento della lista(coppia) crea arco
			
			//elementi coppie sono id, io li devo convertire in country tramite la mappaCountries
			graph.addEdge(this.countriesMap.get(c.getState1no()), 
					this.countriesMap.get(c.getState2no())) ;
			
		}
		
		//stampa di debug
//		System.out.format("Grafo creato con %d vertici e %d archi\n", 
//				this.graph.vertexSet().size(),
//				this.graph.edgeSet().size()) ;
		
	}
	
	
	//metodo che mi dia lista dei country con il numero di confinanti
	public List<CountryAndNumber> getCountryAndNumber() {
		List<CountryAndNumber> list = new ArrayList<>() ;
		
		//popolo lista con i vertici del grafo, + il num dei vertici adiacenti
		for(Country c: graph.vertexSet()) {
			list.add(new CountryAndNumber(c, graph.degreeOf(c))) ;
		}                 //opp Graphs.neighborList(graph, c).size()
		
		Collections.sort(list);//ordino la lista
		return list ;
	}
	

	//per popolare il menu a tendina prendo i valori da qui, ottenuti dal dao
   //la voglio 0rdinata cosi quando creo country in DAO aggiungo ordine alfabetico: "order by StateNme ASC "
	public List<Country> getCountries() {
		return countries;
	}

	
	//metodi passacarte tra dao e controller
	public void simula(Country partenza) {//lancio simulazione apposita per ogni invocazione
		
		this.sim = new Simulator() ; //creo simulatore
		
		this.sim.init(this.graph, partenza); //inizializzo e avvia
		this.sim.run();
		
	}

	public int getTsimulazione() {
		return this.sim.getT(); //tempo simulazione la chiedo al sim
	}

	public List<CountryAndNumber> getCountriesStanziali() {
		//chiedo al simulatore la mappa get stanziali
		Map<Country, Integer> mappa = this.sim.getStanziali() ;
		List<CountryAndNumber> result = new ArrayList<>() ;
		
		// Converti la mappa <Country,Integer> in una lista di CountryAndNumber
		for(Country c: mappa.keySet()) {
			result.add(new CountryAndNumber(c, mappa.get(c))) ;
		}
		
		// Ordina la lista per valori DECRESCENTI di number
		//COMPARATORE AL VOLO:
		Collections.sort(result, new Comparator<CountryAndNumber>() {

			@Override
			public int compare(CountryAndNumber o1, CountryAndNumber o2) {
				return o2.getNumber()-o1.getNumber();//decrescente
			}
			
		});
		
		return result;
	}
	

}
