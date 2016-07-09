package klasterizacija;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;

import main.Slika;
import weka.classifiers.Evaluation;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.EM;
import weka.clusterers.FilteredClusterer;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ArffSaver;
import weka.core.converters.ArffLoader.ArffReader;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.stemmers.SnowballStemmer;
import weka.core.stemmers.Stemmer;
import weka.core.tokenizers.WordTokenizer;
import weka.filters.Filter;
import weka.filters.MultiFilter;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.gui.explorer.VisualizePanel;

public class EMKlasterizacija {

	private FilteredClusterer filteredClusterer;
	private Instances data;

	public EMKlasterizacija(String arffFileName) {

		try {
			DataSource loader = new DataSource(arffFileName);
			data = loader.getDataSet();

			filteredClusterer = buildClusterer();
			
			ispisiPripadanjaKlasterima(filteredClusterer);
			dodeliKlastere(filteredClusterer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void ispisiPripadanjaKlasterima(FilteredClusterer filteredClusterer) throws Exception {
		ClusterEvaluation ce = new ClusterEvaluation();
		ce.setClusterer(filteredClusterer);
		ce.evaluateClusterer(data);
		System.out.println(ce.clusterResultsToString());
	}

	private void dodeliKlastere(FilteredClusterer filteredClusterer)
			throws Exception {
			
		List<String> klasteri = new LinkedList<>();
		for(int i = 0; i < data.numInstances(); i++){		
			klasteri.add("Klaster "+ (filteredClusterer.clusterInstance(data.instance(i))+1));
		}

		dodajPripadanjeKlasteru(klasteri);
	}

	private FilteredClusterer buildClusterer() throws Exception {

		WordTokenizer tokenizer = new WordTokenizer();
		tokenizer.setDelimiters(".,;:=_'/\"()?!#@- ");

		Remove removeFilter = new Remove();
		removeFilter.setAttributeIndices("first-2,5");

		StringToWordVector textToWordfilter = new StringToWordVector();
		textToWordfilter.setTokenizer(tokenizer);

		Stemmer stemmer = new SnowballStemmer();

		textToWordfilter.setStemmer(stemmer);

		textToWordfilter.setInputFormat(data);
		textToWordfilter.setWordsToKeep(500);
		textToWordfilter.setUseStoplist(true);
		textToWordfilter.setDoNotOperateOnPerClassBasis(true);
		textToWordfilter.setLowerCaseTokens(true);

		Filter[] filters = new Filter[2];
		filters[0] = removeFilter;
		filters[1] = textToWordfilter;

		MultiFilter multiFilter = new MultiFilter();
		multiFilter.setFilters(filters);

		filteredClusterer = new FilteredClusterer();
		filteredClusterer.setFilter(multiFilter);

		EM EMClusterer = new EM();

		filteredClusterer.setClusterer(EMClusterer);
		filteredClusterer.buildClusterer(data);

		System.out.println(filteredClusterer.getClusterer());
		
		return filteredClusterer;

	}

	private void dodajPripadanjeKlasteru(List<String> klasteri) throws Exception {

		BufferedReader reader = new BufferedReader(new FileReader(
				"./data/slike.arff"));
		ArffReader arff = new ArffReader(reader);
		Instances data = arff.getData();

		for (int i = 0; i < data.numInstances(); i++) {
			data.instance(i).setValue(4, klasteri.get(i));
		}

		ArffSaver saver = new ArffSaver();
		saver.setInstances(data);
		saver.setFile(new File("./data/klasterovaneSlike.arff"));
		saver.writeBatch();

	}

	public static void main(String[] args) throws Exception {
		try {
			new EMKlasterizacija("data/slike.arff");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
