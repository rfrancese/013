package it.unisa.grogchallenge;

public class Utente {
	private int id;
	private String email;
	private String nome;
	private int punteggio;
	private int first;
	
	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	private String position;
	
	
	
	public Utente(){
	
	}
	
	public Utente(int id, String email,String nome, int punteggio) {
		super();
		this.id = id;
		this.email=email;
		this.nome = nome;
		this.punteggio = punteggio;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public int getPunteggio() {
		return punteggio;
	}
	public void setPunteggio(int punteggio) {
		this.punteggio = punteggio;
	}

	public int getFirst() {
		return first;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	@Override
	public String toString() {
		return "Utente [id=" + id + ", email=" + email + ", nome=" + nome
				+ ", punteggio=" + punteggio + ", first=" + first
				+ ", position=" + position + "]";
	}
	
	
	
}
