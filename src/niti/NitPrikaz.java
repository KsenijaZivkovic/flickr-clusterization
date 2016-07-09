package niti;

import java.awt.BorderLayout;
import java.awt.Event;
import java.awt.GridLayout;
import java.awt.Image;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import main.Slika;

public class NitPrikaz extends Thread {

	private JPanel contentPane;
	private List<Slika> slike;
	private JTabbedPane tabbedPane;
	private JPanel panel;

	public NitPrikaz(JPanel contentPane, JPanel panel, List<Slika> slike) {
		this.contentPane = contentPane;
		this.panel = panel;
		this.slike = slike;
	}

	@Override
	public void run() {
		// inicijalizuj1();
		for (int j = 0; j < slike.size(); j++) {

			URL url = null;
			try {
				url = new URL(slike.get(j).getLink());

				Image image = ImageIO.read(url);
				JLabel jlbl = new JLabel(new ImageIcon(image));
				jlbl.setToolTipText(slike.get(j).getTitle());

				panel.add(jlbl);
				contentPane.updateUI();

			} catch (Exception e) {

			}
		}

	}

	private void inicijalizuj1() {
		int brStrana = slike.size() / 15;
		if (slike.size() % 15 != 0)
			brStrana++;
		tabbedPane = new JTabbedPane();
		for (int i = 0; i < brStrana; i++) {

			panel = new JPanel(new GridLayout(0, 5));

			JScrollPane scrollPane = new JScrollPane(panel,
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			// scrollPane.setLayout(panel.getLayout());

			for (int j = 0; j < slike.size(); j++) {
				if (j == 15 || i > 0) {
					break;
				}
				URL url = null;
				try {
					url = new URL(slike.get(j).getLink());

					Image image = ImageIO.read(url);
					JLabel jlbl = new JLabel(new ImageIcon(image));

					panel.add(jlbl);

				} catch (Exception e) {

				}
			}

			tabbedPane.addTab("" + (i + 1), scrollPane);
			contentPane.add(tabbedPane);
		}

		contentPane.updateUI();
	}
}
