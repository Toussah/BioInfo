package excel;

import java.io.File;
import java.util.List;
import java.util.StringTokenizer;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import app.App;

public class WriteExcelThread extends Thread {

	private List<File> filelist;
	private String last;
	private String path;
	
	public WriteExcelThread(List<File> pfilelist, String plast, String ppath) {
		this.filelist = pfilelist;
		this.last = plast;
		this.path = ppath;
	}

	public void run(){
		writeExcel();
	}
	
private void writeExcel() {
		
		double[] test0 = new double [64];
		double[] test1 = new double [64];
		double[] test2 = new double [64];

		double trinucleotides = 0;
		double sequence = 0;
		double genomes = 0;
		
		int pos = 0;
		int posY = 6;
		
		//on aditionne tout les excel chromosomes.xls des enfants
		for(int k = 0; k < filelist.size(); k++) {
			
			try {
				
				Workbook workbookread = Workbook.getWorkbook(filelist.get(k));

				Sheet sheet = workbookread.getSheet(0);

				genomes += Double.parseDouble(sheet.getCell(10, 9).getContents());
				trinucleotides += Double.parseDouble(sheet.getCell(10, 7).getContents());
				sequence += Double.parseDouble(sheet.getCell(10, 5).getContents());


				for(int j = 0; j < 64; j++) {
					
					pos = posY + j;
					
					test0[j] += Double.parseDouble(sheet.getCell(1, pos).getContents());
					test1[j] += Double.parseDouble(sheet.getCell(3, pos).getContents());
					test2[j] += Double.parseDouble(sheet.getCell(5, pos).getContents());
				}
				
			}
			catch(Exception e) {
			
				e.printStackTrace();
			}
		}//for
		
		//on écrit dans chromosome.xls parent
		try {
			
			Workbook workbookmodel = Workbook.getWorkbook(new File("Genomes"+App.FILE_SEP+"model.xls"));
			WritableWorkbook workbook = Workbook.createWorkbook(new File(path + last), workbookmodel);
			
			WritableSheet sheet1 = workbook.getSheet(0);
			
			WritableFont arial16font = new WritableFont(WritableFont.ARIAL, 16); 
			WritableCellFormat arial16format = new WritableCellFormat (arial16font); 
			
			WritableCellFormat format = new WritableCellFormat();

			StringTokenizer token = new StringTokenizer(path, App.FILE_SEP);
			
			int i = 1;
			String tmp = "";
			
			//permet d'�crire le chemin
			while(token.hasMoreTokens()) {
				
				tmp = token.nextToken();
				sheet1.addCell(new Label(i, 1, tmp, format));
				
				i++;
			}
			
			//ajouter le nom
			sheet1.addCell(new Label(1, 0, tmp, arial16format)); 
			
			//�crire le nombre de s�quence, de trinucléotides et de génomes
			sheet1.addCell(new Number(10, 5, sequence, format));
			sheet1.addCell(new Number(10, 7, trinucleotides, format));
			sheet1.addCell(new Number(10, 9, genomes, format));

			double frequence;
			
			//on parcours chaque �l�ment du tableau
			for(int k = 0; k < 64; k++) {
				
				pos = posY + k;
				

				frequence = (double) (test0[k]/ trinucleotides) * 100.0;
				//�crire phase0
				if(test0[k] > 0) {
					sheet1.addCell(new Number(1, pos, test0[k], format));
					sheet1.addCell(new Number(2, pos, frequence, format));
				}
				else {
					sheet1.addCell(new Number(1, pos, 0, format));
					sheet1.addCell(new Number(2, pos, 0, format));
				}
				
				frequence = (double) (test1[k]/ trinucleotides) * 100.0;
				//�crire phase1
				if(test1[k] > 0) {
					sheet1.addCell(new Number(3, pos, test1[k], format));
					sheet1.addCell(new Number(4, pos, frequence, format));
				}
				else {
					sheet1.addCell(new Number(3, pos, 0, format));
					sheet1.addCell(new Number(4, pos, 0, format));
				}
				
				frequence = (double) (test2[k]/ trinucleotides) * 100.0;
				//�crire phase2
				if(test2[k] > 0) {
					sheet1.addCell(new Number(5, pos, test2[k], format));
					sheet1.addCell(new Number(6, pos, frequence, format));
				}
				else {
					sheet1.addCell(new Number(5, pos, 0, format));
					sheet1.addCell(new Number(6, pos, 0, format));
				}
				
			}
			
			workbook.write(); 
			workbook.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}//writeExcel

}
