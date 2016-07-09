	package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import main.Slika;
import niti.NitPrikaz;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class FrmPrikazSlika extends JFrame {

	private JPanel contentPane;
	private JPanel panel;
	private JComboBox<String> jcbKlasteri;
	private JScrollPane scrollPane;
	private JPanel panelZaSlike;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FrmPrikazSlika frame = new FrmPrikazSlika();
					frame.setSize(820,700);
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FrmPrikazSlika() {
		setTitle("Slike");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		jcbKlasteri = new JComboBox<String>();
		contentPane.add(jcbKlasteri, BorderLayout.NORTH);

		panel = new JPanel();
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.BLUE, null,
				null, null));

		scrollPane = new JScrollPane(panel);
		FlowLayout fl_panel = new FlowLayout(FlowLayout.CENTER, 5, 5);
		panel.setLayout(fl_panel);

		panelZaSlike = new JPanel();
		panel.add(panelZaSlike);
		panelZaSlike.setLayout(new GridLayout(0, 5, 5, 5));

		contentPane.add(scrollPane, BorderLayout.CENTER);

		List<Slika> slike = ucitajSlike();
		prikaziCB(slike);
		prikaziSlike(slike);
		
	}

	private void prikaziCB(final List<Slika> slike) {
		List<String> klasteri = new LinkedList<String>();
		for (int i = 0; i < slike.size(); i++) {
			String klaster = slike.get(i).getCluster();
			if (!klasteri.contains(klaster) && !klaster.equals("")) {
				klasteri.add(klaster);
				klasteri.sort(new Comparator<String>() {

					@Override
					public int compare(String arg0, String arg1) {
						return arg0.compareTo(arg1);
					}
				});
			}
		}

		jcbKlasteri.removeAllItems();
		jcbKlasteri.addItem("Svi klasteri");
		if (!klasteri.isEmpty()) {
			for (String k : klasteri) {
				jcbKlasteri.addItem(k);
			}
		}

		jcbKlasteri.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String selektovano = (String) jcbKlasteri.getSelectedItem();
				try {
					List<Slika> selektovane = new LinkedList<>();
					for (Slika s : slike) {
						if (s.getCluster().equals(selektovano)
								|| selektovano.equals("Svi klasteri"))
							selektovane.add(s);
					}
					panelZaSlike.removeAll();
					prikaziSlike(selektovane);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void prikaziSlike(List<Slika> slike) {
		try {
			NitPrikaz nit = new NitPrikaz(contentPane, panelZaSlike, slike);
			nit.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<Slika> ucitajSlike() {
		try {
			DataSource loader = new DataSource("./data/klasterovaneSlike.arff");
			Instances data = loader.getDataSet();
			
			List<Slika> slike = new LinkedList<>();
			for (int i = 0; i < data.numInstances(); i++) {
				String id = data.instance(i).stringValue(data.attribute(0));
				String link = data.instance(i).stringValue(data.attribute(1));
				String[] tags1 = data.instance(i).stringValue(data.attribute(3)).split("#");
				String title = data.instance(i).stringValue(data.attribute(2));
				String cluster = data.instance(i).stringValue(data.attribute(4));
				
				LinkedList<String> tags = new LinkedList<>();
				for (int j = 0; j < tags1.length; j++) {
					tags.add(tags1[j]);
				}
				
				Slika s = new Slika(id, link, title, tags);
				s.setCluster(cluster);
				slike.add(s);
			}
			return slike;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

}
