// A mathematic virtuell machine
// Copyright (C) 2006  Dirk Friedenberger, projekte@frittenburger.de

// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// as published by the Free Software Foundation; either version 2
// of the License, or (at your option) any later version.

// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.

// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
package de.calctool.vm;
import de.calctool.cmd.*;
import de.calctool.eval.MathEvaluator;
import de.calctool.parser.MathDefinitions;
import de.calctool.parser.MathParser;

import java.util.*;
import java.util.List;

public class MathVM
{
    private Hashtable<String,MathRuntime> runtimes = new Hashtable<String,MathRuntime>(); //write on device
	private final MathDefinitions definitions;
    public MathVM()
    {
    	definitions = MathDefinitions.DefaultDefinitions;
    }
    public MathRuntime getRuntime(String sid)
    {
        long runtimenr = 0;
        try { runtimenr = Long.parseLong(sid); } catch(Exception ignoredexc) {}
	
	MathRuntime rt = runtimes.get(""+runtimenr);
        if(rt == null)
        {
	    //read from file
	    String path = "DATA/rt"+runtimenr+".dat";
	    System.out.println(" create new Runtime ");
            rt = new MathRuntime(path);
            if(runtimenr > 0)
                runtimes.put(""+runtimenr,rt);  
        }
	return rt;
    }
    
    
    
 
    
    
    public MathResult[] calculate(String command,MathRuntime rt)
    {
	//special commands
        try
        {
        	
        	MathParser parser = new MathParser(definitions);
        	MathEvaluator evaluator = new MathEvaluator(definitions,rt);
        	
        	String cmd = command.trim();
            String par = "";
            int cpo    = cmd.indexOf(" ");
            if(cpo >= 0) 
            {
                par = cmd.substring(cpo+1);
                cmd = cmd.substring(0,cpo);
            }
            MathCommand c = definitions.getCommand(cmd);
            if(c != null)
                return c.eval(rt,par);
            MathTerm term[]  = parser.parse(command,rt);
            List<MathResult> results = new ArrayList<MathResult>();
            for(int i = 0;i < term.length;i++)
            {
            	MathTerm t = evaluator.eval(term[i]);
                results.add(new MathResult(t));
                if(evaluator.isNumber(t) && !t.getNumber().isFractal()) continue;
                
                MathTerm t2 = evaluator.evalNumeric(t);
                if(!evaluator.isNumber(t2)) continue;
                results.add(new MathResult(new MathTerm(new MathNumber(t2.getNumber().getDouble()))));

            }
            return results.toArray(new MathResult[0]);
        }
        catch(Exception e)
        {
            e.printStackTrace();
	    rt.cleanup();
            return new MathResult[]{new MathResult(e.getMessage())};
        }
    }
    public String[] getExamples(Hashtable options)
    {
    	
	return new String[]{"1 + 1" , 
			    "plot(sin(x))" ,
			    "(x  ^2 + 3 * x + 2)/(x + 1)" ,
			    " 1/2 + 2/6 * 1/7" ,
			    "A := matrix([ 1 , i ] , [ 3 , 4 ] )" };
    }
	public MathDefinitions getDefinitions() {
		return definitions;
	}
    
    /*
    public String[] getSimple(int width,Hashtable options)
    {
	String cmd   = (String)options.get("req");
  	MathRuntime rt = getRuntime((String)options.get("sid"));
	rt.setWidth(width);
	rt.setPartHeight(15);
	MathResult r[] = calculate(cmd,rt);

	String trenner = "#T#";
	String page = "";
	for(int i = 0;i < r.length;i++)
	    {
		page += (i==0?trenner:"")+r[i].toSimple(rt);
	    }

	return page.split(trenner);

    }
*/    
    /*
    public void get(OutputStream out,Hashtable options) throws IOException
    {
        //get the result as xml-file
        String cmd   = (String)options.get("req");
	MathRuntime rt = getRuntime(options);
	rt.setWidth(500);
	rt.setPartHeight(25);
	rt.setBGColor(new int[]{0x9B,0xD1,0xFA});
        MathResult r[] = calculate(cmd,rt);

	String page = "<?xml version=\"1.0\"?>\r\n";
	
	page += "<data>\r\n";
	for(int i = 0;i < r.length;i++)
            page += r[i].toXML(rt)+"\r\n";
	page += "</data>\r\n";
	

	out.write(new RequestHeader(RequestHeader.XML,page.length()).getBytes());

        out.write(page.getBytes());
        out.flush();
    }
   */
  
   /*
    public void writeImage(String url,OutputStream out,Hashtable<String,String> options) throws IOException
    {
        Image org = getImage(url); //this ist the painted image if null is deletet have format 500 / 40 h=25 is one part

	int w   = Integer.parseInt(options.get("w")); //partwidth
	int h   = Integer.parseInt(options.get("h")); //parthight
	int p   = Integer.parseInt(options.get("p")); //partnumber
	int a   = Integer.parseInt(options.get("a")); //partnumber
	float q = (float)Double.parseDouble(options.get("q"));
	BufferedImage img = new BufferedImage( w , h , BufferedImage.TYPE_INT_RGB );
	Graphics g        = img.createGraphics();
	
	//teile 
	
        g.setColor(Color.white);
        g.fillRect(0,0,w,h);
        if(org != null)
        {
	    int iw = org.getWidth(null);
	    int ih = org.getHeight(null);
	    if(p == -1)
		{
		    g.drawImage( org , 0, 0, w , h , 0 , 0 , iw , ih , null ); 
  		}
	    else
		{
		    int hp = ih / a;
		    g.drawImage( org , 0, 0, w , h , 0 , hp * p , iw  , hp * p + hp, null ); 
		}
        }
        else
        {
            g.setColor(Color.black);
            g.drawString(url+" not found",10, h - 3);
        }
        writeImage( img , out , q);
        out.flush();
        out.close();
    }
*/
    /*
    private static void writeImage( BufferedImage img, OutputStream out,
                                    float quality ) throws IOException 
    { 
	ByteArrayOutputStream bao = new ByteArrayOutputStream();
        ImageWriter writer = ImageIO.getImageWritersByFormatName( "jpg" ).next(); 
        ImageOutputStream ios = ImageIO.createImageOutputStream( bao ); 
        writer.setOutput( ios ); 
        ImageWriteParam iwparam = new JPEGImageWriteParam( Locale.getDefault() ); 
        iwparam.setCompressionMode( ImageWriteParam.MODE_EXPLICIT ) ; 
        iwparam.setCompressionQuality( quality ); 
        writer.write( null, new IIOImage(img, null, null), iwparam ); 
        ios.flush(); 
        writer.dispose(); 
        ios.close(); 
	out.write( new RequestHeader(RequestHeader.JPEG , bao.size() ).getBytes());
	bao.writeTo(out);
    }
	*/
    
    
    /*
    public void print(OutputStream out,String cmd) throws IOException
    {
        //print the result plain to outputstream
	    MathRuntime rt = getRuntime("12345");
        MathResult r[] = calculate(cmd,rt);
        for(int i = 0;i < r.length;i++)
            out.write(r[i].toString().getBytes());
        out.flush();
    }
   
    public static void main(String args[]) //the cmdline version
    {
        MathVM vm = new MathVM();

        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        try
        {
            vm.print(System.out,"noop");
            while(true)
            {
                String line = r.readLine();
                if(line.trim().equals("exit")) break;
                vm.print(System.out,line);
            }
        }
        catch(Exception exc)
        {
            exc.printStackTrace();
        }
    }
	*/
}
