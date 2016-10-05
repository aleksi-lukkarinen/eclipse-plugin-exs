package com.packtpub.e4.minimark.ui;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;




public class MinimarkTranslator {

	public static void convert(Reader reader, Writer writer) throws IOException {
		BufferedReader lines = new BufferedReader(reader);

		String title = lines.readLine();
		if (title != null)
			title = title.trim();
		if (title == null || title.length() < 1) {
			title = "[No title given]";
		}

		writer.write("<html>\n");
		writer.write("  <head>\n");
		writer.write("    <title>" + title + "</title>\n");
		writer.write("  </head>\n");
		writer.write("  <body>\n");
		writer.write("    <h1>" + title + "</h1>\n");
		writer.write("    <p>\n");

		String line;
		while ((line = lines.readLine()) != null) {
			if (line.equals("")) {
				writer.write("    </p>\n");
				writer.write("    <p>\n");
			}
			else {
				writer.write("      " + line + "\n");
			}
		}

		writer.write("    </p>\n");
		writer.write("  </body>\n");
		writer.write("</html>\n");

		writer.flush();
	}

}
