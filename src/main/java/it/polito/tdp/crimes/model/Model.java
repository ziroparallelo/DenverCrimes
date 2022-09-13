package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private EventsDao dao;
	private Graph< String, DefaultWeightedEdge> grafo;
	private List<Arco> archi;
	private List<String> vertici;
	
	private List<String> best;
	private String vertFinale;
	
	public Model() {
		this.dao = new EventsDao();
		
	}
	
	public void creaGrafo(String category, int mese) {
		
		this.grafo = new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		this.vertici = dao.getAllVertici(category, mese);
		
		Graphs.addAllVertices(this.grafo, vertici);
		
		this.archi = dao.getAllArchi(category, mese);
		
		for(Arco a : archi) {
			Graphs.addEdgeWithVertices(this.grafo, a.getCrime1(), a.getCrime2(), a.getPeso());
		}
	}
	
	public List<Integer> getAllData(){
		
		List<Integer> mesi = dao.getAllDate();
		Collections.sort(mesi);
		
		return mesi;
	}
	
	public List<String> getAllCategories(){
		
		List<String> categorie = dao.getAllCategory();
		
		Collections.sort(categorie);
		return categorie;
	}

	public int getNvert() {
		return this.grafo.vertexSet().size();
	}

	public int getNarc() {
		return this.grafo.edgeSet().size();
	}

	public List<Arco> getArchiSup() {
		
		int somma = 0;
		for(DefaultWeightedEdge e: this.grafo.edgeSet()) {
			somma += this.grafo.getEdgeWeight(e);
		}
		
		float media = ((float)somma)/(this.grafo.edgeSet().size());
		
		List<Arco> archiSup = new ArrayList<Arco>();
		
		for(Arco a : this.archi) {
			if(a.getPeso() > media) {
				archiSup.add(a);
			}
		}
		return archiSup;
	}

	public List<Arco> getArchi() {
		
		return this.archi;
	}
	
	public List<String> getVertici(){
		
		return this.vertici;
	}
	
	public List<String> calcolaPercorso(Arco a){
		
		String v1 = a.getCrime1();
		String v2 = a.getCrime2();
		
		this.vertFinale = v2;
		this.best = new ArrayList<String>();
		
		List<String> parziale = new ArrayList<String>();
		parziale.add(v1);
		
		ricorsione_grafo(parziale, v2);

		
		return this.best;
	}

	private void ricorsione_grafo(List<String> parziale, String destinazione) {
		
		//Caso finale
		if(parziale.get(parziale.size()-1).equals(destinazione)) {
			if(parziale.size()>this.best.size()) {
				this.best = new ArrayList<String>(parziale);
			}
			return;
		}
		
		//Caso normale
		
		//Crea nuova soluzione
		for(String v: Graphs.neighborListOf(this.grafo,
				parziale.get(parziale.size()-1))) {
			if(!parziale.contains(v)) {
				
				parziale.add(v);
				ricorsione_grafo(parziale, v);
				parziale.remove(parziale.size()-1);
				
			}
		}
		
	}
//	public List<String> calcolaPercorso(Arco a){
//		
//		String sorgente = a.getCrime1();
//		String destinazione = a.getCrime2();
//		best = new ArrayList<>();
//		
//		List<String> parziale = new ArrayList<>();
//		parziale.add(sorgente);
//		cerca(parziale, destinazione);
//		return best;
//	}
//	
//	private void cerca(List<String> parziale, String destinazione) {
//		//condizione di terminazione
//		if(parziale.get(parziale.size() -1).equals(destinazione)) {
//			//è la soluzione migliore?
//			if(parziale.size() > best.size()) {
//				best = new LinkedList<>(parziale);
//			}
//			return;
//		}
//
//		//scorro i vicini dell'ultimo inserito e provo le varie "strade"
//		for(String v : Graphs.neighborListOf(this.grafo, 
//				parziale.get(parziale.size()-1))) {
//			if(!parziale.contains(v)) {
//				parziale.add(v);
//				cerca(parziale,destinazione);
//				parziale.remove(parziale.size()-1);
//			}
//		}
//	}


	
	
	
	
	
	
	
	
	
	
	
	
}
