package utils;

import java.util.ArrayList;

public class GenomeData {
		
		public String name;
		public String path;
		public int taxid;
		public ArrayList<String> chromosomes;
		public ArrayList<String> mitochondrions;
		public ArrayList<String> plasmids;
		public ArrayList<String> chloroplasts;
		
		public GenomeData(){
			this.chromosomes = new ArrayList<String>();
			this.mitochondrions =  new ArrayList<String>();
			this.plasmids =  new ArrayList<String>();
			this.chloroplasts =  new ArrayList<String>();
		};

}
