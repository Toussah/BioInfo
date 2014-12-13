package nuccore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import sequence.ListSequence;
import erreur.Erreur;

public class ConnectionNuccore {

	private URL site;
	private String site1 = "http://www.ncbi.nlm.nih.gov/sviewer/viewer.cgi?db=nuccore&val=";
	private String site2 = "&retmode=text&report=gbwithparts&log$=seqview"; 
	
	private String id;
	@SuppressWarnings("unused")
	private String filename = "";
	private String name = "";
	private String path = "";
	private String source = "";
	private int debutsource = 0;
	private int finsource = 0;
	
	private String lecture = "";
	private int ligne = 0;
	
	private List<Erreur> lister = new ArrayList<Erreur>();
	private List<String> listcds = new ArrayList<String>();
	private ListSequence listseq = new ListSequence();

	public ConnectionNuccore(String pid, String pname, String ppath) {
		
		id = pid;
		name = pname;
		path = ppath;
	}//ConnectionNuccore
	
	public void readURL() throws Exception {
		
		boolean catchseq = false;
		boolean catchcds = false;
		boolean cdsentier = false;
		
		String seqLine = "";
		int lengthLine = 0;
		int startLine = 0;
		@SuppressWarnings("unused")
		int startAt = 0;
		
		int tmpdeb = 0;
		int tmpfin = 0;
		@SuppressWarnings("unused")
		int nbrcds = 0;
		/****/
		URLConnection uc;
		/****/
		
		try {
			/*******/
			//Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("global.proxy.alcatel-lucent.com", 8000));
			//HttpURLConnection uc =(HttpURLConnection)new URL(site1 + id + site2).openConnection(proxy);
			/*******/
			
			/****/
			site = new URL(site1 + id + site2);
			uc = site.openConnection();
			/****/
			
			uc.setConnectTimeout(360000);
        	BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
  
        	//permet de lire la page web ligne à ligne
        	String inputLine;
        	StringTokenizer tokenline;
        	String tmp = "";
        	while ((inputLine = in.readLine()) != null) {
        		
        		ligne++;
        		if(catchcds == false) {
        			
        			lecture = "ligne " + ligne;
        		}
        		
        		tokenline = new StringTokenizer(inputLine);
        		
	        	if(tokenline.hasMoreTokens()) {
	        		
	        		tmp = tokenline.nextToken();
	        		
	        		//récupérer la source
	        		if(tmp.equals("source")) {
	        			
	        			source = tokenline.nextToken();
	        			
	        			if(estSource(source)) {
	        				
	        			}
	        			else {
	        				
	        				lecture = source;
	        				addErreur("format source invalide");
	        				listcds = new ArrayList<String>();
	        				break;
	        			}
	        		}
	        		
	        		if(catchcds) {
	        			
	        			if(tmp.substring(0, 1).equals("/")) {
	        				
	        				catchcds = false;
	        				cdsentier = true;
	        			}
	        			else {
	        				
	        				lecture = lecture + tmp;
	        			}
	        		}
	        		
	        		if(tmp.equals("CDS")) {
	        			
	        			lecture = tokenline.nextToken();
	        			catchcds= true;
	        		}
	        		
	        		if(cdsentier) {
	        			
	        			if(analyserCDS(lecture)) {
	        				
	        				listcds.add(lecture);
	        			}
	        			
	        			cdsentier = false;
	        		}
	        		
	        		//permet d'arrêter la lecture à la fin de la page
	        		if(tmp.equals("//")) {
	        			
	        			break;
	        		}
	        		
	        		//permet de lire la sequence
	        		if(catchseq) {
	        			
	        			//on trie les séquences (à minima inutile)
	        			//listseq.tri();
	        			
	        			//permet de vérifier que le premier token de la ligne est un chiffre
	        			if(estEntier(tmp) && (Integer.parseInt(tmp) >= debutsource)) {
	        				
	        				//on réinitialise la ligne
	        				seqLine = "";
	        				startLine = Integer.parseInt(tmp);
	        				
	        				//on récupère la ligne de la s�quence
	        				while(tokenline.hasMoreTokens()) {
	        					
	        					seqLine = seqLine + tokenline.nextToken();
	        				}
	        				seqLine = seqLine.toUpperCase();
	        				
	        				//if(estACGT(seqLine)) {
	        				
		        				lengthLine = seqLine.length();
		        				
		        				//vérifier qu'on ne déborde pas en dehors de la source
		        				if(lengthLine <= finsource) {
		        					
		        					for(int k = 0; k < listseq.size(); k++) {
		        						
		        						if(listseq.get(k).estDans(startLine -1, startLine + lengthLine - 1)) {	
		        							
		        							tmpdeb = max(listseq.get(k).getDebut(), startLine) - startLine;
		        							tmpfin = min(listseq.get(k).getFin()+1, startLine+lengthLine) - startLine;
		        							
		        							listseq.get(k).addSubSequence(seqLine.substring(tmpdeb, tmpfin));
		        						}
		        					}
		        				}
		        				else {
		        					
		        					addErreur("erreur taille de la sequence");
		        					listcds = new ArrayList<String>();
		        					break;
		        				}
	        				/*}
	        				else {
	        					
	        					addErreur("lecture d'une chaine non ACGT");
	        					listcds = new ArrayList<String>();
	        					break;
	        				}*/
	        			}
	        			else {
	        				
	        				addErreur("erreur de lecture de la sequence");
	        				listcds = new ArrayList<String>();
	        				break;
	        			}
	        		}
	        		
	        		//permet de reperer le debut de la sequence
	        		if(tmp.equals("ORIGIN")) {
	        			
	        			catchseq = true;
	        		}	
        		}//if tokenline a au moins 1 token
	        	
	        	tokenline = null;
	        	tmp = null;
	        	
	        	
        	}//while lire la page web	
        	in.close();
        	
        	WrongSequence();
        	
		} catch(IOException ioe) {
			ioe.printStackTrace();
			throw ioe;
		}
		catch(Exception e) {
			
			addErreur("une erreur innatendu est survenu");
			e.printStackTrace();
		}
	}//readURL
	
	public boolean analyserCDS(String cds) {
		
		String compl = "complement";
		String join = "join";
		String tmp = cds;
		boolean resultat = true;
		boolean estDecoupable = true;
		boolean estJoin = false;
		int comptec = 0;
		int comptej = 0;
		
		while(estDecoupable) {
			
			//pas de complement et pas de join xxx..xxx
			if(tmp.length() > 1 && estEntier(tmp.substring(0,1))) {
				
				estDecoupable = false;
			}
			//si c'est un complement(*)
			else if(tmp.length() > 10 && tmp.substring(0,10).equals(compl) && (comptec < 1) && (comptej < 1)) {
				
				comptec++;
				tmp = tmp.substring(11, tmp.length()-1);
			}//si c'est un join(*)
			else if(tmp.length() > 4 && tmp.substring(0,4).equals(join) && (comptej < 1)) {
				
				comptej++;
				tmp = tmp.substring(5, tmp.length()-1);
				estJoin = true;
			}
			else {
				
				addErreur("cds contient une écriture incorrect");
				resultat = false;
				estDecoupable = false;
			}
		}

		if(estJoin) {
		
			StringTokenizer st = new StringTokenizer(tmp, ",");
			List<String> listok = new ArrayList<String>();
			
			while(st.hasMoreTokens()) {
				
				listok.add(st.nextToken());
			}
			
			resultat = tokenCDS(listok);
		}
		else {
			
			resultat = tokenCDS(tmp);
		}
		
		compl = null;
		join = null;
		tmp = null;
		
		return resultat;
	}
	
public boolean tokenCDS(String s) {
        
        boolean res = true;
        StringTokenizer tok = new StringTokenizer(s, "..");
        
        String debut = "";
        String fin = "";
        int est = 0;        
        
 
        while(tok.hasMoreTokens()) {
            
            est++;
            
            if(est == 1) {
                
                debut = tok.nextToken();
 
            }
            else if (est == 2) {
                
                fin = tok.nextToken();
 
            }
        }   
        
        if(estEntier(debut) && estEntier(fin) && (est == 2)) {
            
            //si la chaine est en dehors de la source
            if((Integer.parseInt(debut) < debutsource) || (Integer.parseInt(debut) > finsource) || (Integer.parseInt(fin) < debutsource) || (Integer.parseInt(fin) > finsource)) {
                
                addErreur("contient une chaine de lecture hors source");
                res = false;
            }
            else if(Integer.parseInt(fin) < Integer.parseInt(debut)) { //le token est dans l'ordre inverse
                
                addErreur("contient une chaine de lecture inverse");
                res = false;
            }
            else {
 
                listseq.addSequence(s, Integer.parseInt(debut), Integer.parseInt(fin));
                res = true;
            }
        }
        else {
        
            addErreur("contient chaine incorrect");
            res = false;
        }
        
        tok = null;     
        debut = null;
        fin = null; 
        
        return res;
    }
    
    public boolean tokenCDS(List<String> listest) {
        
        boolean res = true;
        int i;
        @SuppressWarnings("unused")
		int debut = debutsource;
        int fin = debutsource;
        String testdebut = "";
        String testfin = "";
        int est = 1;
        boolean test = true;
        
        for(i = 0; i < listest.size(); i++) {
            
            StringTokenizer token = new StringTokenizer(listest.get(i), "..");
            testdebut = "";
            testfin = "";
            est = 0;
            
            while(token.hasMoreTokens()) {
                
                est++;
                
                if(est == 1) {
                    
                    testdebut = token.nextToken();
 
                }
                else if (est == 2) {
                    
                    testfin = token.nextToken();
 
                }               
            }
            
            //si les token sont des entiers
            if(estEntier(testdebut) && estEntier(testfin) && (est == 2)) {
                //si la chaine est en dehors de la source
                if((Integer.parseInt(testdebut) < debutsource) || (Integer.parseInt(testdebut) > finsource) || (Integer.parseInt(testfin) < debutsource) || (Integer.parseInt(testfin) > finsource)) {
                    
                    addErreur("chaine de lecture hors source");
                    test = false;
                    res = false;
                    break;
                }
                else if(Integer.parseInt(testfin) < Integer.parseInt(testdebut)) { //le token est dans l'ordre inverse
                    
                    addErreur("chaine de lecture inverse");
                    test = false;
                    res = false;
                    break;
                }
                else if(Integer.parseInt(testdebut) < fin) { //le debut du token suivant est avant la fin du token précédent
                    
                    addErreur("ordre des tokens incorrect");
                    test = false;
                    res = false;
                    break;
                }
                else {
                
                    debut = Integer.parseInt(testdebut);
                    fin = Integer.parseInt(testfin);
                    res = true;
                }   
            }
            else {
 
                addErreur("Fonction join contient un token incorrect");
                test = false;
                res = false;
                break;
            }
        }
        
        //si tous les tokens étaient bon
        if(test) {
            
            for(i = 0; i < listest.size(); i++) {
                
                listseq.addSequence(listest.get(i));
            }
        }
        
        testdebut = null;
        testfin = null;
        
        return res;
    }
	
	public boolean estSource(String source) {
		
		StringTokenizer tok = new StringTokenizer(source, "..");

		String debut = null;
		String fin = null;
		if(tok.hasMoreTokens()) {
			debut = tok.nextToken();
			fin = tok.nextToken();
		}
		boolean res = false;
		
		
		if(estEntier(debut) && estEntier(fin)) {
			
			debutsource = Integer.parseInt(debut);
			finsource = Integer.parseInt(fin);
			
			if(debutsource > finsource) {
				
				res = false;
			}
			else {
			
				res = true;
			}
		}
		else {
			
			res = false;
		}
		
		tok = null;		
		debut = null;
		fin = null;
		
		return res;
	}//estSource
	
	public boolean estEntier(String test) {
		
		try {
			
			Integer.parseInt(test);
		} catch (NumberFormatException e){return false;}
		
 		return true;
	}//estEntier
	
	public boolean estACGT(String test) {
		
		int i = 0;
		
		for(i = 0; i < test.length()-1; i++) {
			
			if(test.substring(i, i+1).equals("A") || test.substring(i, i+1).equals("C") || test.substring(i, i+1).equals("G") || test.substring(i, i+1).equals("T")) {
				//do nothing
			}
			else {
				
				return false;
			}
		}
		
		return true;
	}
	
	public void WrongSequence() {
		
		for(int i = 0; i < listseq.size(); i++) {
			
			if(estACGT(listseq.get(i).getSequence().toString())) {
				//tout est ok on fait rien
			}
			else {
				//on a un caract�re non pr�vu on vide
				listseq.get(i).setSequence(new StringBuffer());
			}
		}
	}
	
	private void addErreur(String ptype) {
		
		Erreur e = new Erreur(id, name, path, lecture, ptype);
		lister.add(e);
	}//addErreur
	
	public List<Erreur> getListErreur() {
		
		return lister;
	}
	
	public ListSequence getListSequence() {
		
		return listseq;
	}
	
	public List<String> getListCDS() {
		
		return listcds;
	}
	
	public String getID() {
		
		return id;
	}
	
	public String getName() {
		
		return name;
	}
	
	public int min(int a, int b) {
		
		if(a <= b) {
			
			return a;
		}
		else { return b;}
	}
	
	public int max(int a, int b) {
		
		if(a <= b) {
			
			return b;
		}
		else { return a;}
	}
}//class ConnectionNuccore
