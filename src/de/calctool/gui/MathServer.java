// part of the mathematic virtuell machine
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
package de.calctool.gui;
import de.calctool.vm.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class MathServer
{
    MathVM vm = new MathVM();
    public void start() { run(); };
    private static int hex2int(char c)
    {
        if('0' <= c && c <= '9') return c - '0'; 
        if('a' <= c && c <= 'f') return c - 'a' + 10; 
        if('A' <= c && c <= 'F') return c - 'A' + 10; 
        return -1;
    }
    private final static String h = "0123456789ABCDEF";
    private final static String allow = "0123456789ABCDEFGHIJKLMNOPQRSTUVWabcdefghijklmnopqrstuvwxyz";
    private final static char hex[] = h.toCharArray();
    private static String escape(String val)
    {
	char c[] = val.toCharArray();
	String cod = "";
	for(int i = 0;i < c.length;i++)
	    {
		if(allow.indexOf(c[i]) < 0)
		    cod += "%"+hex[ c[i]/16 ] + hex[c[i] % 16];
		else
		    cod += c[i];
	    }
	return cod;
    }
    private static String cleanup(String val,boolean special)
    {
	int i = 0;
	while(special)
	    {
		i = val.indexOf("+",i);
		if(i < 0) break;
		val = val.substring(0,i) + " " + val.substring(i+1);
		i+= 1;
	    }
	i = 0;
        while(true)
	    {
		i = val.indexOf("%",i);
		if(i < 0 || i + 2 >= val.length()) break;
		//System.out.println(val.charAt(i+1) + " == "+hex2int(val.charAt(i+1)));
		//System.out.println(val.charAt(i+2) + " == "+hex2int(val.charAt(i+2)));
		long z = (hex2int(val.charAt(i+1)) << 4) +  hex2int(val.charAt(i+2));
		int next = 3;
		if(val.charAt(i+1) == 'u')
		    {
			z = (hex2int(val.charAt(i+2)) << 12) +  (hex2int(val.charAt(i+3)) << 8)
			    + (hex2int(val.charAt(i+4)) << 4) +  hex2int(val.charAt(i+5));
			next = 6;
		    }
		
		String str = ""+(char)z;
		if(z == 0xb2) str = "^ 2";
		if(z == 0xb3) str = "^ 3";
		if(z == 0xb7) str = " * ";
		if(z == 0x3c0) str = "pi";
		if(z == 0x3b1) str = "alpha";
		if(z == 0x3b2) str = "beta";
		if(z == 0x3b3) str = "gamma";
		if(z == 0x3b4) str = "delta";
		if(z == 0x3b5) str = "epsilon";
		if(z == 0x3bb) str = "lambda";
		if(z == 0x3bc) str = "mu";
		if(z == 0x221e) str = "infin";
		
		val = val.substring(0,i) + str + val.substring(i+next);
		
		i+=1;
	    }
        return val;
    }
    public void run() {
        ServerSocket s = null; 
        try
        {
            s = new ServerSocket(9999,0,InetAddress.getByName("localhost"));
            while(true)
            {
		
		//waiting for Connection from client
		Socket cl     = s.accept(); 
		try
		    {
			String mobile = null;
			String cookie = null;
			BufferedReader in = new BufferedReader(new InputStreamReader(cl.getInputStream()));
			OutputStream out = cl.getOutputStream();
			String url = null;
			Hashtable<String,String> options = new Hashtable<String,String>();
			while(true)
			    {
				//read request
				String line = in.readLine();
				if(line == null) break;
				if(line.trim().equals("")) break;
				System.out.println(line);
				if(line.trim().toLowerCase().startsWith("get"))
				    {
					//parse request
					line  = line.trim().substring(4).trim();
					line = replace(line,"&amp;","&");
					int i =  line.toLowerCase().indexOf("http/");
					if(i > 0)
					    line = line.substring(0,i).trim();
					i = line.indexOf("?");
					if(i < 0)
					    url = line;
					else
					    {
						url = line.substring(0,i);
						line = line.substring(i + 1);
						boolean special = true;
						if(line.indexOf("test=1+1") < 0)
						    special = false;
						while(true)
						    {
							i = line.indexOf("=");
							if(i < 0) break;
							String key = line.substring(0,i);
							String val = "";
							line = line.substring(i + 1);
							i = line.indexOf("&");
							if(i < 0)
							    {
								val = line.trim();
								line = "";
							    }
							else
							    {
								val = line.substring(0,i);
								line = line.substring(i + 1);
							    }
							options.put(key,cleanup(val,special));
						    }
					    }
				    }
				else
				    {
					int i = line.indexOf(":");
					if(i >= 0)
					    {
						String k = line.substring(0,i).toLowerCase().trim();
						String v = line.substring(i+1).toLowerCase().trim();
						if(k.equals("user-agent"))
						    {
							//Analyse the User Agent
							i = v.indexOf("midp");
							if(i >= 0) 
							    {
								//This should be an mobilebrowser
								if(v.indexOf("opera mini") >= 0)
								    mobile = "Opera";
								else
								    mobile = "unknown";
							    }
						    }
						if(k.equals("cookie"))
						    {
							String cp[] = v.split(";");
							for(i = 0;i < cp.length;i++)
							    {
								String cd[] = cp[i].trim().split("=");
								if(cd.length != 2) continue;
								if(cd[0].startsWith("$"))
								    {
									//cookiedata
								    }
								else
								    {
									String ck = cd[0].trim();
									String cv = cd[1].trim();
									if(ck.equals("calctool"))
									    cookie = cp[i];
									if(cv.startsWith("\""))
									    {
										cv = cv.substring(1);
										int pos = cv.indexOf("\"");
										if(pos > 0)
										    cv = cv.substring(0,pos);
									    }
									options.put(ck,cv);
								    }


							    }
						    }


					    }

				    }
			    }
			boolean mobi = false;
			if(url.equals("/") && mobile != null) { mobi = true; }
			if(url.equals("/")) url = "/index.html";
			if(url.equals("/mobile")) mobi = true;
			if(url.equals("/mobile/")) mobi = true;
			if(url.endsWith(".htm")) 
			    {
				String frame = readTextFile("htdocs/frame.html");
				String page  = readTextFile("htdocs"+url);
				
			
				page = replace(frame,"@text@",page);
				out.write(new RequestHeader(RequestHeader.HTML,page.length()).getBytes());
				out.write(page.getBytes());
			    }
			else
			    {
				if(cookie == null)
				    {
					String cvalue = ""+new Date().getTime();
					cookie = "calcttool"+cvalue;
					options.put("calctool",cvalue);
				    }
				if(options.get("sid") == null)
				    {
					if(options.get("calctool") != null)
					    options.put("sid",options.get("calctool"));
				    }
				if(mobi)
				    {
					String page = readTextFile("htdocs/mobile.html");
	
					String req = options.get("req");
					if(req == null)
					    {
						//init
						page = replace(page,"@inhalt@",
							       "<img src=\"/logo_white.jpg\" alt=\"Calctool\" width=\"140\">");
					    }
					else
					    {
						//calc request 
						String lines[] = null;//vm.getSimple(140,options);
						String inhalt = "";
						for(int x = 0;x < lines.length;x++)
						    {
							inhalt += lines[x]+"<br/>";
						    }
						page = replace(page,"@inhalt@",inhalt);
					    }
					//add Examples
					String ex = "";
					String section[] = getExamples(options);
					for(int si = 0;si < section.length;si++)
					    {
						String exampl[] = section[si].split("\n");
						if(exampl.length < 2) continue;
						int spos = exampl[0].indexOf(":");
						if(spos < 0) continue;
						ex += exampl[0].substring(0,spos).trim();
						
						ex += "<ul>\n";
						for(int x = 1;x < exampl.length;x++)
						    {
							int dppos = exampl[x].indexOf(":");
							if(dppos < 0) continue;
							String formula = exampl[x].substring(dppos+1).trim();
							ex += "<li><a href=\"/mobile?sid=@sid@&amp;req="+escape(formula)+"\">"
							    +formula+"</a></li>";
						    }
						ex += "</ul>\n";
					    }
					
					page = replace(page,"@examples@",ex);
					
					String sid = options.get("sid");
					page = replace(page,"@sid@",sid);
					
					out.write(new RequestHeader(RequestHeader.HTML,page.length(),cookie).getBytes());
					out.write(page.getBytes());
				    }
				else
				    {
					if(url.equals("/index.html"))
					    {
						String page = readTextFile("htdocs/index.html");
						
						//add Examples
						String menu = "";
						String ex = "";
						String section[] = getExamples(options);
						int secindex = -1;
						for(int si = 0;si < section.length;si++)
						    {
							String exampl[] = section[si].split("\n");
							if(exampl.length < 2) continue;
							int spos = exampl[0].indexOf(":");
							if(spos < 0) continue;
							secindex++;

							String title = exampl[0].substring(0,spos).trim();
							
							String script = "";
							
							for(int sci = 0;sci < section.length;sci++)
							    {
								script += "section"+sci+".style.display="
								    +(sci == secindex?"'block'":"'none'")+";";
							    }
							menu += "<a class=\"menulink\" href=\"#\" onclick=\""
							    +script+"\">\n";
							menu += "<span class=\"menutext\">"+title+"</span></a> \n";
							
							
							ex += "<div id=\"section"+secindex+"\""
							    +((secindex == 0)? "" :" style=\"display:none;\"")
							    +">\n";
							ex += "<span class=\"sideboardtop\">"+
							    title+"</span><br />\n";
							ex += exampl[0].substring(spos+1).trim()+"<br />\n";
							
							
							for(int x = 1;x < exampl.length;x++)
							    {
								int dppos = exampl[x].indexOf(":");
								if(dppos < 0) continue;
								String formula = exampl[x].substring(dppos + 1).trim();
								ex += "<br />\n";
								ex += "<span class=\"sideboardsub\">"+
								    exampl[x].substring(0,dppos).trim()+"</span><br />\n";
								ex += exampl[x].substring(dppos + 1).trim()+"<br/>\n";
							    }
							ex += "</div>\n";
						    }
						
						page = replace(page,"@menu@",menu);
						page = replace(page,"@examples@",ex);
						out.write(new RequestHeader(RequestHeader.HTML,page.length(),cookie).getBytes());
						out.write(page.getBytes());
					    }
					else
					    {
						File f = new File("htdocs"+url);
						if(f.exists())
						    {
							out.write(new RequestHeader(f).getBytes());
							FileInputStream fin = new FileInputStream(f);
							byte b[] = new byte[0xffff];
							while(true)
							    {
								int r = fin.read(b,0,b.length);
								if(r <= 0) break;
								out.write(b,0,r);
							    }
						    }
						else
						    {
							//modul ?
							if(url.equals("/math.xml"))
							    {
								//vm.get(out,options);
							    }
							else if(url.endsWith(".jpg"))
							    {
								//vm.writeImage(url,out,options);
							    }
							else
							    System.out.println(url + options);
						    }
					    }
				    }
			    }
			out.flush();
			out.close();
		    }
		catch(Exception e)
		    {
			e.printStackTrace();
			Thread.sleep(1000); //to prevent highleveled endlesloop
		    }
	    }
        }
        catch(Exception e)
	    {
		e.printStackTrace();
	    }
        try
	    {
		s.close();
	    }
        catch(Exception e)
	    {
		e.printStackTrace();
	    }
    }
    private String replace(String page,String pattern,String subst)
    {
	int i = 0;
	while(true)
	    {
		i = page.indexOf(pattern,i);
		if(i < 0) break;
		page = page.substring(0,i) + subst + page.substring(i + pattern.length());
		i+= subst.length();
	    }
	return page;
    }
    private String[] getExamples(Hashtable options)
    {
	String file = readTextFile("help/examples.txt");
	if(file == null) return new String[0];
	return file.split("%section");
    }
    private String readTextFile(String f)
    {
	try 
	    {
		String page = "";
		FileInputStream fin = new FileInputStream(f);
		byte b[] = new byte[0xffff];
		while(true)
		    {
			int r = fin.read(b,0,b.length);
			if(r <= 0) break;
			page += new String(b,0,r);
		    }
		return page;
	    }
	catch(Exception e)
	    {
		e.printStackTrace();
	    }
	return "Page not found";
    }
    public static void main(String args[])
    {

	while(true)
	    {
		try
		    {
			MathServer s = new MathServer();
			s.start();
		    }
		catch(Exception e)
		    {
			System.out.println(""+e);	
		    }
		try
		    {
			Thread.sleep(10000);
		    }
		catch(InterruptedException e)
		    {
			System.out.println(""+e);	
		    }
	    }
    }
}
