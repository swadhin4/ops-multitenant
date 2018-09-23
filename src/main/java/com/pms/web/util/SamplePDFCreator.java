package com.pms.web.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringReader;

import com.itextpdf.text.Document;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.PdfWriter;


public class SamplePDFCreator {

	public static void main(String[] args) {

		try {

			String content = "<html><body><img src='D:/GEGDC/SM351137/downloads/your_pic_name.PNG'/></body></html>";
			OutputStream file = new FileOutputStream(new File("D:/TECHM/SM351137/Testapp/sai-app/inventoryapp/target/Test.pdf"));
			Document document = new Document();
			PdfWriter.getInstance(document, file);
			document.open();
			HTMLWorker htmlWorker = new HTMLWorker(document);
			htmlWorker.parse(new StringReader(content));
			document.close();
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
