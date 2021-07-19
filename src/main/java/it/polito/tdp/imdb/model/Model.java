package it.polito.tdp.imdb.model;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import it.polito.tdp.imdb.db.*;
import java.util.*;

public class Model {
	
	private SimpleWeightedGraph<Director, DefaultWeightedEdge> graph;
	private ImdbDAO dao;
	private Map<Integer, Director> idMapDirectors;
	
	public Model() {
		dao = new ImdbDAO();
		idMapDirectors = new HashMap<>();
		dao.listAllDirectors(idMapDirectors);
	}
	
	public void creaGrafo(int year) {
		graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//vertici
		Graphs.addAllVertices(graph, dao.listDirectorsByYear(year, idMapDirectors));
		
		//archi
		for(Arco a : dao.getArchi(year, idMapDirectors)) {
			if(this.graph.containsVertex(a.getD1()) && this.graph.containsVertex(a.getD2())) {
				DefaultWeightedEdge e = this.graph.getEdge(a.getD1(), a.getD2());
				if(e == null) {
					Graphs.addEdge(graph, a.getD1(), a.getD2(), a.getPeso());
				}
			}
		}
		System.out.println("#vertici: "+graph.vertexSet().size()+"\n#archi: "+graph.edgeSet().size());
	}
	
	public List<Adiacenti> getAdiacenti(Director selezionato, int year) {
		List<Adiacenti> list = new LinkedList<>();
		for (Arco a : dao.getArchi(year, idMapDirectors)) {
			Adiacenti adiacente = new Adiacenti(null, a.getPeso());
			if(a.getD1().equals(selezionato)) {
				adiacente.setD(a.getD2());
				list.add(adiacente);
			}
			else if (a.getD2().equals(selezionato)){
				adiacente.setD(a.getD1());
				list.add(adiacente);
			}
		}
		return list;
	}
	
	public Set<Director> listaRegisti() {
		return this.graph.vertexSet();
	}
	
	
}
