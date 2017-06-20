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
import java.io.File;
public class RequestHeader
{
    public final static String XML  = "application/xml"; 
    public final static String JS   = "application/x-javascript"; 
    public final static String HTML = "text/html";
    public final static String CSS  = "text/css";
    public final static String JPEG = "image/jpeg";
    public final static String PNG  = "image/png";
    public final static String GIF  = "image/gif";
    public final static String ICO  = "image/x-icon";
    public final static String ZIP  = "application/zip";
    public final static String TGZ  = "application/x-tar";
    //public final static String RSS  = "application/rss+xml";
    private String header = "";
    public RequestHeader(String contenttype,long contentlength,String cookie)
    {
	init(contenttype,contentlength,cookie);
    }
    public RequestHeader(String contenttype,long contentlength)
    {
	init(contenttype,contentlength,null);
    }
    public RequestHeader(File f)
    {
	String type = null;
	String n = f.getName();
	int e = n.lastIndexOf(".");
	if(e >= 0)
	    {
		String ext = n.substring(e+1).trim().toLowerCase();
		if(ext.equals("jpg"))  type = JPEG;
		if(ext.equals("jpeg")) type = JPEG;
		if(ext.equals("gif"))  type = GIF;
		if(ext.equals("png"))  type = PNG;
		if(ext.equals("ico"))  type = ICO;
		if(ext.equals("css"))  type = CSS;
		if(ext.equals("js"))   type = JS;
		if(ext.equals("htm"))  type = HTML;
		if(ext.equals("html")) type = HTML;
		if(ext.equals("xml"))  type = XML;
		if(ext.equals("zip"))  type = ZIP;
		if(ext.equals("gz"))   type = TGZ;
		if(ext.equals("rss"))  type = XML;
		
		
		if(type != null)
		    init(type,f.length(),null);
	    }
    }
    private void init(String contenttype,long contentlength,String cookie)
    {
	header =  "HTTP/1.1 200 OK\n";
	header += "Connection: close\n";
	header += "Proxy-Connection: close\n";
	header += "Server: WebCalcServer (projekte@frittenburger.de)\n";
	header += "Content-Type: "+contenttype+"\n";
	header += "Content-Length: "+contentlength+"\n";
	if(cookie != null)
	    header += "Set-Cookie: "+cookie+"; Max-Age=2592000;";
	header += "\n";
    }
    public byte[] getBytes()
    {
	return header.getBytes();
    }
}

