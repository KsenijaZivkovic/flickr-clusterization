package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

public class PozivServisa {

	private static PozivServisa instance;
	private List<Slika> slike;

	private PozivServisa() {
		this.slike = new LinkedList<>();
	}

	private void prikupiPodatke() {

		String key = "acaedd637926848183cdc5b671127de3";
		String tags = "cat";
		String search = "cat";
		int perPage = 500;

		LinkedList<Slika> ns = new LinkedList<>();
		try {

			String https_url = MessageFormat
					.format("https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key={0}&tags={1}&text={2}&per_page={3}&content_type=1&license=1,2,3,4,5,6&format=json",
							key, tags, search, perPage);

			// niz slika
			JSONObject jsonObject = primiOdgovor(https_url);
			JSONArray nizSlika = jsonObject.getJSONObject("photos")
					.getJSONArray("photo");

			// niz id-eva
			LinkedList<String> listaID = new LinkedList<>();
			for (int i = 0; i < nizSlika.length(); i++) {
				JSONObject pom = (JSONObject) nizSlika.get(i);
				listaID.add(pom.getString("id"));
			}
			// za svaki id zahtev ka servisu za informacije o slikama
			for (int i = 0; i < listaID.size(); i++) {
				https_url = "https://api.flickr.com/services/rest/?method=flickr.photos.getInfo&api_key=acaedd637926848183cdc5b671127de3&photo_id="
						+ listaID.get(i) + "&format=json";

				jsonObject = primiOdgovor(https_url);
				String naziv = jsonObject.getJSONObject("photo")
						.getJSONObject("title").getString("_content");

				JSONObject tagovi = jsonObject.getJSONObject("photo")
						.getJSONObject("tags");

				JSONArray nizTagova = tagovi.getJSONArray("tag");
				LinkedList<String> nt = new LinkedList<>();
				for (int j = 0; j < nizTagova.length(); j++) {
					JSONObject tag = (JSONObject) nizTagova.get(j);
					String t = tag.getString("raw");
					if (!t.contains("cat")) {
						nt.add(t);
					}
				}

				https_url = "https://api.flickr.com/services/rest/?method=flickr.photos.getSizes&api_key=acaedd637926848183cdc5b671127de3&photo_id="
						+ listaID.get(i) + "&format=json";
				// dostupne velicine slika
				jsonObject = primiOdgovor(https_url);
				// link ka slici koja je velicine 150x150
				String link = ((JSONObject) jsonObject.getJSONObject("sizes")
						.getJSONArray("size").get(1)).getString("source");

				// koristimo sliku samo ako naziv i tagovi nisu prazni i ako naziv sadrzi cat
				if (!nt.isEmpty() && !naziv.isEmpty()&& naziv.toLowerCase().contains("cat") || naziv.toLowerCase().contains("kitt") && !naziv.toLowerCase().contains("park")){//&& !naziv.toLowerCase().contains("fish")&& !naziv.toLowerCase().contains("bird")&& naziv.toLowerCase().contains("park")) {
					Slika s = new Slika(listaID.get(i), link, naziv, nt);
					ns.add(s);
				}
			}

			// pravimo .arff od niza slika
			napraviArff(ns);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void napraviArff(LinkedList<Slika> ns) throws Exception {
		FastVector atts;
		Instances data;

		atts = new FastVector();

		Attribute id = new Attribute("id", (FastVector) null);
		Attribute link = new Attribute("link", (FastVector) null);
		Attribute title = new Attribute("title", (FastVector) null);
		Attribute tags = new Attribute("tags", (FastVector) null);
		Attribute cluster = new Attribute("cluster", (FastVector) null);

		atts.addElement(id);
		atts.addElement(link);
		atts.addElement(title);
		atts.addElement(tags);
		atts.addElement(cluster);

		data = new Instances("slike", atts, 6);

		for (int i = 0; i < ns.size(); i++) {
			Instance instance = new Instance(5);
			instance.setDataset(data);
			instance.setValue(id, ns.get(i).getId());
			instance.setValue(link, ns.get(i).getLink());
			instance.setValue(title, ns.get(i).getTitle());
			instance.setValue(tags, ns.get(i).vratiTagove(ns.get(i).getTags()));

			data.add(instance);
		}

		// cuvamo podatke u arff fajl
		ArffSaver saver = new ArffSaver();
		saver.setInstances(data);
		saver.setFile(new File("./data/slike.arff"));
		saver.writeBatch();
	}

	private String formatirajJson(String izlaz) {
		izlaz = izlaz.substring(izlaz.indexOf("{"),
				izlaz.indexOf(",\"stat\":\"ok\"})"))
				+ "}";
		return izlaz;
	}

	private JSONObject primiOdgovor(String https_url) {
		try {
			URL url = new URL(https_url);
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			if (con != null) {

				BufferedReader br = new BufferedReader(new InputStreamReader(
						con.getInputStream()));

				String izlaz = "";
				String input;

				while ((input = br.readLine()) != null) {
					izlaz += input;
				}
				izlaz = formatirajJson(izlaz);
				br.close();

				return new JSONObject(izlaz);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		new PozivServisa().prikupiPodatke();
	}
}
