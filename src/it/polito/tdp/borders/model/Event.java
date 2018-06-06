package it.polito.tdp.borders.model;

/*Evento è cosa fa evolvere sistema da uno stato all'altro, e ne ho uno solo:
 * il fatto che alcune persone si spostino, mi interessa sapere dove vanno
 * mi interessa sapere il punto B di destinazione*/

public class Event implements Comparable<Event> {
	
	private int t ; //tempo in cui avviene
	
	private int num ; // quante persone si spostano
	private Country destination ; // nazione di destinazione
	
	

	public Event(int t, int num, Country destination) {
		super();
		this.t = t;
		this.num = num;
		this.destination = destination;
	}



	public int getT() {
		return t;
	}



	public int getNum() {
		return num;
	}



	public Country getDestination() {
		return destination;
	}



	@Override
	public String toString() {
		return "Event [t=" + t + ", num=" + num + ", destination=" + destination + "]";
	}



	//deve confrontare i due valori di tempo, ordine crescente di t
	@Override
	public int compareTo(Event other) {
		return this.t - other.t;
	}

}