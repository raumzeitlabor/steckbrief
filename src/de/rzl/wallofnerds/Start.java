package de.rzl.wallofnerds;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.apache.batik.apps.rasterizer.DestinationType;
import org.apache.batik.apps.rasterizer.SVGConverter;
import org.apache.batik.apps.rasterizer.SVGConverterException;

import de.rzl.wallofnerds.component.BadgeJComboBox;

public class Start extends JFrame {

	private static final long serialVersionUID = 1L;
	private JLabel lbNick, lbLine2, lbQR, lbAlreadyQR, lbOutput, lbPic, lbPDF,
			lbPNG, lbJPEG, lbTIFF, lbBadges;
	private JTextField tfNick, tfLine2, tfQR, tfPic;
	private JButton btDoit;
	private JCheckBox cbAlreadyQR, cbPDF, cbPNG, cbJPEG, cbTIFF;
	private File file;
	private List<BadgeJComboBox> badges = new ArrayList<BadgeJComboBox>();

	public Start() {
		super("Wall of Nerds - Steckbriefgenerator v 0.1");
		initGUI();
		initActions();
		file = new File("template.svg");
		file = file.getAbsoluteFile();
		if (!file.exists()) {
			JOptionPane
					.showMessageDialog(this,
							"Template konnte nicht gefunden werden. Bitte geben sie den Pfad an.");
			JFileChooser chooser = new JFileChooser();

			int state = chooser.showOpenDialog(null);
			if (state == JFileChooser.APPROVE_OPTION) {
				file = chooser.getSelectedFile();
			} else {
				JOptionPane.showMessageDialog(null,
						"Y U NO choose template?\nProgramm wird beendet!",
						"WTF?", JOptionPane.ERROR_MESSAGE, ImageIconFactory
								.getImageIcon("resource/y_u_no.jpg", true));
				System.exit(0);
			}
		}
	}

	private void initActions() {
		btDoit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!cbPDF.isSelected() && !cbPNG.isSelected()
						&& !cbJPEG.isSelected() && !cbTIFF.isSelected()) {
					JOptionPane.showMessageDialog(null,
							"Bitte mindestens ein Outputformat wählen",
							"Fehler", JOptionPane.INFORMATION_MESSAGE, null);
				} else {
					StringBuffer input;
					try {
						BufferedReader br = new BufferedReader(new FileReader(
								file));

						input = new StringBuffer();
						try {
							input.append(br.readLine());
						} catch (IOException e) {
							e.printStackTrace();
						}
						try {
							while (!(input.append(br.readLine())).substring(
									input.length() - 4).equals("null")) {
							}
							String s = input.substring(0, input.length() - 4);
							if ((!s.contains("_PIC_")) || (!s.contains("_QR_"))
									|| (!s.contains("_LINE2_"))
									|| (!s.contains("_NICK_"))) {
								System.out.println("Keine gültige Datei");
								return;
							}
							s = s.replace("_NICK_", tfNick.getText());
							s = s.replace("_LINE2_", tfLine2.getText());
							s = s.replace("_PIC_", tfPic.getText());
							for (int i = 0; i < badges.size(); i++) {
								if (badges.get(i).getSelectedBadge().getName()
										.equals("None")) {
									int pos = s.lastIndexOf("_BADGE" + (i + 1)
											+ "_")
											+ 1
											+ ("_BADGE" + (i + 1) + "_")
													.length();
									s = s.substring(0, pos)
											+ " width=\"0\" height=\"0\" "
											+ s.substring(pos + 55);
								} else {
									s = s.replace("_BADGE" + (i + 1) + "_",
											"badges/"
													+ badges.get(i)
															.getSelectedBadge()
															.getFileName());
								}
							}
							if (cbAlreadyQR.isSelected()) {
								s = s.replace("_QR_", tfNick.getText());
							} else {
								s = s.replace("_QR_",
										"http://api.qrserver.com/v1/create-qr-code/?data="
												+ tfNick.getText()
												+ "&amp;size=600x600");
							}
							s = s.replace("_PIC_", tfNick.getText());
							File svgFile = File.createTempFile("graphic-",
									".svg",
									new File(System.getProperty("user.dir")));
							svgFile.deleteOnExit();
							BufferedWriter out = new BufferedWriter(
									new FileWriter(svgFile));
							try {
								out.write(s);
							} finally {
								out.close();
							}
							if (cbPDF.isSelected()) {
								File outputFile = new File(file.getParent()
										+ "/steckbrief.pdf");
								SVGConverter converter = new SVGConverter();
								converter
										.setDestinationType(DestinationType.PDF);
								converter.setSources(new String[] { svgFile
										.toString() });
								converter.setDst(outputFile);
								converter.execute();
								JOptionPane.showMessageDialog(null,
										"Erfolgreich als steckbrief.pdf unter "
												+ file.getParent()
												+ " abgelegt.", "Erfolg",
										JOptionPane.INFORMATION_MESSAGE, null);
							}
							if (cbPNG.isSelected()) {
								File outputFile = new File(file.getParent()
										+ "/steckbrief.png");
								SVGConverter converter = new SVGConverter();
								converter
										.setDestinationType(DestinationType.PNG);
								converter.setSources(new String[] { svgFile
										.toString() });
								converter.setDst(outputFile);
								converter.execute();
								JOptionPane.showMessageDialog(null,
										"Erfolgreich als steckbrief.png unter "
												+ file.getParent()
												+ " abgelegt.", "Erfolg",
										JOptionPane.INFORMATION_MESSAGE, null);
							}
							if (cbJPEG.isSelected()) {
								File outputFile = new File(file.getParent()
										+ "/steckbrief.jpeg");
								SVGConverter converter = new SVGConverter();
								converter
										.setDestinationType(DestinationType.JPEG);
								converter.setSources(new String[] { svgFile
										.toString() });
								converter.setDst(outputFile);
								converter.execute();
								JOptionPane.showMessageDialog(null,
										"Erfolgreich als steckbrief.jpeg unter "
												+ file.getParent()
												+ " abgelegt.", "Erfolg",
										JOptionPane.INFORMATION_MESSAGE, null);
							}
							if (cbTIFF.isSelected()) {
								File outputFile = new File(file.getParent()
										+ "/steckbrief.tiff");
								SVGConverter converter = new SVGConverter();
								converter
										.setDestinationType(DestinationType.TIFF);
								converter.setSources(new String[] { svgFile
										.toString() });
								converter.setDst(outputFile);
								converter.execute();
								JOptionPane.showMessageDialog(null,
										"Erfolgreich als steckbrief.tiff unter "
												+ file.getParent()
												+ " abgelegt.", "Erfolg",
										JOptionPane.INFORMATION_MESSAGE, null);
							}
							JOptionPane.showMessageDialog(null,
									"Alle Exports erfolgreich abgeschlossen!",
									"Fuck Yeah",
									JOptionPane.INFORMATION_MESSAGE,
									ImageIconFactory.getImageIcon(
											"resource/fuck_yeah.jpg", true));
						} catch (IOException e) {
							e.printStackTrace();
						} catch (SVGConverterException e) {
							e.printStackTrace();
						}
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	private void initGUI() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		lbNick = new JLabel("Nickname:");
		tfNick = new JTextField();
		tfNick.setToolTipText("Dein Nickname");
		tfNick.setPreferredSize(new Dimension(180, 25));
		lbLine2 = new JLabel("Linie 2:");
		tfLine2 = new JTextField();
		tfLine2.setToolTipText("Zusatzinfo, eventuell ein kleines Zitat");
		tfLine2.setPreferredSize(new Dimension(180, 25));
		lbQR = new JLabel("URL für QR-Code:");
		tfQR = new JTextField();
		tfQR.setToolTipText("Hier kannst du die URL zu deiner Webpage oÄ. oder direkt zu einem QR-Code angeben.");
		tfQR.setPreferredSize(new Dimension(180, 25));
		lbAlreadyQR = new JLabel("Ist das bereits ein QR-Code?");
		cbAlreadyQR = new JCheckBox();
		cbAlreadyQR
				.setToolTipText("Falls hier kein Haken gesetzt wird kann jegliche Art von Information in das Feld\noben eingegeben werden, es wird dann in einen QR umgewandelt.");
		lbPic = new JLabel("URL zum Bild:");
		tfPic = new JTextField();
		tfPic.setToolTipText("Ein Bild von dir");
		tfPic.setPreferredSize(new Dimension(180, 25));
		lbOutput = new JLabel("Output:");
		lbPDF = new JLabel("PDF");
		cbPDF = new JCheckBox();
		lbPNG = new JLabel("PNG");
		cbPNG = new JCheckBox();
		lbJPEG = new JLabel("JPEG");
		cbJPEG = new JCheckBox();
		lbTIFF = new JLabel("TIFF");
		cbTIFF = new JCheckBox();
		for (int i = 0; i < 10; i++) {
			badges.add(new BadgeJComboBox());
		}
		btDoit = new JButton("Let's go");
		c.gridy = 0;
		c.gridx = 0;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.LINE_END;
		this.add(lbNick, c);
		c.gridx += 1;
		c.gridwidth = 8;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(tfNick, c);
		c.gridy += 1;
		c.gridx = 0;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		this.add(lbLine2, c);
		c.gridx += 1;
		c.gridwidth = 8;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(tfLine2, c);
		c.gridy += 1;
		c.gridx = 0;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		this.add(lbQR, c);
		c.gridx += 1;
		c.gridwidth = 8;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(tfQR, c);
		c.gridy += 1;
		c.gridx = 0;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		this.add(lbAlreadyQR, c);
		c.gridx += 1;
		c.gridwidth = 8;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(cbAlreadyQR, c);
		c.gridy += 1;
		c.gridx = 0;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		this.add(lbPic, c);
		c.gridx += 1;
		c.gridwidth = 8;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(tfPic, c);
		c.gridy += 1;
		c.gridx = 0;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		this.add(lbOutput, c);
		c.gridx += 1;
		c.anchor = GridBagConstraints.CENTER;
		this.add(lbPDF, c);
		c.gridx += 1;
		this.add(cbPDF, c);
		c.gridx += 1;
		this.add(lbPNG, c);
		c.gridx += 1;
		this.add(cbPNG, c);
		c.gridx += 1;
		this.add(lbJPEG, c);
		c.gridx += 1;
		this.add(cbJPEG, c);
		c.gridx += 1;
		this.add(lbTIFF, c);
		c.gridx += 1;
		this.add(cbTIFF, c);
		c.gridy += 1;
		c.gridx = 0;
		c.gridwidth = 1;
		c.gridheight = 5;
		c.fill = GridBagConstraints.BOTH;
		JLabel l = new JLabel();
		l.setIcon(ImageIconFactory.getImageIcon("resource/all_the_badges.png",
				true));
		this.add(l, c);
		int x = 0;
		c.gridwidth = 4;
		c.gridheight = 1;
		for (int i = 0; i < 5; i++) {
			c.gridx += 1;
			for (int k = 0; k < 2; k++) {
				this.add(badges.get(x), c);
				c.gridx += c.gridwidth;
				x++;
			}
			c.gridx = 0;
			c.gridy += 1;
		}
		c.gridy += 1;
		c.gridx = 0;
		c.gridheight = 1;
		c.gridwidth = 9;
		this.add(btDoit, c);
		pack();
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((d.width - this.getSize().width) / 2,
				(d.height - this.getSize().height) / 2);
		this.setResizable(false);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		new Start().setVisible(true);
	}
}