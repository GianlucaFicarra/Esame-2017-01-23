package it.polito.tdp.borders.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;


/*PUNTO 2: modello si avvale di simulatore, non lo implemento dentro model
 per implementare il simulatore mi faccio le solide domande di sempre:*/
public class Simulator {
	
	// Tipi di eventi / coda prioritaria degli eventi (PARTE DINAMICA)
	PriorityQueue<Event> queue ;
	
	// Modello del mondo (PARTE STATICA)
	//---->fotografo situazione vedo elenco stati e numero che mi dice quante persne ho stanziali li
	private Map<Country,Integer> stanziali ;
	private Graph graph ; //anche il grafo salvato nel modello per valutare gli stati adiacenti
	
	// Parametri di simulazione
	private int N_MIGRANTI = 1000 ; //num di persone iniettate al tempo 0, poi ne avro 1000 nei vari stati
	
	// Valori in output
	private int T ; // passi di simulazione
	
	
	
	//metodo per inizializzarlo, chiamato costruttore o init
	//dovro dargli il grafo e il paese iniziale da cui partire
	public void init(Graph<Country, DefaultEdge> graph, Country partenza) {
		
		//preimposto le strutture dati che serviranno,
		//le costruisco nel init così ogni volta cancello tutto e ricomincio con strutture pulite
		
		this.T = 1 ; //azzero il tempo al tempo iniziale
		
		//azzera la mappa degli stanziali
		this.stanziali = new HashMap<Country, Integer>() ; //creo nuova mappa da popolare
		for(Country c: graph.vertexSet()) //per ogni paese del mio grafo 
			this.stanziali.put(c, 0) ; //aggiungo elemento con stato e valore 0
		
		//inizzializzo coda degli eventi (event già ordinato)
		this.queue = new PriorityQueue<Event>() ;
		
		//--> aggiungo nella coda l'elemento iniziale
		this.queue.add(new Event(T, N_MIGRANTI, partenza)) ;
		
		//il grafo mi serve ancora per valutare gli stati adiacenti, devo salvare nel modello mondo il grafo
		this.graph = graph ;
	}
	
	
	public void run() {
		
		Event e ;
		while( (e=queue.poll())!=null) { //lancio simulazione che termina quando finisce coda eventi
			
			//SOLITAMENTE HO SWITCH ED EVENT TYPE, QUI NE HO 1 SOLO!!!
			
			this.T = e.getT() ; //aggiorno qui sempre il tempo estratto, alla fine avrà il valore finale più alto
			
			int arrivi = e.getNum() ; //salvo arrivi
			Country stato = e.getDestination() ; //salvo lo stato (tutti hanno almeno 1 confinante per come fatto nel punto 1)
			
			//li arrivi li devo dividere, metà resta l'altra va via dividendosi negli stati confinanti
			//vado a vedere quali siano gli stati confinanti, li chiedo al grafo:
			
			List<Country> confinanti = Graphs.neighborListOf(graph, stato) ;
			  //se grafo non orientato Graphs.neighborListOf dato grafo e vertice, ci da lista di vertici 
			  //se orientatoGraphs.predecessorListOf(arg0, arg1) e Graphs.successorListOf(arg0, arg1)
			
			//calcolo quante persone devono migrare negli stati confinanti
			int migranti = (arrivi / 2) / confinanti.size() ; //metà arrivati divisi tra i confinanti con approx all'esremo inferiore
			
			if(migranti!=0) { //se da 0 non-stanziali < num stati confinanti, non si spostano
				for(Country arrivo: confinanti) { //creo eventi di arrivo in tutti gli stati confinanti
					queue.add(new Event(e.getT()+1, migranti, arrivo)) ;
				}
			}
			
			//persone rimaste ferme, sono gli arrivati meno quelli che si sono spostati
			//arrivo 100, 2 confinanti--> rimasti 100-25*2
			int rimasti = arrivi - migranti * confinanti.size() ;
			
			//i miei stanziali nello stato sono quelli che avevo + i rimasti
			this.stanziali.put(stato, this.stanziali.get(stato)+rimasti) ;	
		}
	}

	//parametri in output
	public Map<Country, Integer> getStanziali() { //mappa stanziati per ogni stato
		return stanziali;
	}

	//num di passi di simulazione, è il max vaore di T, essendo per T crescenti sarà l'ultimo
	public int getT() {
		return T;
	}
	
}