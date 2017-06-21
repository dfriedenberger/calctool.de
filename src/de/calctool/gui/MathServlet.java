package de.calctool.gui;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Locale;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.calctool.image.MathImageCreator;
import de.calctool.vm.MathResult;
import de.calctool.vm.MathRuntime;
import de.calctool.vm.MathVM;

public class MathServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private MathVM vm = new MathVM();
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		
		ObjectMapper mapper = new ObjectMapper();

		MathRequest req = mapper.readValue(request.getInputStream(), MathRequest.class);


		MathResponse resp = new MathResponse();
		
		
		MathRuntime rt = vm.getRuntime(req.getSession());
		MathImageCreator imageCreator = new MathImageCreator(vm.getDefinitions(),rt);

		MathResult[] results = vm.calculate(req.getCommand(),rt);
		
		List<MathResponseResult> rs = new ArrayList<MathResponseResult>(); 
		for(MathResult result : results)
		{
			MathResponseResult r = new MathResponseResult();
			r.setText(result.toString());
		
			String b64 = getBase64(imageCreator.toImage(result));
			r.setImage64(b64);
			rs.add(r);
		}
		
		resp.setResult(rs.toArray(new MathResponseResult[0]));
		resp.setStatus("OK");

		
		response.setContentType("text/json");
		response.setStatus(HttpServletResponse.SC_OK);

		
		mapper.writeValue(response.getWriter(), resp);

	}

	private String getBase64(BufferedImage img) throws IOException {
		
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
        ImageWriter writer = ImageIO.getImageWritersByFormatName( "jpg" ).next(); 
        ImageOutputStream ios = ImageIO.createImageOutputStream( bao ); 
        writer.setOutput( ios ); 
        ImageWriteParam iwparam = new JPEGImageWriteParam( Locale.getDefault() ); 
        iwparam.setCompressionMode( ImageWriteParam.MODE_EXPLICIT ) ; 
        iwparam.setCompressionQuality( 1.0f ); 
        writer.write( null, new IIOImage(img, null, null), iwparam ); 
        ios.flush(); 
        writer.dispose(); 
        ios.close(); 
        
        return Base64.getEncoder().encodeToString(bao.toByteArray());
	}

}
