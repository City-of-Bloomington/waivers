package annex.utils;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import javax.naming.*;
import javax.naming.directory.*;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * Top class that includes some basic parameters and functions
 * needed in almost all the extending classes
 */

public class CommonInc{

    public boolean debug = false;
    static final long serialVersionUID = 130L;			
    static Logger logger = LogManager.getLogger(CommonInc.class);
    public String message = "";
    List<String> errors = null;
    //
    public CommonInc(){
	debug = Helper.debug;
    }
    public CommonInc(boolean deb){
	debug = deb;
    }

    public String getMessage(){
	return message;
    }
    public boolean hasMessage(){
	return !message.equals("");
    }
    public boolean hasErrors(){
	return (errors != null && errors.size() > 0);
    }
    public List<String> getErrors(){
	return errors;
    }
    public void addError(String val){
	if(errors == null)
	    errors = new ArrayList<String>();
	errors.add(val);
    }

}
