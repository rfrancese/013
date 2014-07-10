package it.unisa.grogchallenge;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


public class ConnectJsonUtil {
	private AccountManager mAccountManager;
	private Context ctx;
	private String json ="";	
	private String url;
	private DBGrogChallenge db;
	private String email;
	public static final String LOCAL_MAIL = "localMail";
	public static final String LOCAL_NAME = "local";
	private SessionGame game;
	private Utente utente;
	private HashMap<String,String> classifica;
	private boolean loaded = false;
	
	
	public boolean isLoaded() {
		return loaded;
	}


	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}


	public ConnectJsonUtil(Context ctx,String url,DBGrogChallenge db,SessionGame g){
		this.ctx=ctx;
		this.url=url;
		this.db=db;
		this.game = g;
	}
	
	
	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public Utente getUtente() {
		return utente;
	}


	public void setUtente(Utente utente) {
		this.utente = utente;
	}


	public HashMap<String, String> getClassifica() {
		return classifica;
	}


	public void setClassifica(HashMap<String, String> classifica) {
		this.classifica = classifica;
	}


	public String[] getAccountNames() {
	   /* mAccountManager = AccountManager.get(ctx);
	    Account[] accounts = mAccountManager.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
	    String[] names = new String[accounts.length];
	    for (int i = 0; i < names.length; i++) {
	        names[i] = accounts[i].name;
	    }
	    return names;*/
		return null;
	}

 private String POST(String url){
	
        InputStream inputStream = null;
        String result = "";
        HttpResponse httpResponse = null;
        HttpClient httpclient=null;
        HttpPost httpPost  = null;
       StatusLine statusLine=null;
       int lastHttpErrorCode = 0;
        try {
        	
            // 1. create HttpClient
        	httpclient= new DefaultHttpClient();
 
            // 2. make POST request to the given URL
           httpPost= new HttpPost(url);
           
           HttpParams httpParameters = httpPost.getParams();
           // Set the timeout in milliseconds until a connection is established.
           int timeoutConnection = 3000;
           HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
           // Set the default socket timeout (SO_TIMEOUT) 
           // in milliseconds which is the timeout for waiting for data.
           int timeoutSocket = 3000;
           HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
           
 /*	
            String json = "";
 
            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            /*jsonObject.accumulate("name", person.getName());
            jsonObject.accumulate("country", person.getCountry());
            jsonObject.accumulate("twitter", person.getTwitter());
 			
            for (int i = 0; i < key.length; i++) {
            	 jsonObject.accumulate(key[i],value[i]);
			}
            
            jsonObject.accumulate("operazione", "checkEmail");
            jsonObject.accumulate("email", this.email);
 			
            
            
            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();
 
            // ** Alternative way to convert Person object to JSON string usin Jackson Lib 
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person); 
 */
            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);
 
            // 6. set httpPost Entity
            httpPost.setEntity(se);
 
            // 7. Set some headers to inform server about the type of the content   
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
 
            // 8. Execute POST request to the given URL
            httpResponse  = httpclient.execute(httpPost);
            statusLine= httpResponse.getStatusLine();
            lastHttpErrorCode = statusLine.getStatusCode();
            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
 
            // 10. convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
 
        } catch (ConnectException e) {
        	
            //Log.d("ConnectException", e.getLocalizedMessage());
            //e.printStackTrace();
        }catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
        	//Log.d("SERVER","SocketTimeoutException");
        	e.printStackTrace();
        	//riporto il gioco allo stato precedente
        	if(game.getLastState()==game.MENU){
        		game.setState(game.MENU);
        	}
        	checkTutorial();
        } catch (ConnectTimeoutException e) {
        	
        	//Log.d("SERVER","ConnectTimeoutException");
            e.printStackTrace();

        	//riporto il gioco allo stato precedente
            if(game.getLastState()==game.MENU){
        		game.setState(game.MENU);
        	}
            checkTutorial();
            
        } catch (Exception e) {
            //Log.d("InputStream", e.getLocalizedMessage());
            e.printStackTrace();
        }finally{
        	httpclient.getConnectionManager().closeExpiredConnections();
        	
        }
 
       
        
        return result; 
    }

 private String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
 
        inputStream.close();
     
        return result;
 
    }   
 

 public void loadUtente() throws InterruptedException, ExecutionException{
	    costruisciUtente();
		//new LoadUtente().execute(url);
		new LoadUtente().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
 }
 
 public void loadClassifica() throws InterruptedException, ExecutionException{
	    costruisciClassifica();
		//new LoadClassifica().execute(url);
		new LoadClassifica().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
		 
 }
 
 
 public String constructJson(String[] key,String[] value) throws JSONException {
	
	
	 
	 String str = "";
	 
     // 3. build jsonObject
     JSONObject jsonObject = new JSONObject();
    	
     for (int i = 0; i < key.length; i++) {
     	 jsonObject.accumulate(key[i],value[i]);
		}
     
     
     
     str = jsonObject.toString();

     this.json=str;
     return str;
     // ** Alternative way to convert Person object to JSON string usin Jackson Lib 
     // ObjectMapper mapper = new ObjectMapper();
     // json = mapper.writeValueAsString(person); 
	 
	 
	}
 public boolean isOnline() {
	   /* ConnectivityManager cm =
	        (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;*/
	 
	 boolean outcome = false;

     if (ctx != null) {
         ConnectivityManager cm = (ConnectivityManager) ctx
                 .getSystemService(Context.CONNECTIVITY_SERVICE);

         NetworkInfo[] networkInfos = cm.getAllNetworkInfo();

         for (NetworkInfo tempNetworkInfo : networkInfos) {


             /**
              * Can also check if the user is in roaming
              */
             if (tempNetworkInfo.isConnected()) {
                 outcome = true;
                 break;
             }
         }
     }
    /* if(!outcome){
    		//Toast.makeText(ctx, "No internet connection!", Toast.LENGTH_LONG).show();
    		return false;
     }*/
    
	return outcome;
 	
	}
 
 
 	
 	
 	
 	
 private class LoadUtente extends AsyncTask<String, String, String> {
	 	
	 protected void onPreExecute(String...urls) {
		 		
	 }
	 	@Override
	    protected String doInBackground(String... urls) {
	 		return POST(urls[0]);
	    }
	   
		// onPostExecute displays the results of the AsyncTask.
	    @Override
	    protected void onPostExecute(String result) {
	    
	    	Log.d("RISULTATO", result);
	    	//Toast.makeText(ctx, "Data Sent!", Toast.LENGTH_LONG).show();
	    	
	    	ParseJson parse = new ParseJson();
	    	
	    	HashMap<String,String> resu = parse.parse(result); 
	    	if(resu!=null){
	    		//Log.d("LOAD UTENTE", "creo l'utente caricato dal server");
		        	utente = new Utente();
		        	utente.setNome(resu.get("nome"));
		        	utente.setPosition(resu.get("posizione"));
		        	int point = Integer.parseInt(resu.get("punteggio"));
		        	utente.setPunteggio(point);
		        	utente.setEmail(email);
		
		        	
		        	//Log.d("UTENTE","Utente:" + utente.toString() );
		        	if(db.getUtentiCount()>0){
			        	int pointLocal = db.getAll().get(0).getPunteggio();
			        	if(pointLocal > utente.getPunteggio()){
			        		utente.setPunteggio(pointLocal);
			        		try {
			        			
			        			//nuovo asynctask che aggiorna il punteggio anche sul server
								addPunteggio(utente.getNome(),utente.getPunteggio(),utente.getEmail());
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			        		
			        	}
		        	}
		        	
		        	aggiornaDB();
		        	
		        	
		        	
		        	
		        	try {
						loadClassifica();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        	
	    	}	
	    	
	    	checkTutorial();
	    	
        }
	    
	}
 	
 public void checkTutorial(){
	 if(db.getUtentiCount()>0){
			Utente utente = db.getAll().get(0);//avrò solo un utente in locale
				if(utente.getFirst()==0){
					//è la prima volta che viene aperta la sessione di gioco
					 //Log.d("GROGCHALLENGE","fase di tutorial");
					 game.setTutorialState(true);
					// utente.setFirst(1);
					// db.update(utente);
		  		 }
				
			}else if(db.getUtentiCount()==0){
			
					//è la prima volta che viene aperta la sessione di gioco
					//e ci sono stati problemi di connessione per cui non si è riuscito
					//a memorizzare ancora nulla
					 //Log.d("GROGCHALLENGE","fase di tutorial");
					 game.setTutorialState(true);
					 
					 //poichè sono arrivato in questo stato significa che
					 //o non ho connessione oppure ho problemi con la connessione
					 // e non ho record nel database locale, quindi inserisco un record
					 //con nome "local" nel database locale
					 db.inserisci(getEmail(),LOCAL_MAIL,0);
								
			}
 }
 
 
 public void costruisciUtente(){
	 String[] key = new String[2];
	 String[] value = new String[2];
	 
	 key[0] = "operazione";
	 value[0] ="getUtente";
	 key[1] = "email";
	 value[1] = email;
	 try {
		constructJson(key, value);
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 
 }
	
 	
 
 
 public void costruisciClassifica(){
	 String[] key = new String[1];
	 String[] value = new String[1];
	 
	 key[0] = "operazione";
	 value[0] ="getTopList";
	 
	 try {
		constructJson(key, value);
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
 }
 
 private class LoadClassifica extends AsyncTask<String, String, String> {
	 	
	 protected void onPreExecute(String...urls) {
		  
		    
	 }
	 	@Override
	    protected String doInBackground(String... urls) {
	 		return POST(urls[0]);
	    }
	   
		// onPostExecute displays the results of the AsyncTask.
	    @Override
	    protected void onPostExecute(String result) {
	    	
	    	//Log.d("RISULTATO", result);
	    	//Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
	    	//Closes the connection.
	    	ParseJson parse = new ParseJson();
	    	
	    	classifica = parse.parse(result); 
        	loaded=true;
	    	
	   }
	}
 	
 
 private class AddPunteggio extends AsyncTask<String, String, String> {
	 	
	 protected void onPreExecute(String...urls) {
		  
		    
	 }
	 	@Override
	    protected String doInBackground(String... urls) {
	 		return POST(urls[0]);
	    }
	   
		// onPostExecute displays the results of the AsyncTask.
	    @Override
	    protected void onPostExecute(String result) {
	    	
	    	//Log.d("RISULTATO", result);
	    	//Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
	    	//Closes the connection.
	    	
	    	try {
				loadUtente();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	   }
	}
 	
 
 	public void checkUtente(){
 		String[] emails = getAccountNames();
 		email = emails[0];
 		
 		try {
			this.loadUtente();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		
 	}
 

 private class HttpAsyncTask extends AsyncTask<String, String, String> {
	 	
	 protected void onPreExecute(String...urls) {
		    //Show progress Dialog here
		      super.onPreExecute();
		  
		   
		 		
	 }
	 	@Override
	    protected String doInBackground(String... urls) {
	 		return POST(urls[0]);
	    }
	   
		// onPostExecute displays the results of the AsyncTask.
	    @Override
	    protected void onPostExecute(String result) {
	    	super.onPostExecute(result);
	    
	    	//Log.d("RISULTATO", result);
	    	//Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
	    	//Closes the connection.
            
	   }
	}
 	
 
 public void newMail(String mail){
		db.inserisci(mail,LOCAL_NAME,0);
		}
		
 
 public void aggiornaDB(){
	 if(utente.getNome() != null){
		 if(db.getUtentiCount()>0){
		// Log.d("GROGCHALLENGE_DB","aggiorno il db");
		 
		 //inserisco l'utente all'id corrispondente in locale
		 int id = db.getAll().get(0).getId();
		 int first = db.getAll().get(0).getFirst();
		 utente.setFirst(first);
		 utente.setId(id);
		 db.update(utente);
		 }else{
			 db.inserisci(utente.getEmail(),utente.getNome(), utente.getPunteggio());
		 }
	 }
 }
 
 
 

	public HashMap<String,String> checkName(String name) throws JSONException, InterruptedException, ExecutionException{
		String[] key = new String[2];
		   String[] value = new String[2];
		   key[0]="operazione";
		   value[0]="checkName";
		   key[1]="nome";
		   value[1]=name;
		   Log.d("CHECKNAME", name);
			constructJson(key, value);
		   	String result = connectToSend();
			ParseJson parse = new ParseJson();
			HashMap<String,String> hmResult = parse.parse(result);
			return hmResult;
	}
 
 
	public String connectToSend() throws InterruptedException, ExecutionException{
		 return new HttpAsyncTask().execute(url).get();
	 }
 
	public void connectToSendAsync() throws InterruptedException, ExecutionException{
		new HttpAsyncTask().execute(url);
	 }
	public void addPunteggio(String nome,int punteggio,String email) throws Exception{
 		
			Log.d("ADD PUNTEGGIO","add: " + nome + " - " + punteggio + " - " + email);
		   String[] key = new String[4];
		   String[] value = new String[4];
		   key[0]="operazione";
		   value[0]="addPunteggio";
		   key[1]="email";
		   value[1]=email;
		  key[2]="nome";
		   value[2]=nome;
		  key[3]="punteggio";
		   value[3]="" + punteggio;
			
		   	constructJson(key, value);
			new AddPunteggio().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
			
			
	}
	
	
	/*
	 * prendo la mail 
	 * se è sul server
	 * carico dal server
	 * altrimenti creo un record locale con nickname "local"
	 * 
	 */
	
	
	public void noRecord(){
		 String[] key = new String[2];
		   String[] value = new String[2];
		   key[0]="operazione";
		   value[0]="checkEmail";
		   key[1]="email";
		   value[1]=email;
		   
			try {
				constructJson(key, value);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			new NoRecordOnline().execute(url);
			
	}
	
	private class NoRecordOnline extends AsyncTask<String, String, String> {
	 	
		 protected void onPreExecute(String...urls) {
			  
			    
		 }
		 	@Override
		    protected String doInBackground(String... urls) {
		 		return POST(urls[0]);
		    }
		   
			// onPostExecute displays the results of the AsyncTask.
		    @Override
		    protected void onPostExecute(String result) {
		    	
		    	//Log.d("RISULTATO", result);
		    	//Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
		    	//Closes the connection.
		    	ParseJson parse = new ParseJson();
		    	
		    	HashMap<String, String> res = parse.parse(result); 
	        	if(res != null){
			    	if(res.get("result").equals("-1")){
			    		//la mail non c'è sul server
			    		//Log.d("RESULT", "la mail non è sul server: " + email);
			    		db.inserisci(email, LOCAL_NAME,0);
			    	}else{
			    		try {
							loadUtente();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			    	}
	        	}
		   }
		}
	 	
	public void getMailFromDevice(){
		String[] emails = getAccountNames();
		this.email = emails[0];
	}
	
	
	
	public void recordOnline(){
		 String[] key = new String[2];
		   String[] value = new String[2];
		   key[0]="operazione";
		   value[0]="checkEmail";
		   key[1]="email";
		   value[1]=email;
		   
			try {
				constructJson(key, value);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			new RecordOnline().execute(url);
			
	}
	
	private class RecordOnline extends AsyncTask<String, String, String> {
	 	
		 protected void onPreExecute(String...urls) {
			  
			    
		 }
		 	@Override
		    protected String doInBackground(String... urls) {
		 		return POST(urls[0]);
		    }
		   
			// onPostExecute displays the results of the AsyncTask.
		    @Override
		    protected void onPostExecute(String result) {
		    	
		    	Log.d("RISULTATO", result);
		    	//Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
		    	//Closes the connection.
		    	ParseJson parse = new ParseJson();
		    	
		    	HashMap<String, String> res = parse.parse(result); 
	        		if(res!=null){
				    	if(res.get("result").equals("-1")){
				    		//la mail non c'è sul server
				    		//fine
				    	}else{
				    		try {
								loadUtente();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ExecutionException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
				    	}
	        		}
		    	
		    }
		    
		    
		}
	
	
	
	
	
}//FINE CLASSE CONNECT JSON
