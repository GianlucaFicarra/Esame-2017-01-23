package it.polito.tdp.borders.db;

import it.polito.tdp.borders.model.CoppiaNoStati;
import it.polito.tdp.borders.model.Country;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BordersDAO {
	
	//restituisce tutte le nazioni
	public List<Country> loadAllCountries() {
		
		String sql = 
				"SELECT ccode,StateAbb,StateNme " +
				"FROM country " +
				"ORDER BY StateAbb " ;

		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet rs = st.executeQuery() ;
			
			List<Country> list = new LinkedList<Country>() ;
			
			while( rs.next() ) {
				
				Country c = new Country(
						rs.getInt("ccode"),
						rs.getString("StateAbb"), 
						rs.getString("StateNme")) ;
				
				list.add(c) ;
			}
			
			conn.close() ;
			
			return list ;
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null ;
	}
	
	//restituisce lista country collegati fino all'anno passato(torna vertici grafo)
	public List<Country> getCountriesFromYear(int anno) {
		String sql = "select * from country " + //dammi i campi di country
				"where CCode in ( " +  //solo se il cod cauntry sta nell'intervallo specificato
				"select state1no " +  //query annidata, seleziona stato 1 da tabella di confinanti 
				"from contiguity " + 
				"where year<=? and conttype=1)" +//se anno minore di quello passato e confine via terra
				"order by StateNme ASC " ; 
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setInt(1, anno); //imposto anno prima di eseguire la query
			ResultSet rs = st.executeQuery() ;
			
			List<Country> list = new LinkedList<Country>() ;
			
			while( rs.next() ) {
				
				Country c = new Country(
						rs.getInt("ccode"),
						rs.getString("StateAbb"), 
						rs.getString("StateNme")) ;
				
				list.add(c) ;
			}
			
			conn.close() ;
			
			return list ;
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null ;

	}
	
	
	/*per gli archi mi faccio filtrare contiguity per farmi restituire le coppie di stati che devo collegare
	 * ovvero le coppie degli id, per poi risalire all'oggetto country dentro il mio grafo
	 * -->creo classe appositaCoppiaNoStati dove salvo i due id della coppia da collegare,
	 * al model restituisco questo oggetto contenente id degli stati adiacenti*/
	public List<CoppiaNoStati> getCoppieAdiacenti(int anno) {
		String sql = "select state1no, state2no " + 
				"from contiguity " + 
				"where year<=? " + 
				"and conttype=1 " + 
				"and state1no < state2no" ; //non torno elemento ed il suo contrario, AB BA prendo solo coppia 
		
		List<CoppiaNoStati> result = new ArrayList<>() ;
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setInt(1, anno);//setto l'anno
			
			ResultSet rs = st.executeQuery() ;
			
			while(rs.next()) { //per ogni risultato, creo oggetto COppiaNOStati e lo aggiunto
				result.add( new CoppiaNoStati(rs.getInt("state1no"), rs.getInt("state2no"))) ;
			}
			
			conn.close();
			return result ;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
		
	}
	
	
	public static void main(String[] args) {
		List<Country> list ;
		BordersDAO dao = new BordersDAO() ;
		list = dao.loadAllCountries() ;
		for(Country c: list) {
			System.out.println(c);
		}
	}
	
	
}
