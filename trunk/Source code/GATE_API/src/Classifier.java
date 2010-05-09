import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Classifier {

	private static ArrayList<String> listPattern = new ArrayList<String>();

	public static void loadPattern() {
		File file = new File("store/Pattern.txt");
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		DataInputStream dis = null;
		try {
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			dis = new DataInputStream(bis);

			while (dis.available() != 0) {
				String temp = dis.readLine();
				if (temp != " " && temp != null) {
					listPattern.add(temp);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
				bis.close();
				dis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	static public String checkPatternQuestion(String q) {
		if (listPattern.size() <= 0) {
			return null;
		}

		for (int i = 0; i < listPattern.size(); i++) {
			String[] s = listPattern.get(i).split("#");
			Pattern temp = Pattern.compile(s[0]);
			Matcher matcher = temp.matcher(q);
			if (matcher.find()) {
				for (int j = 1; j <= matcher.groupCount(); j++) {
					System.out.println(matcher.group(j));
				}
				return s[0];
			}
		}
		System.out.println("---------NULL PATTERN---------");
		return null;
	}
}
