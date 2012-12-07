package de.rzl.wallofnerds.component;

import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import de.rzl.wallofnerds.Badge;
import de.rzl.wallofnerds.tools.BadgesUtil;

public class BadgeJComboBox extends JComboBox {

	private static final long serialVersionUID = 1L;

	public BadgeJComboBox() {
		super();
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		model.addElement(new Badge("None", null));
		List<Badge> l = BadgesUtil.getAllBadges();
		for (Badge b : l) {
			model.addElement(b);
		}
		this.setModel(model);
		this.setRenderer(new BadgeListCellRenderer());
	}

	public Badge getSelectedBadge() {
		return (Badge) getSelectedItem();
	}
}
