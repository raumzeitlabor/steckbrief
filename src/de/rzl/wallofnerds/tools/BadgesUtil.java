package de.rzl.wallofnerds.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.rzl.wallofnerds.Badge;

public class BadgesUtil {

	public static List<Badge> getAllBadges() {
		List<Badge> badges = new ArrayList<Badge>();
		File[] badgeImageFiles;
		badgeImageFiles = new File(System.getProperty("user.dir") + "/badges")
				.listFiles();
		for (int i = 0; i < badgeImageFiles.length; i++) {
			File f = badgeImageFiles[i];
			if (!f.getName().substring(0, 4).equals("mini")) {
				Badge b = new Badge(f.getName().substring(0,
						f.getName().length() - 4), f.getName());
				badges.add(b);
			}
		}

		return badges;
	}
}
