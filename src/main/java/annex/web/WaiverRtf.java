
package annex.web;

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
import javax.servlet.annotation.WebServlet;
import annex.model.*;
import annex.list.*;
import annex.utils.*;

@WebServlet(urlPatterns = {"/WaiverRtf"})
public class WaiverRtf extends TopServlet {

    static Logger logger = LogManager.getLogger(WaiverRtf.class);		
    static final long serialVersionUID = 70L;
    String fileAndPath = "waiver.rtf";

    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException {
	doPost(req, res);
    }

    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException{
	res.setContentType("text/html");

	String name, value, message="", id="", str="";

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
			    writeBusinessWaiver(document,
						waiver,
						businessEntity,
						agent,
						attorneyFullName,
						paraLegalName);
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
				    attorneyFullName,
				    paraLegalName);
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
    void writeWaiver(Document document,
		     Waiver waiver,
		     List<Entity> owners,
		     String attorneyFullName,
		     String paraLegalName){
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
	// System.err.println(" owners "+ownerNames);
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
	    table.setBorder(5);
	    // table.setPadding(3);
	    // table.getDefaultCell().setBorder(1);
	    table.setWidth(100);
	    table.setWidths(withs);
	    table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	    table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);						
	    phrase = new Phrase();
	    ch = new Chunk("Service Address:",fntu);
	    phrase.add(ch);						
	    RtfCell cell = new RtfCell(phrase);
	    cell.setVerticalAlignment(Element.ALIGN_TOP);
	    table.addCell(cell);

	    //
	    phrase = new Phrase();
	    ch = new Chunk(waiver.getAddressInfo(),fnt);
	    phrase.add(ch);
	    cell = new RtfCell(phrase);
	    cell.setVerticalAlignment(Element.ALIGN_TOP);
	    // cell.setBorder(0);
	    table.addCell(cell);						
	    //
	    phrase = new Phrase();
	    ch = new Chunk("Legal Description:",fntu);
	    phrase.add(ch);						
	    cell = new RtfCell(phrase);
	    cell.setVerticalAlignment(Element.ALIGN_TOP);
	    // cell.setBorder(0);
	    table.addCell(cell);						
	    //
	    phrase = new Phrase();
	    ch = new Chunk(waiver.getLegalDescription(),fnt);
	    phrase.add(ch);
	    cell = new RtfCell(phrase);
	    cell.setVerticalAlignment(Element.ALIGN_TOP);
	    // cell.setBorder(0);
	    table.addCell(cell);
	    //
	    phrase = new Phrase();
	    ch = new Chunk("Deed Instrument Nos.:",fntu);
	    phrase.add(ch);
	    cell = new RtfCell(phrase);
	    cell.setVerticalAlignment(Element.ALIGN_TOP);
	    // cell.setBorder(0);
	    table.addCell(cell);
	    //
	    phrase = new Phrase();
	    ch = new Chunk(waiver.getDeedInstrumentNum(),fnt);
	    phrase.add(ch);
	    cell = new RtfCell(phrase);
	    cell.setVerticalAlignment(Element.ALIGN_TOP);
	    // cell.setBorder(0);
	    table.addCell(cell);
	    //
	    String str="", str2 = waiver.getParcelPin();
	    if(str2 != null && !str2.equals("")){
		str = "Parcel Pin #:";
		phrase = new Phrase();
		ch = new Chunk(str,fntu);
		phrase.add(ch);
		cell = new RtfCell(phrase);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		// cell.setBorder(0);
		table.addCell(cell);
		phrase = new Phrase();
		ch = new Chunk(str2,fnt);
		phrase.add(ch);
		cell = new RtfCell(phrase);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		// cell.setBorder(0);
		table.addCell(cell);
	    }
	    str2 = waiver.getParcelTaxId();
	    if(str2 != null && !str2.equals("")){
		str = "Parcel Tax ID #:";
		phrase = new Phrase();
		ch = new Chunk(str,fntu);
		phrase.add(ch);
		cell = new RtfCell(phrase);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		//cell.setBorder(0);
		table.addCell(cell);
		phrase = new Phrase();
		ch = new Chunk(str2,fnt);
		phrase.add(ch);
		cell = new RtfCell(phrase);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		// cell.setBorder(0);
		table.addCell(cell);					
	    }
	    //
	    document.add(table);
	    //
	    // monroe table
	    //
	    Table ssTable = new Table(2);
	    int widths2[] = {25,75};						
	    ssTable.setWidth(100);
	    ssTable.setWidths(widths2);
	    ssTable.getDefaultCell().setBorder(1);
	    ssTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

	    //
	    ch = new Chunk("STATE OF INDIANA",fnt);
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
	    ch = new Chunk(" ",fnt);
	    phrase = new Phrase();
	    phrase.add(ch);
	    cell = new RtfCell(phrase);
	    cell.setBorder(Rectangle.NO_BORDER);
	    ssTable.addCell(cell);						
	    //
	    ch = new Chunk(") SS:",fnt);
	    phrase = new Phrase();
	    phrase.add(ch);
	    cell = new RtfCell(phrase);
	    cell.setBorder(Rectangle.NO_BORDER);						
	    ssTable.addCell(cell);
	    //
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
						
	    // old
	    /**
	       Paragraph pss = new Paragraph();
	       pss.setIndentationLeft(0);
	       phrase = new Phrase();
	       ch = new Chunk("STATE OF INDIANA    )\n",fnt);
	       phrase.add(ch);
	       pss.add(phrase);
	       // cell = new RtfCell(phrase);
	       // cell.setBorder(0);
	       // ssTable.addCell(cell);
	       //						
	       // phrase = new Phrase();
	       // ch = new Chunk("      )\n",fnt);
	       // phrase.add(ch);
	       // pss.add(phrase);
	       // cell = new RtfCell(phrase);
	       // cell.setBorder(Rectangle.NO_BORDER);
	       // ssTable.addCell(cell);
						
	       phrase = new Phrase();
	       ch = new Chunk(" ",fnt);
	       phrase.add(ch);
	       pss.add(phrase);
						
	       phrase = new Phrase();
	       ch = new Chunk("                        ) SS:\n",fnt);
	       phrase.add(ch);
	       pss.add(phrase);						

	       //
	       // third row
	       ch = new Chunk("COUNTY OF MONROE)\n",fnt);
	       phrase = new Phrase();
	       phrase.add(ch);
	       pss.add(phrase);
	    */

	    Paragraph pnote = new Paragraph();
	    pnote.setIndentationLeft(0);
								
	    phrase = new Phrase();
	    ch = new Chunk("My Commission Expires:__________\t",fnt);
	    phrase.add(ch);
	    pnote.add(phrase);
						
	    phrase = new Phrase();
	    ch = new Chunk("Signed:______________\n",fnt);
	    phrase.add(ch);
	    pnote.add(phrase);
								
	    phrase = new Phrase();
	    ch = new Chunk("NP#:__________________________\t",fnt);
	    phrase.add(ch);
	    pnote.add(phrase);

						
	    phrase = new Phrase();
	    ch = new Chunk("Name Printed:_______________ Notary Public\n",fnt);
	    phrase.add(ch);
	    pnote.add(phrase);								

	    phrase = new Phrase();
	    ch = new Chunk("Residing in _______________ County, Indiana\n",fnt);
	    phrase.add(ch);
	    cell = new RtfCell(phrase);
	    pnote.add(phrase);

	    //
	    phrase = new Phrase();								
	    ch = new Chunk("\nSigned:(Owner)\n",fnt);
	    phrase.add(ch);
	    document.add(phrase);
	    phrase = new Phrase();
	    int jj=1;
	    for(Entity owner:owners){
		if(jj > 1){
		    ch = new Chunk(" ",fnt);
		    phrase.add(ch);
		}
		ch = new Chunk("__________________"+owner.getName()+", Owner ",fnt);
		phrase.add(ch);
		jj++;
	    }
	    ch = new Chunk("\n", fnt);
	    phrase.add(ch);
	    document.add(phrase);
	    // 
	    // 
	    // document.add(pss);
	    //
	    // document.add(ssTable);
	    //
	    for(Entity owner:owners){
		//
		document.add(ssTable);
		ch = new Chunk("\n", fnt);
		phrase = new Phrase();
		phrase.add(ch);
		document.add(phrase);								
		//
		phrase = new Phrase();								
		ch = new Chunk("Before me, a Notary Public, personally appeared "+owner.getName()+" and acknowledged the execution of the above release of the right to remonstrate against pending or future annexation to the City of Bloomington, Indiana, to be a voluntary act and deed, this _____ day of ____________, 20__.\n",fnt);						
		//
		phrase.add(ch);
		pp = new Paragraph();
		pp.setIndentationLeft(0);
		pp.setFirstLineIndent(pp_indent);
		pp.setAlignment(Element.ALIGN_LEFT);
		pp.add(phrase);
		document.add(pp);
		document.add(pnote);
	    }
	    phrase = new Phrase();
	    ch = new Chunk("I affirm under penalties of perjury that I have taken reasonable care to redact each Social Security Number in this document, unless required by law.\n",fnts);
	    phrase.add(ch);
	    ch = new Chunk("                        signed:/s/ ______________\n",fnts);
	    phrase.add(ch);
	    ch = new Chunk("                                  "+paraLegalName+"\n", fnts);
	    phrase.add(ch);
	    pp = new Paragraph();
	    pp.setAlignment(Element.ALIGN_LEFT);
	    pp.add(phrase);
	    document.add(pp);
	    //
	    phrase = new Phrase();						
	    // phrase.add(ch);						
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
			     String attorneyFullName,
			     String paraLegalName){
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
	    ch = new Chunk("The undersigned "+agent.getName()+", as authorized agent for "+businessEntity.getName()+", an Indiana limited liability corporation and Owner of the real estate hereinafter described, for and in consideration of the City of Bloomington, Indiana, granting to the Owner the right to tap into and connect to the sewer system of the City of Bloomington for the purpose of providing sewer service to the described real estate, now releases the right of the Owner of the described real estate, and its successors in title, to remonstrate against any pending or future annexation by the City of Bloomington, Indiana, of such described real estate for a period of fifteen (15) years from the date of filing this instrument.  The person signing below on behalf of Owner represents and certifies that he or she has full authority to execute this Waiver of Protest of Annexation on behalf of Owner.  The real estate to be served by such sewers and the real estate for which the right of remonstrance against pending or future annexation to the City of Bloomington is released, is described as follows:",fnt);
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
	    table.setBorder(3);
	    table.setBorderWidth(3);
	    table.setBorder(Rectangle.TOP);
	    table.setBorder(Rectangle.RIGHT);
	    table.setBorder(Rectangle.LEFT);
	    table.setBorder(Rectangle.BOTTOM);
	    table.getDefaultCell().setBorder(3);

	    phrase = new Phrase();
	    ch = new Chunk("Service Address:",fnt);
	    phrase.add(ch);						
	    RtfCell cell = new RtfCell(phrase);
	    cell.setBorder(3);
	    cell.setBorder(Rectangle.TOP);
	    table.addCell(cell);

	    //
	    phrase = new Phrase();
	    ch = new Chunk(waiver.getAddressInfo(),fntu);
	    phrase.add(ch);
	    cell = new RtfCell(phrase);
	    cell.setBorder(3);
	    table.addCell(cell);						
	    //
	    phrase = new Phrase();
	    ch = new Chunk("Legal Description:",fnt);
	    phrase.add(ch);						
	    cell = new RtfCell(phrase);
	    cell.setBorder(3);
	    cell.setVerticalAlignment(Element.ALIGN_TOP);
	    table.addCell(cell);						
	    //
	    phrase = new Phrase();
	    ch = new Chunk(waiver.getLegalDescription(),fntu);
	    phrase.add(ch);
	    cell = new RtfCell(phrase);
	    cell.setBorder(3);
	    table.addCell(cell);
	    //
	    phrase = new Phrase();
	    ch = new Chunk("Deed Instrument Nos.:",fnt);
	    phrase.add(ch);
	    cell = new RtfCell(phrase);
	    cell.setBorder(3);
	    table.addCell(cell);
	    //
	    phrase = new Phrase();
	    ch = new Chunk(waiver.getDeedInstrumentNum(),fntu);
	    phrase.add(ch);
	    cell = new RtfCell(phrase);
	    cell.setBorder(3);
	    table.addCell(cell);
	    //
	    String str="", str2 = waiver.getParcelPin();
	    if(str2 != null && !str2.equals("")){
		str = "Parcel Pin #:";
		phrase = new Phrase();
		ch = new Chunk(str,fnt);
		phrase.add(ch);
		cell = new RtfCell(phrase);
		cell.setBorder(3);
		table.addCell(cell);
		//
		phrase = new Phrase();
		ch = new Chunk(str2,fntu);
		phrase.add(ch);
		cell = new RtfCell(phrase);
		cell.setBorder(3);
		table.addCell(cell);
	    }
	    str2 = waiver.getParcelTaxId();
	    if(str2 != null && !str2.equals("")){								
		str = "Parcel ID #:";
		phrase = new Phrase();
		ch = new Chunk(str,fnt);
		phrase.add(ch);
		cell = new RtfCell(phrase);
		cell.setBorder(3);
		table.addCell(cell);
		//
		phrase = new Phrase();
		ch = new Chunk(str2,fntu);
		phrase.add(ch);
		cell = new RtfCell(phrase);
		cell.setBorder(3);
		table.addCell(cell);						
	    }
	    //
	    document.add(table);
	    //
	    phrase = new Phrase();
	    ch = new Chunk("The undersigned as an officer for "+businessEntity.getName()+" further SWEARS and AFFIRMS to be the (________) of "+businessEntity.getName()+" and has the authority to sign for and on behalf of "+businessEntity.getName(), fnt);
	    phrase.add(ch);
	    pp = new Paragraph();
	    pp.setAlignment(Element.ALIGN_LEFT);
	    pp.add(phrase);
	    document.add(pp);
	    //
	    /**
	       table = new Table(2);
	       table.setBorder(3);
	       table.setBorderWidth(3);
	       table.getDefaultCell().setBorder(3);
	       table.getDefaultCell().setBorderWidth(3);
	       table.setBorder(Rectangle.TOP);
	       table.setBorder(Rectangle.RIGHT);
	       table.setBorder(Rectangle.LEFT);
	       table.setBorder(Rectangle.BOTTOM);
	       table.setWidth(100);
	    */

	    //
	    phrase = new Phrase();
	    ch = new Chunk("OWNER: ",fnt);
	    phrase.add(ch);						
	    ch = new Chunk(businessEntity.getName()+"\t",fntu);
	    phrase.add(ch);
	    // cell = new RtfCell(phrase);
	    // cell.setBorder(3);
	    // table.addCell(cell);						
	    document.add(phrase);
						
	    ch = new Chunk("By: ______________\n",fnt);
	    phrase = new Phrase();
	    phrase.add(ch);
	    document.add(phrase);
	    // cell = new RtfCell(phrase);
	    // cell.setBorder(3);
	    // table.addCell(cell);

	    // empty cell
	    ch = new Chunk(" ",fnt);
	    phrase = new Phrase();
	    phrase.add(ch);
	    // cell = new RtfCell(phrase);
	    // cell.setBorder(3);
	    // table.addCell(cell);						
	    //
	    document.add(phrase);
	    phrase = new Phrase();
	    ch = new Chunk("\t Name Printed:         \n\tTitle:_______________\n",fnt);						
	    phrase.add(ch);
	    document.add(phrase);
	    // cell = new RtfCell(phrase);
	    // cell.setBorder(3);
	    //table.addCell(cell);
	    // document.add(table);
	    //
	    //
	    /**
	       Table contTable = new Table(2);
	       contTable.setWidth(100);
	       contTable.setBorderWidth(3);
	       contTable.getDefaultCell().setBorderWidth(3);
	       contTable.setBorder(Rectangle.TOP);
	       contTable.setBorder(Rectangle.RIGHT);
	       contTable.setBorder(Rectangle.LEFT);
	       contTable.setBorder(Rectangle.BOTTOM);
	       contTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	    */
	    phrase = new Phrase();
	    ch = new Chunk("STATE OF INDIANA    )\n",fnt);
	    phrase.add(ch);
	    document.add(phrase);
	    // cell = new RtfCell(phrase);
	    // cell.setBorder(3);
	    // contTable.addCell(cell);
	    //
	    /**
	       phrase = new Phrase();
	       ch = new Chunk(") \n",fnt);
	       phrase.add(ch);
	       document.add(phrase);
	    */
	    // cell = new RtfCell(phrase);
	    // cell.setBorder(3);						
	    // cell.setBorder(Rectangle.NO_BORDER);
	    // contTable.addCell(cell);

	    // phrase = new Phrase();
	    // ch = new Chunk(" \n",fnt);
	    // phrase.add(ch);
	    // document.add(phrase);
	    // cell = new RtfCell(phrase);
	    // cell.setBorder(3);
	    // cell.setBorder(Rectangle.NO_BORDER);
	    // contTable.addCell(cell);
						
	    phrase = new Phrase();
	    ch = new Chunk("                 ) SS:\n",fnt);
	    phrase.add(ch);
	    document.add(phrase);						

	    // cell = new RtfCell(phrase);
	    // cell.setBorder(3);
	    // cell.setBorder(Rectangle.NO_BORDER);
	    // contTable.addCell(cell);
	    //
	    // third row
	    ch = new Chunk("COUNTY OF MONROE  )\n",fnt);
	    phrase = new Phrase();
	    phrase.add(ch);
	    document.add(phrase);
	    // cell = new RtfCell(phrase);
	    // cell.setBorder(3);
	    // cell.setBorder(Rectangle.NO_BORDER);
	    //contTable.addCell(cell);
	    // 
	    // ch = new Chunk(")\n",fnt);
	    // phrase = new Phrase();
	    //; phrase.add(ch);
	    // document.add(phrase);
	    // cell = new RtfCell(phrase);
	    // cell.setBorder(3);
	    // cell.setBorder(Rectangle.NO_BORDER);
	    // contTable.addCell(cell);
	    //
	    // document.add(contTable);
	    //
	    phrase = new Phrase();
	    ch = new Chunk("Before me, a Notary Public, personally appeared _________ and acknowledged the execution of the above release of the right to remonstrate against pending or future annexation to the City of Bloomington, Indiana, to be a voluntary act and deed, this ____ day of _______________, 20__.\n",fnt); 
	    phrase.add(ch);
	    document.add(phrase);
	    //
	    /**
	       Table notTable = new Table(2);
	       notTable.setWidth(100);
	       notTable.setWidths(withs);
	       notTable.setBorderWidth(3);
	       notTable.getDefaultCell().setBorder(3);
	       notTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	    */
	    phrase = new Phrase();
	    ch = new Chunk("My Commission Expires:________      \t",fnt);
	    phrase.add(ch);
	    document.add(phrase);
	    // cell = new RtfCell(phrase);
	    // cell.setBorder(3);
	    // cell.setBorder(Rectangle.NO_BORDER);
	    // notTable.addCell(cell);						

	    phrase = new Phrase();
	    ch = new Chunk("Signed:__________________\n",fnt);
	    phrase.add(ch);
	    document.add(phrase);
	    // cell = new RtfCell(phrase);
	    // cell.setBorder(3);
	    // cell.setBorder(Rectangle.NO_BORDER);
	    // notTable.addCell(cell);

	    phrase = new Phrase();
	    ch = new Chunk("NP#:__________________     \t",fnt);
	    phrase.add(ch);
	    document.add(phrase);
	    // cell = new RtfCell(phrase);
	    // cell.setBorder(3);
	    // cell.setBorder(Rectangle.NO_BORDER);
	    // notTable.addCell(cell);

	    phrase = new Phrase();
	    ch = new Chunk("Printed Name:__________________\n",fnt);
	    phrase.add(ch);
	    document.add(phrase);
	    // cell = new RtfCell(phrase);
	    // cell.setBorder(3);
	    // cell.setBorder(Rectangle.NO_BORDER);
	    // notTable.addCell(cell);

	    phrase = new Phrase();
	    ch = new Chunk(" ",fnt);
	    phrase.add(ch);
	    document.add(phrase);
	    // cell = new RtfCell(phrase);
	    // cell.setBorder(3);
	    // cell.setBorder(Rectangle.NO_BORDER);
	    // notTable.addCell(cell);

	    phrase = new Phrase();
	    ch = new Chunk("              Notary Public\n Residing in ___________ County, Indiana\n",fnt);
	    phrase.add(ch);
	    document.add(phrase);
	    // cell = new RtfCell(phrase);
	    // cell.setBorder(3);
	    // cell.setBorder(Rectangle.NO_BORDER);
	    // notTable.addCell(cell);
	    //
	    // document.add(notTable);						
	    //
	    phrase = new Phrase();
	    pp = new Paragraph();
	    pp.setAlignment(Element.ALIGN_RIGHT);
	    pp.add(phrase);
	    document.add(pp);
	    phrase = new Phrase();
	    ch = new Chunk("I affirm under penalties of perjury that I have taken reasonable care to redact each Social Security Number in this document, unless required by law.\n", fnts);
	    phrase.add(ch);
	    ch = new Chunk("                                Signed:/s/______________\n",fnts);
	    phrase.add(ch);
	    ch = new Chunk("                                  "+paraLegalName+"\n", fnts);
	    phrase.add(ch);	    
	    pp = new Paragraph();
	    pp.setAlignment(Element.ALIGN_LEFT);
	    pp.add(phrase);
	    document.add(pp);
	    //
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






















































