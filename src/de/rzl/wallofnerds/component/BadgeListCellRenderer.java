package de.rzl.wallofnerds.component;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import de.rzl.wallofnerds.Badge;
import de.rzl.wallofnerds.ImageIconFactory;

public class BadgeListCellRenderer extends DefaultListCellRenderer implements
		ListCellRenderer {

	private static final long serialVersionUID = 1L;

	@Override
	public Component getListCellRendererComponent(JList arg0, Object arg1,
			int arg2, boolean arg3, boolean arg4) {
		Component comp = super.getListCellRendererComponent(arg0, arg1, arg2,
				arg3, arg4);
		Badge b = (Badge) arg1;
		JLabel label = new JLabel();
		label.setOpaque(true);
		label.setForeground(comp.getForeground());
		label.setBackground(comp.getBackground());
		if (!b.getName().equals("None")) {
			label.setIcon(ImageIconFactory.getImageIcon("badges/mini-"
					+ b.getFileName(), false));
		}
		label.setText(b.getName());
		label.setSize(label.getWidth(), 32);
		return label;
	}
}
