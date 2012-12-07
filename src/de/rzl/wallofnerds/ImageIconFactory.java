package de.rzl.wallofnerds;


import javax.swing.ImageIcon;

public abstract class ImageIconFactory {

	public static ImageIcon getImageIcon(String path, boolean resource) {
		ImageIcon ii;
		if (resource) {
			ii = new ImageIcon(ImageIconFactory.class.getResource(path));
		} else {
				ii = new ImageIcon(path);
		}
		return ii;
	}

}
