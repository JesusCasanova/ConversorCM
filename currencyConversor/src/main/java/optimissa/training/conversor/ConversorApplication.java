package optimissa.training.conversor;

import org.json.JSONObject;
import optimissa.training.conversor.model.Amount;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

@SpringBootApplication
public class ConversorApplication {

	public static ArrayList<Amount> listaEquivalenciasUSD= new ArrayList<>();

	public static void main(String[] args) throws IOException, InterruptedException {
		/* lista de valores fijos en el codigo para comprobar que hacemos
		bien el proceso de conversion, a sustituir por valores
		dinamicos sacados de otra API externa
		listaEquivalenciasEURO.add(new Amount("USD", 1.02f));
		listaEquivalenciasEURO.add(new Amount("GBP", 0.83f));
		listaEquivalenciasEURO.add(new Amount("JPY", 133.9f));
		 */
		//con la respuesta de la llamada a este endpoint

		/**
		 * Hacemos un objeto json donde esperamos la respuesta de la url, usamos el newBuilder
		 * para que construya una nueva petición a la url
		 * selecionamos la version de la htp que es 1.1, ponemos la url que no es la primera
		 * en el header hacemos la petición json
		 * method en este caso get pero puede ser post
		 * HttpResponse.BodyHandlers.ofString()).body()); para pillar el contenido de la respuesta
		 * string linea para que la respuesta sea tipo string y poder separarla
		 */
		JSONObject respuesta = new JSONObject(HttpClient.newHttpClient().send(HttpRequest.newBuilder()
						.version(HttpClient.Version.HTTP_1_1)
						.uri(URI.create("https://api.fastforex.io/fetch-all?api_key=ea0690b13a-7ea2e7d0d2-rfzzp0"))
						.header("Content-Type", "application/json")
						.method("GET", HttpRequest.BodyPublishers.noBody())
						.build()
				, HttpResponse.BodyHandlers.ofString()).body());
		String linea= "";
		//rellenamos linea por la linea de la respueta un nuevo elemento del arralist
		for(int i= 0; i< respuesta.getJSONObject("results").length(); i++) {
			//System.out.println(i);
			linea= (((respuesta.getJSONObject("results").toString())).split(",")[i]).replace("{", "").replace("}", "").replace("\"", "");
			//System.out.println(linea.split(":")[0]);
			//System.out.println(linea.split(":")[1]);
			//el split nos permite seperar una cadena en base a un caracter y con el 
			//corchete cogemos la parte que queremos, la primera, segunda, etc..
			listaEquivalenciasUSD.add(new Amount(linea.split(":")[0].trim(),
												  Float.parseFloat(linea.split(":")[1].trim())));
		}

		SpringApplication.run(ConversorApplication.class, args);
	}

	/

}
