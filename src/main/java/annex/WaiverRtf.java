
package annex;

import java.util.List;
import java.util.Enumeration;
import java.sql.*;
import java.io.*;
import javax.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.lowagie.text.*;
import com.lowagie.text.rtf.*;
import com.lowagie.text.rtf.table.RtfCell;
import com.lowagie.text.rtf.headerfooter.*;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class WaiverRtf extends HttpServlet {

    static Logger logger = LogManager.getLogger(WaiverRtf.class);		
    static final long serialVersionUID = 70L;
    String url="";
    boolean debug = false;
    String fileAndPath = "waiver.rtf";
    /**
     * Generates the request form.
     * @param req
     * @param res
     * @throws ServletException
     * @throws IOException
     */
    public void doGet(HttpServletRequest req, 
											HttpServletResponse res) 
				throws ServletException, IOException {
				doPost(req, res);
    }

    public void doPost(HttpServletRequest req, 
											 HttpServletResponse res) 
				throws ServletException, IOException{
				res.setContentType("text/html");

				String attorneyFullName="";
				String name, value, message="", id="";

				boolean success=true;
				Enumeration<String> values = req.getParameterNames();
				String [] vals;
				while (values.hasMoreElements()){

						name = values.nextElement().trim();
						vals = req.getParameterValues(name);
						value = vals[vals.length-1].trim();	
						if (name.equals("id")) {
								id=value; // waiver id
						}
				}
				url    = getServletContext().getInitParameter("url");
				String str = getServletContext().getInitParameter("debug");
				if(str != null && str.equals("true")) debug = true;
				str = getServletContext().getInitParameter("attorneyFullName");
				if(str != null)
						attorneyFullName = str;
				User user = null;
				HttpSession session = req.getSession(false);
				if(session != null){
						user = (User)session.getAttribute("user");
						if(user == null){
								str = url+"Login";
								res.sendRedirect(str);
								return; 
						}
				}
				else{
						str = url+"Login";
						res.sendRedirect(str);
						return; 
				}
				logger.debug(" waiver rtf ");
				//
				/// Release judgment
				//
				if(true){
						String msg = "";
						Waiver waiver = null;
						List<Entity> owners = null;
						if(!id.equals("")){
								Waiver one = new Waiver(debug, id);
								msg = one.doSelect();
								if(msg.equals("")){
										if(!one.hasEntities()){
												msg = "No owner(s) in this waiver ";
										}
										else{
												waiver = one;
												owners = waiver.getEntities();
										}
								}
						}
						try{
								boolean isBusiness = false;
								for(Entity one:owners){
										if(one.getIsBusiness()){
												isBusiness = true;
												break;
										}
								}
								if(isBusiness && owners.size() < 2){
										msg = "The business must have at least one agent, add an agent to the list of entities of the waiver then try to run this again";
								}
								if(waiver == null || !msg.equals("")){
										PrintWriter out = res.getWriter();
										out.println("<head><title></title></head>");
										out.println("<body>");
										if(!msg.equals("")){

										}
										else {
												msg = "No waiver match found ";
										}
										out.println("<p style='text-align:center'>"+msg+"</p>");										
										out.println("</body>");
										out.println("</html>");
										out.flush();										
										return;
								}
								else{
										// A4 8.3" x 11" 612, 792, legal: 8.5" x 14" 612x1008
										Rectangle pageSize = new Rectangle(612, 1008);//legal 8.5" X 14"
										// left right top bottom
										// Document document = new Document();
										Document document = new Document(pageSize, 63, 54, 54, 18);// 18,18,54,35
										ServletOutputStream out = null;
										ByteArrayOutputStream baos = new ByteArrayOutputStream();
										RtfWriter2 writer = RtfWriter2.getInstance(document, baos);
										
										document.open();										
										//
										if(isBusiness){
												Entity businessEntity = owners.get(0);
												for(int jj=1;jj<owners.size();jj++){
														Entity agent = owners.get(jj);
														/**
														writeCorporateAuthority(document,
																										businessEntity,
																										agent);
														document.newPage();
														*/
														writeBusinessWaiver(document,
																								waiver,
																								businessEntity,
																								agent,
																								attorneyFullName);
														// we print more than one because
														// we do not know who is signing
														if(owners.size() > 2){
																document.newPage();	
														}
												}
										}
										else{
												writeWaiver(document,
																		waiver,
																		owners,
																		attorneyFullName);
										}
										document.close();
										writer.close();
										String today = Helper.getToday();
										today = today.replaceAll("/","_");
										String filename = "waiver_"+today+".rtf";
										res.setHeader("Expires", "0");
										res.setHeader("Cache-Control", 
																	"must-revalidate, post-check=0, pre-check=0");
										res.setHeader("Pragma", "public");
										res.setHeader("Content-Disposition","attachment;filename="+filename);
										//
										// setting the content type
										res.setContentType("application/msword");
										//
										// the contentlength is needed for MSIE!!!
										res.setContentLength(baos.size());
										out = res.getOutputStream();
										if(out != null){
												baos.writeTo(out);
										}								
								}
						}
						catch(Exception ex){
								logger.error(ex);
						}
				}
    }
		/**
			 // not needed anymore
    void writeCorporateAuthority(Document document,
																 Entity businessEntity,
																 Entity sgent){
				Font fnt,fnts,fntb,fntu,fntbu;
				int pp_indent = 20;
				try{
						fnt = FontFactory.getFont("Times-New-Roman",12, Font.NORMAL);
						fnts = FontFactory.getFont("Times-New-Roman", 10, Font.NORMAL);
						fntb = FontFactory.getFont("Times-New-Roman", 12, Font.BOLD);
						fntu = FontFactory.getFont("Times-New-Roman", 12, Font.UNDERLINE);
						fntbu = FontFactory.getFont("Times-New-Roman", 14, Font.BOLD | Font.UNDERLINE);						
						//
						// space
						Phrase spacePhrase = new Phrase();
						Chunk ch = new Chunk(" \n\n\n", fnt);
						spacePhrase.add(ch);
						document.add(spacePhrase);
						//
						Phrase phrase = new Phrase();
						ch = new Chunk("CORPORATE AUTHORITY TO SIGN",fntbu);
						phrase.add(ch);
						Paragraph pp = new Paragraph();
						pp.setAlignment(Element.ALIGN_CENTER);
						pp.add(phrase);
						document.add(pp);
						//

						ch = new Chunk("To whom It May Concern,\n\n I swear and affirm that I am the President of "+businessEntity.getName()+", _____, and all officers in "+businessEntity.getName()+", _____ have the authority to sign for and on behalf of "+businessEntity.getName()+", ______\n\n  I do hereby swear and affirm that Mr./Ms. _____________________ is the Chief Financial Officer/President for "+businessEntity.getName()+", _____, and that he/she has the authority to sign for and on behalf of "+businessEntity.getName()+", _____, and by his/her signature to thereby bind "+businessEntity.getName()+", _____.\n\n", fnt);
						phrase = new Phrase(ch);						
						pp = new Paragraph();
						pp.setIndentationLeft(20);
						pp.setFirstLineIndent(pp_indent);
						pp.setAlignment(Element.ALIGN_LEFT);
						pp.add(phrase);
						document.add(pp);
						//
						Table table = new Table(2);
						table.setBorder(0);
						table.getDefaultCell().setBorder(0);
						table.setWidth(100);
						phrase = new Phrase();
						ch = new Chunk(" ",fnt);
						phrase.add(ch);
						//
						RtfCell cell = new RtfCell(phrase);
						cell.setBorder(0);
						table.addCell(cell);
						phrase = new Phrase();
						ch = new Chunk("So Signed this ___ day of ____, 20___",fnt);
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(0);
						table.addCell(cell);
						//
						phrase = new Phrase();
						ch = new Chunk(" ",fnt);
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(0);
						table.addCell(cell);
						
						phrase = new Phrase();
						ch = new Chunk(" _________________ President,",fnt);
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(0);
						table.addCell(cell);
						//
						phrase = new Phrase();
						ch = new Chunk(" ",fnt);
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(0);
						table.addCell(cell);
						
						phrase = new Phrase();
						ch = new Chunk(" ___________________Printed Name of President",fnt);
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(0);
						table.addCell(cell);						
						//
						phrase = new Phrase();
						ch = new Chunk(" ",fnt);
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(0);
						table.addCell(cell);
						
						phrase = new Phrase();
						ch = new Chunk(businessEntity.getName()+", ______",fnt);
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(0);
						table.addCell(cell);
						phrase = new Phrase();
						ch = new Chunk("STATE OF INDIANA ",fnt);
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(0);
						table.addCell(cell);
						//						
						phrase = new Phrase();
						ch = new Chunk(") ",fnt);
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(Rectangle.NO_BORDER);
						table.addCell(cell);

						phrase = new Phrase();
						ch = new Chunk(" ",fnt);
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(Rectangle.NO_BORDER);
						table.addCell(cell);

						phrase = new Phrase();
						ch = new Chunk(") SS:",fnt);
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(Rectangle.NO_BORDER);
						table.addCell(cell);
						//
						// third row
						ch = new Chunk("COUNTY OF MONROE",fnt);
						phrase = new Phrase();
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(Rectangle.NO_BORDER);
						table.addCell(cell);
						// 
						ch = new Chunk(")",fnt);
						phrase = new Phrase();
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(Rectangle.NO_BORDER);
						table.addCell(cell);
						//
						document.add(table);
						//
						phrase = new Phrase();
						
						ch = new Chunk("Before me, a Notary Public in and for said County and State, this ______ day of ____, 20___, personally appeared _______________________, as the President of "+businessEntity.getName()+", _____, who acknowledged the free and voluntary execution of the foregoing instrument.  ",fnt);
						phrase = new Phrase();
						phrase.add(ch);
						pp.setIndentationLeft(0); // 
						pp.setFirstLineIndent(pp_indent);
						pp.setAlignment(Element.ALIGN_LEFT);
						pp.add(phrase);
						document.add(pp);
						//
						table = new Table(2);
						table.setWidth(100);
						table.getDefaultCell().setBorder(0);
						table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						phrase = new Phrase();
						ch = new Chunk("My Commission Expires:__________",fnt);
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(Rectangle.NO_BORDER);
						table.addCell(cell);						
	    
						phrase = new Phrase();
						ch = new Chunk("Signed:__________________",fnt);
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(Rectangle.NO_BORDER);
						table.addCell(cell);
	    
						phrase = new Phrase();
						ch = new Chunk("_________________________",fnt);
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(Rectangle.NO_BORDER);
						table.addCell(cell);
						
						phrase = new Phrase();
						ch = new Chunk("Name Printed:__________________",fnt);
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(Rectangle.NO_BORDER);
						table.addCell(cell);
	    
						phrase = new Phrase();
						ch = new Chunk("NP#:__________________",fnt);
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(Rectangle.NO_BORDER);
						table.addCell(cell);	    
						phrase = new Phrase();	    
						ch = new Chunk("                 Notary Public",fnt);
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(Rectangle.NO_BORDER);
						table.addCell(cell);
						phrase = new Phrase();
						ch = new Chunk(" ",fnt);
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(Rectangle.NO_BORDER);
						table.addCell(cell);
						//
						ch = new Chunk("Residing in             , Indiana\n\n",fnt);
						phrase.add(ch);						
						cell = new RtfCell(phrase);
						cell.setBorder(Rectangle.NO_BORDER);
						table.addCell(cell);
						//
						document.add(table);
				}
				catch(Exception ex){
						logger.error(ex);
				}						
		}
		*/
    void writeWaiver(Document document,
										 Waiver waiver,
										 List<Entity> owners,
										 String attorneyFullName){
				//
				String ownerNames = owners.get(0).getName();
				if(owners.size() > 1){
						for(int jj=1;jj<owners.size();jj++){
								Entity one = owners.get(jj);
								// for more than 2
								if(jj >= 1 && jj < owners.size() - 1){
										ownerNames += ", ";
								}
								else {
										ownerNames += " and ";
								}
								ownerNames += one.getName();								
						}
				}
				System.err.println(" owners "+ownerNames);
				int pp_indent = 20;
				Font fnt,fnts,fntb,fntu,fntbu;
				try{
						fnt = FontFactory.getFont("Times-New-Roman",12, Font.NORMAL);
						fnts = FontFactory.getFont("Times-New-Roman", 8, Font.NORMAL);
						fntb = FontFactory.getFont("Times-New-Roman", 12, Font.BOLD);
						fntu = FontFactory.getFont("Times-New-Roman", 12, Font.UNDERLINE);
						fntbu = FontFactory.getFont("Times-New-Roman", 14, Font.BOLD | Font.UNDERLINE);						
						//
						// space
						Phrase spacePhrase = new Phrase();
						Chunk ch = new Chunk(" \n\n\n", fnt);
						spacePhrase.add(ch);
						document.add(spacePhrase);
						//
						Phrase phrase = new Phrase();
						ch = new Chunk("WAIVER OF PROTEST OF ANNEXATION ",fntbu);
						phrase.add(ch);
						ch = new Chunk("\n", fnt);
						phrase.add(ch);
						Paragraph pp = new Paragraph();
						pp.setAlignment(Element.ALIGN_CENTER);
						pp.add(phrase);
						document.add(pp);
						//
						phrase = new Phrase();
						ch = new Chunk("The undersigned "+ownerNames+", as owner(s) of the real estate hereinafter described, for and in consideration of the City of Bloomington, Indiana, granting to the undersigned the right to tap into and connect to the sewer system of the City of Bloomington for the purpose of providing sewer service to the described real estate, now release the right of the undersigned as owner(s) of the described real estate and their successors in title to remonstrate against any pending or future annexation by the City of Bloomington, Indiana, of such described real estate for a period of fifteen (15) years from the date of filing this instrument.  The real estate to be served by such sewers and the real estate for which the right of remonstrance against pending or future annexation to the City of Bloomington is released, is described as follows:",fnt);
						
						phrase.add(ch);
						pp = new Paragraph();
						pp.setIndentationLeft(0);
						pp.setFirstLineIndent(pp_indent);
						pp.setAlignment(Element.ALIGN_LEFT);
						pp.add(phrase);
						document.add(pp);
						//
						int withs[] = {30,70};
						Table table = new Table(2);
						table.setBorder(0);
						table.getDefaultCell().setBorder(0);
						table.setWidth(100);
						table.setWidths(withs);
						
						phrase = new Phrase();
						ch = new Chunk("Service Address:",fntu);
						phrase.add(ch);						
						RtfCell cell = new RtfCell(phrase);
						cell.setBorder(0);
						table.addCell(cell);

						//
						phrase = new Phrase();
						ch = new Chunk(waiver.getAddressInfo(),fnt);
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(0);
						table.addCell(cell);						
						//
						phrase = new Phrase();
						ch = new Chunk("Legal Description:",fntu);
						phrase.add(ch);						
						cell = new RtfCell(phrase);
						cell.setBorder(0);
						table.addCell(cell);						
						//
						phrase = new Phrase();
						ch = new Chunk(waiver.getLegalDescription(),fnt);
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(0);
						table.addCell(cell);
						//
						phrase = new Phrase();
						ch = new Chunk("Deed Instrument Nos.:",fntu);
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(0);
						table.addCell(cell);
						//
						phrase = new Phrase();
						ch = new Chunk(waiver.getDeedInstrumentNum(),fnt);
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(0);
						table.addCell(cell);
						//
						String str="", str2 = waiver.getParcelPin();
						if(str2 != null && !str2.equals("")){
								str = "Parcel Pin #:";
								phrase = new Phrase();
								ch = new Chunk(str,fntu);
								phrase.add(ch);
								cell = new RtfCell(phrase);
								cell.setBorder(0);
								table.addCell(cell);
								phrase = new Phrase();
								ch = new Chunk(str2,fnt);
								phrase.add(ch);
								cell = new RtfCell(phrase);
								cell.setBorder(0);
								table.addCell(cell);
						}
						str2 = waiver.getParcelTaxId();
						if(str2 != null && !str2.equals("")){
								str = "Parcel Tax ID #:";
								phrase = new Phrase();
								ch = new Chunk(str,fntu);
								phrase.add(ch);
								cell = new RtfCell(phrase);
								cell.setBorder(0);
								table.addCell(cell);
								phrase = new Phrase();
								ch = new Chunk(str2,fnt);
								phrase.add(ch);
								cell = new RtfCell(phrase);
								cell.setBorder(0);
								table.addCell(cell);					
						}
						//
						document.add(table);
						//
						phrase = new Phrase();
						if(owners != null && owners.size() == 1){
								ch = new Chunk("\nSigned:(Owner)\n",fnt);
						}
						else{
								ch = new Chunk("\nSigned:(Owner(s))\n",fnt);
						}
						phrase.add(ch);
						document.add(phrase);
						phrase = new Phrase();						
						for(Entity owner:owners){
								ch = new Chunk("__________________"+owner.getName()+", Owner ",fnt);
										phrase.add(ch);								
						}
						ch = new Chunk("\n", fnt);
						phrase.add(ch);
						document.add(phrase);						
						//
						phrase = new Phrase();
						ch = new Chunk("Witnessed:\n"+
													 "Executed and Delivered in my presence. Witness further attest Witness is not a party to this transaction and will not receive any interest in or proceeds from the property.\n"+
													 "_______________________ Witness Signature      _______________________ Witness Printed Name\n",fnt);
						phrase.add(ch);
						pp = new Paragraph();
						pp.setIndentationLeft(0);
						pp.setFirstLineIndent(pp_indent);
						pp.setAlignment(Element.ALIGN_LEFT);
						pp.add(phrase);
						document.add(pp);
						// 
						Table ssTable = new Table(2);
						ssTable.setWidth(100);						
						ssTable.getDefaultCell().setBorder(0);
						ssTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						phrase = new Phrase();
						ch = new Chunk("STATE OF INDIANA ",fnt);
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(0);
						ssTable.addCell(cell);
						//						
						phrase = new Phrase();
						ch = new Chunk(") ",fnt);
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(Rectangle.NO_BORDER);
						ssTable.addCell(cell);
						
						phrase = new Phrase();
						ch = new Chunk(" ",fnt);
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(Rectangle.NO_BORDER);
						ssTable.addCell(cell);
						
						phrase = new Phrase();
						ch = new Chunk(") SS:",fnt);
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(Rectangle.NO_BORDER);
						ssTable.addCell(cell);
						//
						// third row
						ch = new Chunk("COUNTY OF MONROE",fnt);
						phrase = new Phrase();
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(Rectangle.NO_BORDER);
						ssTable.addCell(cell);
						// 
						ch = new Chunk(")",fnt);
						phrase = new Phrase();
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(Rectangle.NO_BORDER);
						ssTable.addCell(cell);
						//
						document.add(ssTable);
						//
						phrase = new Phrase();

						ch = new Chunk("Before me, a Notary Public, personally appeared "+ownerNames+" and acknowledged the execution of the above release of the right to remonstrate against pending or future annexation to the City of Bloomington, Indiana, to be his voluntary act and deed, this day of ____________, 20__.",fnt);						
						//
						phrase.add(ch);
						pp = new Paragraph();
						pp.setIndentationLeft(0);
						pp.setFirstLineIndent(pp_indent);
						pp.setAlignment(Element.ALIGN_LEFT);
						pp.add(phrase);
						document.add(pp);
						//
						//
						Table noteTable = new Table(2);
						noteTable.setWidth(100);
						noteTable.getDefaultCell().setBorder(0);
						noteTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						
						phrase = new Phrase();
						ch = new Chunk("My Commission Expires:__________",fnt);
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(Rectangle.NO_BORDER);
						noteTable.addCell(cell);						
						
						phrase = new Phrase();
						ch = new Chunk("Signed:__________________",fnt);
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(Rectangle.NO_BORDER);
						noteTable.addCell(cell);
										
						phrase = new Phrase();
						ch = new Chunk("NP#:______________ ",fnt);
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(Rectangle.NO_BORDER);
						noteTable.addCell(cell);
						
						phrase = new Phrase();
						ch = new Chunk("Name Printed:__________________ Notary Public",fnt);
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(Rectangle.NO_BORDER);
						noteTable.addCell(cell);
						
						phrase = new Phrase();
						ch = new Chunk(" ",fnt);
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(Rectangle.NO_BORDER);
						noteTable.addCell(cell);
						
						phrase = new Phrase();
						ch = new Chunk("Residing in Monroe, Indiana\n",fnt);
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(Rectangle.NO_BORDER);
						noteTable.addCell(cell);
						document.add(noteTable);
						//
						document.add(ssTable);
						
						phrase = new Phrase();
						ch = new Chunk("Before me, a Notary Public in and for said County and State, personally appeared ______________(witness), being known or proved to me to be the person whose name is subscribed as a witness above to the foregoing document execution and delivery by "+ownerNames+", and swore that they are not a party to and will not receive any interest in the transaction this ____ day of __________, 20__.\n",fnt);
						phrase.add(ch);
						document.add(phrase);
						//
						document.add(noteTable);
						// 
						// 
						phrase = new Phrase();
						ch = new Chunk("I affirm under penalties of perjury that I have taken reasonable care to redact each Social Security Number in this document, unless required by law.\n",fnts);
						phrase.add(ch);
						ch = new Chunk("                         ______________\n",fnts);
						phrase.add(ch);
						ch = new Chunk("This instrument prepared by "+attorneyFullName+", Attorney at Law, City of Bloomington Legal Department, P. O. Box 100, Bloomington, IN  47402",fnts);
						phrase.add(ch);
						pp = new Paragraph();
						pp.setIndentationLeft(0);
						// pp.setFirstLineIndent(pp_indent);
						pp.setAlignment(Element.ALIGN_CENTER);
						pp.add(phrase);
						document.add(pp);
				}
				catch(Exception ex){
						logger.error(ex);
				}
    }
    void writeBusinessWaiver(Document document,
														 Waiver waiver,
														 Entity businessEntity,
														 Entity agent,
														 String attorneyFullName){
				//
				int pp_indent = 20;
				Font fnt,fnts,fntb,fntu,fntbu;
				try{
						fnt = FontFactory.getFont("Times-New-Roman",12, Font.NORMAL);
						fnts = FontFactory.getFont("Times-New-Roman", 10, Font.NORMAL);
						fntb = FontFactory.getFont("Times-New-Roman", 12, Font.BOLD);
						fntu = FontFactory.getFont("Times-New-Roman", 12, Font.UNDERLINE);
						fntbu = FontFactory.getFont("Times-New-Roman", 14, Font.BOLD | Font.UNDERLINE);						
						//
						// space
						Phrase spacePhrase = new Phrase();
						Chunk ch = new Chunk(" \n\n\n", fnt);
						spacePhrase.add(ch);
						document.add(spacePhrase);
						//
						Phrase phrase = new Phrase();
						ch = new Chunk("WAIVER OF PROTEST OF ANNEXATION ",fntbu);
						phrase.add(ch);
						ch = new Chunk("\n", fnt);
						phrase.add(ch);	    
						Paragraph pp = new Paragraph();
						pp.setAlignment(Element.ALIGN_CENTER);
						pp.add(phrase);
						document.add(pp);
						//
						phrase = new Phrase();

						ch = new Chunk("The undersigned "+agent.getName()+", as authorized agent for "+businessEntity.getName()+" LLC, an Indiana limited liability corporation and Owner of the real estate hereinafter described, for and in consideration of the City of Bloomington, Indiana, granting to the Owner the right to tap into and connect to the sewer system of the City of Bloomington for the purpose of providing sewer service to the described real estate, now release the right of the Owner of the described real estate, and its successors in title, to remonstrate against any pending or future annexation by the City of Bloomington, Indiana, of such described real estate for a period of fifteen (15) years from the date of filing this instrument.  The person signing below on behalf of Owner represents and certifies that he or she has full authority to execute this Waiver of Protest of Annexation on behalf of Owner.  The real estate to be served by such sewers and the real estate for which the right of remonstrance against pending or future annexation to the City of Bloomington is released, is described as follows:",fnt);
						phrase.add(ch);
						pp = new Paragraph();
						pp.setIndentationLeft(0);
						pp.setFirstLineIndent(pp_indent);
						pp.setAlignment(Element.ALIGN_LEFT);
						pp.add(phrase);
						document.add(pp);
						//
						Table table = new Table(2);
						int withs[] = {40,60};
						table.setWidths(withs);
						table.setWidth(100);						
						table.setBorder(0);
						table.getDefaultCell().setBorder(0);

						phrase = new Phrase();
						ch = new Chunk("Service Address:",fnt);
						phrase.add(ch);						
						RtfCell cell = new RtfCell(phrase);
						cell.setBorder(0);
						table.addCell(cell);

						//
						phrase = new Phrase();
						ch = new Chunk(waiver.getAddressInfo(),fntu);
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(0);
						table.addCell(cell);						
						//
						phrase = new Phrase();
						ch = new Chunk("Legal Description:",fnt);
						phrase.add(ch);						
						cell = new RtfCell(phrase);
						cell.setBorder(0);
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);						
						//
						phrase = new Phrase();
						ch = new Chunk(waiver.getLegalDescription(),fntu);
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(0);
						table.addCell(cell);
						//
						phrase = new Phrase();
						ch = new Chunk("Deed Instrument Nos.:",fnt);
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(0);
						table.addCell(cell);
						//
						phrase = new Phrase();
						ch = new Chunk(waiver.getDeedInstrumentNum(),fntu);
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(0);
						table.addCell(cell);
						//
						String str="", str2 = waiver.getParcelPin();
						if(str2 != null && !str2.equals("")){
								str = "Parcel Pin #:";
								phrase = new Phrase();
								ch = new Chunk(str,fnt);
								phrase.add(ch);
								cell = new RtfCell(phrase);
								cell.setBorder(0);
								table.addCell(cell);
								//
								phrase = new Phrase();
								ch = new Chunk(str2,fntu);
								phrase.add(ch);
								cell = new RtfCell(phrase);
								cell.setBorder(0);
								table.addCell(cell);
						}
						str2 = waiver.getParcelTaxId();
						if(str2 != null && !str2.equals("")){								
								str = "Parcel ID #:";
								phrase = new Phrase();
								ch = new Chunk(str,fnt);
								phrase.add(ch);
								cell = new RtfCell(phrase);
								cell.setBorder(0);
								table.addCell(cell);
								//
								phrase = new Phrase();
								ch = new Chunk(str2,fntu);
								phrase.add(ch);
								cell = new RtfCell(phrase);
								cell.setBorder(0);
								table.addCell(cell);						
						}
						//
						document.add(table);
						//
						phrase = new Phrase();
						ch = new Chunk("The undersigned as an officer for "+businessEntity.getName()+" further SWEARS and AFFIRMS to be the President (___) of "+businessEntity.getName()+", LLC, and all officers in "+businessEntity.getName()+", LLC have the authority to sign for and on behalf of "+businessEntity.getName()+", LLC.", fnt);
						phrase.add(ch);
						pp = new Paragraph();
						pp.setAlignment(Element.ALIGN_LEFT);
						pp.add(phrase);
						document.add(pp);
						//
						table = new Table(2);
						table.setBorder(0);
						table.getDefaultCell().setBorder(0);
						table.setWidth(100);
						

						//
						phrase = new Phrase();
						ch = new Chunk("OWNER: ",fnt);
						phrase.add(ch);						
						ch = new Chunk(businessEntity.getName(),fntu);
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(0);
						table.addCell(cell);						

						
						ch = new Chunk("By: ______________",fnt);
						phrase = new Phrase();
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(0);
						table.addCell(cell);

						// empty cell
						ch = new Chunk(" ",fnt);
						phrase = new Phrase();
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(0);
						table.addCell(cell);						
						//
						phrase = new Phrase();
						ch = new Chunk("  Name Printed:         \nTitle:_______________",fnt);						
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(0);
						table.addCell(cell);
						document.add(table);
						//
						
						//
						phrase = new Phrase();
						ch = new Chunk("Witnessed:\n Executed and Delivered in my presence. Witness further attest Witness is not a party to this transaction and will not receive any interest in or proceeds from the property.\n\n",fnt);
						pp = new Paragraph();
						pp.setAlignment(Element.ALIGN_LEFT);
						pp.add(phrase);
						document.add(pp);
						//
						phrase = new Phrase();
						phrase.add(ch);						
						ch = new Chunk("_____________________Witness Signature   ",fnt);
						phrase.add(ch);
						ch = new Chunk("_____________________Witness Printed Name",fnt);
						phrase.add(ch);
						document.add(phrase);
						//
						
						//
						Table contTable = new Table(2);
						contTable.setWidth(100);						
						contTable.getDefaultCell().setBorder(0);
						contTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

						phrase = new Phrase();
						ch = new Chunk("STATE OF INDIANA ",fnt);
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(0);
						contTable.addCell(cell);
						//						
						phrase = new Phrase();
						ch = new Chunk(") ",fnt);
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(Rectangle.NO_BORDER);
						contTable.addCell(cell);

						phrase = new Phrase();
						ch = new Chunk(" ",fnt);
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(Rectangle.NO_BORDER);
						contTable.addCell(cell);

						phrase = new Phrase();
						ch = new Chunk(") SS:",fnt);
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(Rectangle.NO_BORDER);
						contTable.addCell(cell);
						//
						// third row
						ch = new Chunk("COUNTY OF MONROE",fnt);
						phrase = new Phrase();
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(Rectangle.NO_BORDER);
						contTable.addCell(cell);
						// 
						ch = new Chunk(")",fnt);
						phrase = new Phrase();
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(Rectangle.NO_BORDER);
						contTable.addCell(cell);
						//
						document.add(contTable);
						//
						phrase = new Phrase();
						ch = new Chunk("Before me, a Notary Public in and for said County and State, personally appeared _______________(witness), being known or proved to me to be the person whose name is subscribed as a witness above to the foregoing document execution and delivery by "+agent.getName()+", and swore that they are not a party to and will not receive any interest in the transaction this ____ day of __________, 20__.\n",fnt);
						phrase.add(ch);
						document.add(phrase);
						//
						Table notTable = new Table(2);
						// int withs[] = {40,60};
						notTable.setWidth(100);
						notTable.setWidths(withs);
						notTable.getDefaultCell().setBorder(0);
						notTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

						phrase = new Phrase();
						ch = new Chunk("My Commission Expires:________",fnt);
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(Rectangle.NO_BORDER);
						notTable.addCell(cell);						

						phrase = new Phrase();
						ch = new Chunk("Signed:__________________",fnt);
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(Rectangle.NO_BORDER);
						notTable.addCell(cell);

						phrase = new Phrase();
						ch = new Chunk("NP#:__________________",fnt);
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(Rectangle.NO_BORDER);
						notTable.addCell(cell);

						phrase = new Phrase();
						ch = new Chunk("Printed Name:__________________",fnt);
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(Rectangle.NO_BORDER);
						notTable.addCell(cell);

						phrase = new Phrase();
						ch = new Chunk(" ",fnt);
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(Rectangle.NO_BORDER);
						notTable.addCell(cell);

						phrase = new Phrase();
						ch = new Chunk("              Notary Public\n Residing in Monroe, Indiana\n",fnt);
						phrase.add(ch);
						cell = new RtfCell(phrase);
						cell.setBorder(Rectangle.NO_BORDER);
						notTable.addCell(cell);
						//
						document.add(notTable);						
						//
						// again county table
						document.add(contTable);
						//
						phrase = new Phrase();
						ch = new Chunk("Before me, a Notary Public in and for said County and State, personally appeared ______________, being known or proved to me to be the person whose name is subscribed as a witness above to the foregoing document execution and delivery by __________, and swore that they are not a party to and will not receive any interest in the transaction this ____ day of __________, 20__.\n\n",fnt);
						phrase.add(ch);
						document.add(phrase);						
						//
						document.add(notTable);
						
						phrase = new Phrase();
						ch = new Chunk("I affirm under penalties of perjury that I have taken reasonable care to redact each Social Security Number in this document, unless required by law.         ",fnt);
						phrase.add(ch);
						ch = new Chunk("Signed:_____________\n\n",fnt);
						phrase.add(ch);	    
						pp = new Paragraph();
						pp.setIndentationLeft(0);
						pp.setFirstLineIndent(pp_indent);
						pp.setAlignment(Element.ALIGN_LEFT);
						pp.add(phrase);
						document.add(pp);

						
						phrase = new Phrase();

						pp = new Paragraph();
						pp.setAlignment(Element.ALIGN_RIGHT);
						pp.add(phrase);
						document.add(pp);
						
						phrase = new Phrase();
						ch = new Chunk(" This instrument prepared by "+attorneyFullName+", Attorney at Law \n City of Bloomington Legal Department, P. O. Box 100, Bloomington, Indiana  47402-0100",fnts);
						phrase.add(ch);
						pp = new Paragraph();
						pp.setAlignment(Element.ALIGN_CENTER);
						pp.add(phrase);
						document.add(pp);
				}
				catch(Exception ex){
						logger.error(ex);
				}
    }		

}






















































