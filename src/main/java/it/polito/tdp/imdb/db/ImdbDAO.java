package it.polito.tdp.imdb.db;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.*;
import it.polito.tdp.imdb.model.*;

public class ImdbDAO {
	
	public List<Actor> listAllActors(){
		String sql = "SELECT * FROM actors";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				
				result.add(actor);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public void listAllDirectors(Map<Integer, Director> idMapDirectors){
		String sql = "SELECT * FROM directors";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				idMapDirectors.put(res.getInt("id"), director);
			}
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public List<Director> listDirectorsByYear(int year, Map<Integer, Director> idMapDirectors){
		String sql = "SELECT distinct d.id as id "
				+ "FROM directors d, movies m, movies_directors md "
				+ "WHERE m.year = ? "
				+ "AND md.movie_id = m.ID AND md.director_id = d.id "
				+ "ORDER BY id";
		List<Director> result = new ArrayList<Director>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, year);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				//if(idMapDirectors.get(res.getInt("d.ID")) == null) {
					Director director = idMapDirectors.get(res.getInt("id"));
					//idMapDirectors.put(res.getInt("d.ID"), director);
					result.add(director);
					//}
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Arco> getArchi (int year, Map<Integer, Director> idMapDirectors) {
		String sql = "SELECT md1.director_id as id1, md2.director_id as id2, COUNT(DISTINCT(r1.actor_id)) AS n "
				+ "FROM roles r1, roles r2, movies_directors md1, movies_directors md2, movies m1, movies m2 "
				+ "WHERE r1.actor_id = r2.actor_id "
				+ "AND m1.id = md1.movie_id AND m2.id = md2.movie_id AND m1.year = m2.year AND m1.year = ? "
				+ "AND r1.movie_id = md1.movie_id AND r2.movie_id = md2.movie_id AND md1.director_id <> md2.director_id "
				+ "AND md1.director_id > md2.director_id "
				+ "GROUP BY md1.director_id, md2.director_id";
		List<Arco> result = new LinkedList<Arco>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, year);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Director d1 = idMapDirectors.get(rs.getInt("id1"));
				Director d2 = idMapDirectors.get(rs.getInt("id2"));
				Arco a = new Arco (d1, d2, rs.getInt("n"));
				result.add(a);
			}
			conn.close();
			return result;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	
}
