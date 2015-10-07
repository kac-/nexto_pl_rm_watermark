package com.kac.nexto_pl;

import java.io.FileOutputStream;

import com.itextpdf.text.pdf.PRIndirectReference;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class RmWatermark {

	public static void main(String[] args) throws Exception {
		String fileIn = args[0];
		String fileOut = fileIn.substring(0, fileIn.lastIndexOf('.')) + "_no_watermark.pdf";
		PdfReader reader = new PdfReader(fileIn);

		// usunięcie ostatniej strony
		reader.selectPages("1-" + (reader.getNumberOfPages() - 1));

		PdfStamper stp = new PdfStamper(reader, new FileOutputStream(fileOut));
		// usunięcie pośredniego odwołania "Xi0" do x-obiektu zawierającego znak wodny
		PdfDictionary pageDic = reader.getPageN(1);
		PdfDictionary xobjects = pageDic.getAsDict(PdfName.RESOURCES).getAsDict(PdfName.XOBJECT);
		PRIndirectReference ir = (PRIndirectReference) xobjects.get(new PdfName("Xi0"));
		PRStream s = (PRStream) reader.getPdfObject(ir.getNumber());
		s.setData(new byte[] {});

		// zamknięcie plików
		stp.close();
		reader.close();
	}
}
