package it.polito.tdp.borders.model;


/*classe creata perche devo restituire al controller lista di oggetti
 * che contengano country e numero di stti ad esso confinanti
 * ordinati per numero decrescente di confinanti */
public class CountryAndNumber implements Comparable<CountryAndNumber> {
	
	private Country country ;
	private int number ;
	
	public CountryAndNumber(Country country, int number) {
		super();
		this.country = country;
		this.number = number;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	@Override
	public int compareTo(CountryAndNumber other) {
		// ordine DECRESCENTE per numero
		
		return -(this.number-other.number);
	}
	
	

}
