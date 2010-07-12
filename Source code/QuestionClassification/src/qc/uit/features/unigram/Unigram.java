package qc.uit.features.unigram;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Unigram {
	private static String _punctuation_marks = "?<>,:;\"'`()_,&!";

	public static String[] getUnigrams(String sentence) {
		String[] result = null;
		result = processWords(sentence).split(" ");
		return result;
	}

	private static String processWords(String str) {
		String[] words = str.split(" ");
		String result = "";
		for (String word : words) {
			if(word.equals(":"))
				break;
			// System.out.println("process at word : " + word);
			if (!word.equals("What")
					&& !word.equals("How")
					&& !word.equals("Who")
					&& !word.equals("When")
					&& !word.equals("Where")
					&& !word.equals("Why")
					&& !word.equals("Which")
					&& !word.equals("Whose")
					&& !word.equals("Name")) {
				if (Pattern.matches("[A-Z].*",word )) {
					// System.out.println("MATCHED PRONOUN!");
					continue;
				}
			}
			String tempword = word.trim();
			tempword = getBaseWord(tempword);
			tempword = removeSymbol(tempword, _punctuation_marks);
			tempword = tempword.trim();
			if (tempword.equals("") || tempword.equals("-")
					|| tempword.equals("$") || tempword.equals("."))
				continue;
			tempword = formatNumToString(tempword);
			if (tempword.equals("") || tempword.equals("-")
					|| tempword.equals("$") || tempword.equals("."))
				continue;
			result += tempword.toLowerCase() + " ";
		}
		return result.trim();
	}

	private static String getBaseWord(String word) {
		String wordtemp = word;
		if (wordtemp.endsWith("ous"))
			return wordtemp;
		if (wordtemp.endsWith("cs"))
			return wordtemp;
		if (wordtemp.endsWith("'s"))
			wordtemp = wordtemp.substring(0, wordtemp.length() - 2);
		if (wordtemp.endsWith("ies"))
			wordtemp = wordtemp.substring(0, wordtemp.length() - 3) + "y";
		if (wordtemp.endsWith("ss"))
			return word;
		if (wordtemp.isEmpty())
			return wordtemp;
		if (wordtemp.getBytes()[0] < 97)// first character is upcase
			return wordtemp;
		Pattern pattern1 = Pattern.compile("(\\w+)(o|sh|ch|f|s|z|x)es");
		Pattern pattern2 = Pattern.compile("(\\w+)s");
		Matcher matcher = pattern1.matcher(wordtemp);
		if (matcher.matches()) {
			wordtemp = matcher.group(1) + matcher.group(2);
		} else {
			matcher = pattern2.matcher(wordtemp);
			if (matcher.matches() 
					&& !wordtemp.equals("has")
					&& !wordtemp.equals("his")
					&& !wordtemp.equals("was")) {
				wordtemp = matcher.group(1);
				if (wordtemp.length() == 1)// have one character
					wordtemp = word;
			}
		}
		if (wordtemp.endsWith("."))
			wordtemp = wordtemp.substring(0, wordtemp.length() - 1);
		return wordtemp;
	}

	private static String removeSymbol(String str, String symbolStr) {
		char[] sbs = symbolStr.toCharArray();
		char[] chs = str.toCharArray();
		String result = "";
		for (char ch : chs) {
			boolean is_symbol = false;
			for (char sch : sbs) {
				if (ch == sch) {
					is_symbol = true;
					break;
				}
			}
			if (!is_symbol) {
				result += ch;
			}
		}
		return result;
	}

	private static String formatNumToString(String numWord) {
		String result = numWord;
		try {
			int i = Integer.parseInt(result);
			return "number";
		} catch (Exception e) {
			// do nothing
		}
		if (Pattern.matches("\\d+th-century", result))
			return "century";
		if (Pattern.matches("\\d+th", result)
				|| Pattern.matches("#\\d", result)
				|| Pattern.matches("\\d+nd", result))
			return "order";
		if (Pattern.matches("\\d+-hour", result)
				|| Pattern.matches("\\d+-minute", result))
			return "period";
		if (Pattern.matches("\\b(\\d\\d\\d\\d)-(\\d\\d\\d\\d)\\b", result))
			return "period";
		if (Pattern.matches("\\b(\\d\\d\\d\\d)-(\\d\\d\\d)\\b", result))
			return "time";
		if (Pattern.matches("\\b(\\d\\d)-(\\d\\d)\\b", result))
			return "period";
		if (Pattern.matches("\\b(\\d+)-(\\d+)-(\\d+)\\b", result))
			return "date";
		if (Pattern.matches("\\$\\d+", result)
				|| Pattern.matches("\\d+-cent", result)
				|| Pattern.matches("\\d+-a-year", result))
			return "money";
		if (Pattern.matches("\\d+-foot-square", result))
			return "size";
		if (Pattern.matches("\\b\\d+s\\b", result))
			return "year";
		if (Pattern.matches("\\d+-mile", result))
			return "distance";
		if (Pattern.matches("\\d+-year-old", result))
			return "age";
		if (Pattern.matches("\\d+%", result))
			return "percent";
		if (Pattern.matches("\\b(\\d+)\\.(\\d+)\\b", result))
			return "number";
		return result;
	}
}
