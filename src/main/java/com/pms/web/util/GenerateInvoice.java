package com.pms.web.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class GenerateInvoice {

	private BaseFont bfBold;
	private BaseFont bf;
	private int pageNumber = 0;

	public static void main(final String[] args) {

		String pdfFilename = "TestPDF.pdf";
		GenerateInvoice generateInvoice = new GenerateInvoice();
		generateInvoice.createPDF(pdfFilename);

	}

	private void createPDF(final String pdfFilename) {
		Document doc = new Document();
		PdfWriter docWriter = null;
		initializeFonts();

		try {
			String path = "D:/TechM/SM351137/Sample-Applications/sai-app/inventoryapp/" + pdfFilename;
			docWriter = PdfWriter.getInstance(doc, new FileOutputStream(path));
			doc.addAuthor("betterThanZero");
			doc.addCreationDate();
			doc.addProducer();
			doc.addCreator("MySampleCode.com");
			doc.addTitle("Invoice");
			doc.setPageSize(PageSize.LETTER);

			doc.open();
			PdfContentByte cb = docWriter.getDirectContent();

			boolean beginPage = true;
			int y = 0;

			for (int i = 0; i < 100; i++) {
				if (beginPage) {
					beginPage = false;
					generateLayout(doc, cb);
					generateHeader(doc, cb);
					y = 615;
				}
				generateDetail(doc, cb, i, y);
				y = y - 15;
				if (y < 50) {
					printPageNumber(cb);
					doc.newPage();
					beginPage = true;
				}
			}
			printPageNumber(cb);

		} catch (DocumentException dex) {
			dex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (doc != null) {
				doc.close();
			}
			if (docWriter != null) {
				docWriter.close();
			}
		}
	}

	private void printPageNumber(final PdfContentByte cb) {
		cb.beginText();
		cb.setFontAndSize(bfBold, 8);
		cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Page No. " + (pageNumber+1), 570 , 25, 0);
		cb.endText();
		pageNumber++;

	}

	private void generateDetail(final Document doc, final PdfContentByte cb, final int index, final int y) {
		DecimalFormat df = new DecimalFormat("0.00");

		try {

			/*createContent(cb,48,y,String.valueOf(index+1),PdfContentByte.ALIGN_RIGHT);
			createContent(cb,52,y, "ITEM" + String.valueOf(index+1),PdfContentByte.ALIGN_LEFT);
			createContent(cb,152,y, "Product Description - SIZE " + String.valueOf(index+1),PdfContentByte.ALIGN_LEFT);

			double price = Double.valueOf(df.format(Math.random() * 10));
			double extPrice = price * (index+1) ;
			createContent(cb,498,y, df.format(price),PdfContentByte.ALIGN_RIGHT);
			createContent(cb,568,y, df.format(extPrice),PdfContentByte.ALIGN_RIGHT);*/

		}

		catch (Exception ex){
			ex.printStackTrace();
		}


	}

	private void createContent(final PdfContentByte cb, final float x, final float y, final String text, final int align) {
		cb.beginText();
		cb.setFontAndSize(bf, 8);
		cb.showTextAligned(align, text.trim(), x , y, 0);
		cb.endText();
	}

	private void generateHeader(final Document doc, final PdfContentByte cb) {
		try {

			createHeadings(cb,200,750,"Company Name");
			createHeadings(cb,200,735,"Address Line 1");
			createHeadings(cb,200,720,"Address Line 2");
			createHeadings(cb,200,705,"City, State - ZipCode");
			createHeadings(cb,200,690,"Country");

			createHeadings(cb,482,743,"ABC0001");
			createHeadings(cb,482,723,"123456");
			createHeadings(cb,482,703,"09/26/2012");

		}

		catch (Exception ex){
			ex.printStackTrace();
		}


	}

	private void createHeadings(final PdfContentByte cb, final float x, final float y, final String text) {
		cb.beginText();
		cb.setFontAndSize(bfBold, 8);
		cb.setTextMatrix(x,y);
		cb.showText(text.trim());
		cb.endText();


	}

	private void generateLayout(final Document doc, final PdfContentByte cb) {
		try {

			cb.setLineWidth(1f);

			// Invoice Header box layout
			/*	cb.rectangle(420,700,150,60);
			cb.moveTo(420,720);
			cb.lineTo(570,720);
			cb.moveTo(420,740);
			cb.lineTo(570,740);
			cb.moveTo(480,700);
			cb.lineTo(480,760);
			cb.stroke();*/

			// Invoice Header box Text Headings
			/*createHeadings(cb,422,743,"Account No.");
			createHeadings(cb,422,723,"Invoice No.");
			createHeadings(cb,422,703,"Invoice Date");*/

			// Invoice Detail box layout
			cb.rectangle(70,60,450,640);
			/*cb.moveTo(20,630);
			cb.lineTo(570,630);
			cb.moveTo(50,50);
			cb.lineTo(50,650);
			cb.moveTo(150,50);
			cb.lineTo(150,650);
			cb.moveTo(430,50);
			cb.lineTo(430,650);
			cb.moveTo(500,50);
			cb.lineTo(500,650);*/
			cb.stroke();

			// Invoice Detail box Text Headings
			/*	createHeadings(cb,22,633,"Qty");
			createHeadings(cb,52,633,"Item Number");
			createHeadings(cb,152,633,"Item Description");
			createHeadings(cb,432,633,"Price");
			createHeadings(cb,502,633,"Ext Price");*/

			//add the images
			//Image companyLogo = Image.getInstance("images/olympics_logo.gif");
			//companyLogo.setAbsolutePosition(25,700);
			//companyLogo.scalePercent(25);
			//doc.add(companyLogo);

		}

		/*catch (DocumentException dex){
			dex.printStackTrace();
		}*/
		catch (Exception ex){
			ex.printStackTrace();
		}

	}

	private void initializeFonts() {
		try {
			bfBold = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}