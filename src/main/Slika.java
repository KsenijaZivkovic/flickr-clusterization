package main;

import java.io.Serializable;
import java.util.LinkedList;

public class Slika implements Serializable {
	private String id;
	private String title;
	private LinkedList<String> tags;
	private String link;
	private String cluster;

	public Slika(String id, String link,String title, LinkedList<String> tags) {
		this.id = id;
		this.title = title;
		this.tags = tags;
		this.link = link;
		this.cluster = "";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public LinkedList<String> getTags() {
		return tags;
	}

	public void setTags(LinkedList<String> tags) {
		this.tags = tags;
	}

	public String getCluster() {
		return cluster;
	}

	public void setCluster(String cluster) {
		this.cluster = cluster;
	}

	public String vratiTagove(LinkedList<String> tags) {
		String tagovi = "";
		for (int i = 0; i < tags.size(); i++) {
			tagovi += tags.get(i);
			if (i != tags.size() - 1) {
				tagovi += "#";
			}
		}
		return tagovi;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@Override
	public String toString() {
		return "Slika [id=" + id + ", title=" + title + ", tags=" + tags
				+ ", link=" + link + "]";
	}

}
