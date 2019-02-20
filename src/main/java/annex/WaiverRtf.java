
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

import org.apache.log4j.Logger;

public class WaiverRtf extends HttpServlet {

    String url="";
    static final long serialVersionUID = 70L;
    static Logger logger = Logger.getLogger(WaiverRtf.class);
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
														writeCorporateAuthority(document,
																										businessEntity,
																										agent);
														document.newPage();	
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
    void writeWaiver(Document document,
										 Waiver waiver,
										 List<Entity> owners,
										 String attorneyFullName){
				//
				String ownerNames = "";
				for(int jj=0;jj<owners.size();jj++){
						Entity one = owners.get(jj);
						if(jj+1 < owners.size()){
								ownerNames += ", ";
						}
						else if(jj+1 == owners.size() && owners.size() > 1){
								ownerNames += " and ";
						}
						ownerNames += one.getName();
				}
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
						ch = new Chunk("The undersigned "+ownerNames+", as owner(s) of the real estate hereinafter described, for and in consideration of the City of Bloomington, Indiana, granting to the undersigned the right to tap into and connect to the sewer system of the City of Bloomington for the purpose of providing sewer service to the described real estate, now release the right of the undersigned as owner of the described real estate and their successors in title to remonstrate against any pending or future annexation by the City of Bloomington, Indiana, of such described real estate for a period of fifteen (15) years from the date of filing this instrument.",fnt);
						phrase.add(ch);
						pp = new Paragraph();
						pp.setIndentationLeft(0);
						pp.setFirstLineIndent(pp_indent);
						pp.setAlignment(Element.ALIGN_LEFT);
						pp.add(phrase);
						document.add(pp);
						//
						phrase = new Phrase();
						ch = new Chunk("The real estate to be served by such sewers and the real estate for which the right of remonstrance against pending or future annexation to the City of Bloomington is released, is described as follows:",fnt);
						phrase.add(ch);
						pp = new Paragraph();
						pp.setIndentationLeft(0);
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
						ch = new Chunk("Signed:\n",fnt);
						phrase.add(ch);
						document.add(phrase);
						//
						table = new Table(owners.size());
						table.setWidth(100);
						table.getDefaultCell().setBorder(0);
						table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						//
						for(Entity owner:owners){
								phrase = new Phrase();
								ch = new Chunk("____________________________\n",fnt);
								phrase.add(ch);
								ch = new Chunk(owner.getName()+"\n",fnt);
								phrase.add(ch);								
								cell = new RtfCell(phrase);
								cell.setBorder(Rectangle.NO_BORDER);
								table.addCell(cell);								
						}
						document.add(table);
						//
						for(Entity owner:owners){
								table = new Table(2);
								table.setWidth(100);						
								table.getDefaultCell().setBorder(0);
								table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

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
								ch = new Chunk("Before me, a Notary Public, personally appeared "+owner.getName()+" and acknowledged the execution of the above release of the right to remonstrate against pending or future annexation to the City of Bloomington, Indiana, to be his voluntary act and deed, this ____ day of ____________, 20____. \n",fnt);
								phrase.add(ch);
								pp = new Paragraph();
								pp.setIndentationLeft(0);
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
								ch = new Chunk(" ",fnt);
								phrase.add(ch);
								cell = new RtfCell(phrase);
								cell.setBorder(Rectangle.NO_BORDER);
								table.addCell(cell);
								
								phrase = new Phrase();
								ch = new Chunk("Name Printed:__________________ Notary Public",fnt);
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
								ch = new Chunk("Residing in Monroe County, Indiana\n",fnt);
								phrase.add(ch);
								cell = new RtfCell(phrase);
								cell.setBorder(Rectangle.NO_BORDER);
								table.addCell(cell);
								document.add(table);
								//
								phrase = new Phrase();
								ch = new Chunk("I affirm under penalties of perjury that I have taken reasonable care to redact each Social Security Number in this document, unless required by law.\n",fnt);
								phrase.add(ch);
								pp = new Paragraph();
								pp.setIndentationLeft(0);
								pp.setFirstLineIndent(pp_indent);
								pp.setAlignment(Element.ALIGN_LEFT);
								pp.add(phrase);
								document.add(pp);
								
								phrase = new Phrase();
								ch = new Chunk("Signed:__________________________________\n\n",fnt);
								phrase.add(ch);
								pp = new Paragraph();
								pp.setAlignment(Element.ALIGN_RIGHT);
								pp.add(phrase);
								document.add(pp);
						}
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
						ch = new Chunk("The undersigned "+agent.getName()+", as authorized agent for "+businessEntity.getName()+", an Indiana Limited Liability Coorporation and Owner of the real estate hereinafter described, for and in consideration of the City of Bloomington, Indiana, granting to the Owner the right to tap into and connect to the sewer system of the City of Bloomington for the purpose of providing sewer service to the described real estate, now release the right of the Owner of the described real estate, and its successors in title, to remonstrate against any pending or future annexation by the City of Bloomington, Indiana, of such described real estate for a period of fifteen (15) years form the date of filing this instrument.  The person signing below on behalf of Owner represents and certifies that he or she has full authority to execute this Waiver of Protest of Annexation on behalf of Owner.\n",fnt);
						phrase.add(ch);
						pp = new Paragraph();
						pp.setIndentationLeft(0);
						pp.setFirstLineIndent(pp_indent);
						pp.setAlignment(Element.ALIGN_LEFT);
						pp.add(phrase);
						document.add(pp);
						//
						phrase = new Phrase();
						ch = new Chunk("The real estate to be served by such sewers and the real estate for which the right of remonstrance against pending or future annexation to the City of Bloomington is released, is described as follows:",fnt);
						phrase.add(ch);
						pp = new Paragraph();
						pp.setIndentationLeft(0);
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
						ch = new Chunk("Deed Instrument No.:",fnt);
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
						ch = new Chunk("\n"+businessEntity.getName()+"\n",fntu);
						phrase.add(ch);
						ch = new Chunk("By:\n\n",fnt);						
						document.add(phrase);
						//
						phrase = new Phrase();
						ch = new Chunk("_______________________\n",fnt);
						phrase.add(ch);						
						ch = new Chunk("Name Printed:",fnt);
						phrase.add(ch);
						ch = new Chunk(agent.getName()+"\n", fntu);
						phrase.add(ch);
	    
						ch = new Chunk("Title:",fnt);
						phrase.add(ch);
						str = agent.getTitle();
						if(str == null || str.trim().equals("")) str = "                       ";
						ch = new Chunk(str,fntu);
						phrase.add(ch);
						ch = new Chunk("\n\n",fnt);
						phrase.add(ch);	    
						document.add(phrase);
						//
						table = new Table(2);
						table.setWidth(100);						
						table.getDefaultCell().setBorder(0);
						table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

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
						ch = new Chunk("Before me, a Notary Public, personally appeared ", fnt);
						phrase.add(ch);
						ch = new Chunk(agent.getName(), fntu);
						phrase.add(ch);
						ch = new Chunk(" and acknowledged the execution of the above release of the right to remonstrate against pending or future annexation to the City of Bloomington, Indiana, to be his voluntary act and deed, this ____ day of ____________, 20____.",fnt);
						phrase.add(ch);
						pp = new Paragraph();
						pp.setIndentationLeft(0);
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
						ch = new Chunk("My Commission Expires: ",fnt);
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
						//
						phrase = new Phrase();
						ch = new Chunk("I affirm under penalties of perjury that I have taken reasonable care to redact each Social Security Number in this document, unless required by law. ",fnt);
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






















































