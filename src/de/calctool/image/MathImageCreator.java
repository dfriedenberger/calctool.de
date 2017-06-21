package de.calctool.image;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import de.calctool.func.MathFunction;
import de.calctool.parser.MathDefinitions;
import de.calctool.vm.MathDataSet;
import de.calctool.vm.MathImage;
import de.calctool.vm.MathResult;
import de.calctool.vm.MathRuntime;
import de.calctool.vm.MathTerm;
import de.calctool.vm.MathVM;

public class MathImageCreator {

	private final MathDefinitions definitions;
	private final MathRuntime rt;

	public int HP = 20;
	public int WIDTH = 250;
	
	
	public MathImageCreator(MathDefinitions definitions, MathRuntime rt) {
		this.definitions = definitions;
		this.rt = rt;
	}

	
	public MathImage createOperationImage(String text,MathTerm term1,MathTerm term2) {
		return concat(new MathImage[] { bracketImage(term1), createStringImage(text),bracketImage(term2) });
	}
	
	public MathImage createImage(MathTerm term) {
		if (term.simpleoperation > 0) {
			MathFunction f = definitions.getSimpleOperation(term.simpleoperation);
			if (f != null)
			{
				MathImage img = f.createImage(this, term.child.toArray(new MathTerm[0]));
				
				if(img != null) return img;
				System.out.println("Hier darf img eigentlich nicht null sein");
			}
		}

		if (term.function == null)
			switch (term.child.size()) {
			case 0:
				if (term.isnumber)
					return createStringImage(term.number.getString(false));
				if (term.isMatrix())
					return term.matrix.toImage(this); // TODO: Move t Image
				if(term.isDataSet())
					return this.createDatasetImage(term.getDataSet());
				return createStringImage(term.text);
			case 1:
				return concat(new MathImage[] { createStringImage(term.text), bracketImage(term.child.get(0)) });
			case 2:
				System.out.println("Hier darf man eigentlich nicht langkommen (case 2)");

				return createOperationImage(term.text, term.child.get(0), term.child.get(1));
			
			default:
				throw new RuntimeException(term.child.size() + " childs not supported");
			}
		else {
			// function

			MathFunction f = definitions.getFunction(term.function);
			if (f != null)
				// if (f.needImage(term.child))
				return f.createImage(this, term.child.toArray(new MathTerm[0]));

			MathImage img = createStringImage(term.function);
			if (term.child.size() > 0) {
				img = concat(new MathImage[] { img, createStringImage("(") });
				for (int i = 0; i < term.child.size(); i++) {
					if (i > 0)
						img = concat(new MathImage[] { img, createStringImage(",") });
					img = concat(new MathImage[] { img, createImage(term.child.get(i)) });
				}
				img = concat(new MathImage[] { img, createStringImage(")") });
			}
			return img;
		}
	}

	private MathImage createDatasetImage(MathDataSet dataSet) {

		
		MathImage img = createImage(200);
		img.clear(true);
		
		Graphics g = img.getGraphics();
		
		
		double minx = dataSet.getMinX();
		double maxx = dataSet.getMaxX();

		double miny = dataSet.getMinY();
		double maxy = dataSet.getMaxY();
		
		double off = 10;
		double fx = (WIDTH - 2 * off) / (maxx - minx);
		double fy = (img.getHeight() - 2 * off) / (maxy - miny);

		g.setColor(Color.black);
		for(int i = 0;i < dataSet.x.length;i++)
		{
			int x = (int) (off + (dataSet.x[i] - minx) * fx);
			int y = (int) (off + (img.getHeight() - 2 * off) - (dataSet.y[i] - miny) * fy);
			g.fillRect(x, y, 1, 1);
		}
		
		return img;
	}


	private MathImage bracketImage(MathTerm term) {
		if ((term.child.size() > 0) && term.withBracket(term.child.get(0))) {
			return concat(new MathImage[] { createStringImage("("), createImage(term), createStringImage(")") });
		}
		return createImage(term);
	}

	public MathImage createImage(int h) {

		int lines = h / HP;

		if(lines <= 0) lines = 1;
		
		BufferedImage img = new BufferedImage(WIDTH, lines * HP, BufferedImage.TYPE_INT_RGB);
		return new MathImage(img, "image.jpg", lines, rt);
	}

	public MathImage concat(MathImage img[]) {
		int h = 0;
		int w = 0;
		for (int x = 0; x < img.length; x++) {
			if (img[x].getHeight() > h)
				h = img[x].getHeight();
			w += img[x].filledWidth();
		}
		int lines = (h + HP - 1) / HP;
		h = lines * HP;
		MathImage mimg = createImage(h);
		Graphics g = mimg.getGraphics();

		w = 0;
		for (int x = 0; x < img.length; x++) {
			int dx1 = w;
			int dx2 = dx1 + img[x].filledWidth();
			int dy1 = (h - img[x].getHeight()) / 2;
			int dy2 = dy1 + img[x].getHeight();

			int sx1 = 0;
			int sx2 = img[x].filledWidth();
			int sy1 = 0;
			int sy2 = img[x].getHeight();

			g.drawImage(img[x].getImage(), dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);

			w += img[x].filledWidth();
			mimg.setMaxwidth(w);
		}
		return mimg;

	}

	public MathImage concat(MathImage img[][]) {
		int h[] = new int[img.length];
		int w[] = new int[img[0].length];
		for (int r = 0; r < img.length; r++)
			for (int c = 0; c < img[0].length; c++) {
				if (r == 0)
					w[c] = 0;
				if (c == 0)
					h[r] = 0;
				if (img[r][c].getHeight() > h[r])
					h[r] = img[r][c].getHeight();
				if (img[r][c].filledWidth() > w[c])
					w[c] = img[r][c].filledWidth();
			}

		int lines = (sum(h, h.length) + HP - 1) / HP;
		MathImage mimg = createImage(lines * HP);
		Graphics g = mimg.getGraphics();

		for (int r = 0; r < img.length; r++)
			for (int c = 0; c < img[0].length; c++) {
				int lw = img[r][c].filledWidth();
				int dx1 = sum(w, c) + (w[c] - lw) / 2;
				int dx2 = dx1 + lw;
				int dy1 = sum(h, r) + (h[r] - img[r][c].getHeight()) / 2;
				int dy2 = dy1 + img[r][c].getHeight();

				int sx1 = 0;
				int sx2 = img[r][c].filledWidth();
				int sy1 = 0;
				int sy2 = img[r][c].getHeight();

				g.drawImage(img[r][c].getImage(), dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);

			}
		mimg.setMaxwidth(sum(w, w.length));
		return mimg;
	}

	private static int sum(int a[], int len) {
		int s = 0;
		for (int i = 0; i < len; i++)
			s += a[i];
		return s;
	}

	public MathImage createStringImage(String txt) {
		MathImage img = createImage(1);
		img.clear(false);
		img.println(txt);
		return img;
	}

	public BufferedImage toImage(MathResult result) {
		
		if(result.isText())
			return createStringImage(result.getText()).getImage();
		return createImage(result.term).getImage();

	}

	public int getRuntimeHP() {
		return HP;
	}

	
}
