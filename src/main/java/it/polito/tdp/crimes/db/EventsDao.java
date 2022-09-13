package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.crimes.model.Arco;
import it.polito.tdp.crimes.model.Event;


public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Integer> getAllDate(){
		
		String sql ="select distinct MONTH(reported_date) as mese "
				+ "from events";
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Integer> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(res.getInt("mese"));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println("Errore");
					
			}
				
			
		}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
		
	}
	
	public List<String> getAllCategory(){
		
		String sql ="select distinct offense_category_id from events";
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<String> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
					list.add(res.getString("offense_category_id"));
				
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	public List<String> getAllVertici(String category, int mese) {
		
		String sql ="select distinct offense_type_id "
				+ "from events "
				+ "where MONTH(reported_date) = ? "
				+ "and offense_category_id = ? ";
		
		List<String> list = new ArrayList<String>() ;
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			
			st.setInt(1, mese);
			st.setString(2, category);
			
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				list.add(res.getString("offense_type_id"));
				
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
		
	}

	public List<Arco> getAllArchi(String category, int mese) {
		
		String sql ="select e1.offense_type_id as t1, e2.offense_type_id as t2, count(distinct e1.neighborhood_id) as n "
				+ "from events e1, events e2 "
				+ "where MONTH(e1.reported_date) = ? "
				+ "and e1.offense_category_id = ? "
				+ "and MONTH(e1.reported_date) = MONTH(e2.reported_date) "
				+ "and e1.offense_category_id = e2.offense_category_id "
				+ "and e1.neighborhood_id = e2.neighborhood_id "
				+ "and e1.offense_type_id > e2.offense_type_id "
				+ "group by e1.offense_type_id, e2.offense_type_id";
		
		List<Arco> list = new ArrayList<Arco>() ;
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			
			st.setInt(1, mese);
			st.setString(2, category);
			
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				list.add(new Arco(res.getString("t1"), res.getString("t2"), res.getInt("n")));
				
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}

	}

}
