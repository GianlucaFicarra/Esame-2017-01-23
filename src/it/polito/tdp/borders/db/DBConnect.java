package it.polito.tdp.borders.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*DB con i confini, dico i due paesi, la tipologia di contiguita(via mare, terra, fiumi)
 *anno a partire dal quale questi sono diventati confinanti, mi interessano
 *solo i confini di terra tipo 1 */
class DBConnect {
	
	private static String url = "jdbc:mysql://localhost/countries?user=root&password=gualtieri95";

	/**
	 * Restituisce una nuova connessione, con i parametri a lui noti
	 * @return la nuova java.sql.Connection, oppure null se si verificano
	 * errori di connessione
	 */
	public static Connection getConnection() {
		Connection conn;
		try {
			conn = DriverManager.getConnection(url);
			return conn ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null ;
	}

}