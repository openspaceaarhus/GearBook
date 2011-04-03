package dk.osaa.gb;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Reader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.GenericMultipleBarcodeReader;
import com.google.zxing.multi.MultipleBarcodeReader;

/**
 * Wrapper for ZXing, which allows mortals to use the library.
 */
public class QRDecoder {
	
	private static Logger log = Logger.getLogger(QRDecoder.class.getName());

	private static final Hashtable<DecodeHintType, Object> HINTS;
	private static final Hashtable<DecodeHintType, Object> HINTS_PURE;

	static {
		HINTS = new Hashtable<DecodeHintType, Object>(5);
		HINTS.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
		Collection<BarcodeFormat> possibleFormats = new Vector<BarcodeFormat>(17);
		possibleFormats.add(BarcodeFormat.QR_CODE);
		HINTS.put(DecodeHintType.POSSIBLE_FORMATS, possibleFormats);
		HINTS_PURE = new Hashtable<DecodeHintType, Object>(HINTS);
		HINTS_PURE.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
	}

	/**
	 * Simple QR decoding routing, will try very hard to find one or more QR codes in the image and return them.
	 * @param is The input stream where the image can be read from
	 * @return The results, might be 0 long.
	 */
	public static Collection<Result> decodeImage(InputStream is) { 

		Collection<Result> results = new ArrayList<Result>(1);

		BufferedImage image;
		try {
			image = ImageIO.read(is);
		} catch (Exception e) {
			log.log(Level.INFO, "Got exception while reading image from strea, perhaps it's corrupt", e);
			return results;
		}
		
		if (image == null) {
			log.log(Level.INFO, "bad image");
			return results;
		}

		Reader reader = new MultiFormatReader();
		LuminanceSource source = new BufferedImageLuminanceSource(image);
		BinaryBitmap bitmap = new BinaryBitmap(new GlobalHistogramBinarizer(source));

		try {
			// Look for multiple barcodes
			MultipleBarcodeReader multiReader = new GenericMultipleBarcodeReader(reader);
			Result[] theResults = multiReader.decodeMultiple(bitmap, HINTS);
			if (theResults != null) {
				results.addAll(Arrays.asList(theResults));
			}
		} catch (ReaderException re) {
		}

		if (results.isEmpty()) {
			try {
				// Look for pure barcode
				Result theResult = reader.decode(bitmap, HINTS_PURE);
				if (theResult != null) {
					results.add(theResult);
				}
			} catch (ReaderException re) {
			}
		}

		if (results.isEmpty()) {
			try {
				// Look for normal barcode in photo
				Result theResult = reader.decode(bitmap, HINTS);
				if (theResult != null) {
					results.add(theResult);
				}
			} catch (ReaderException re) {
			}
		}

		if (results.isEmpty()) {
			try {
				// Try again with other binarizer
				BinaryBitmap hybridBitmap = new BinaryBitmap(new HybridBinarizer(source));
				Result theResult = reader.decode(hybridBitmap, HINTS);
				if (theResult != null) {
					results.add(theResult);
				}
			} catch (ReaderException re) {
			}
		}

		if (results.isEmpty()) {
			log.fine("Failed to find any barcode in image");
			return results;
		}

		if (log.isLoggable(Level.FINE)) {
			for (Result result : results) {
				log.fine(result.getText());
			}
		}
		
		return results;
	}
	
	/**
	 * Just a small bit of test code, not really suitable for anything. 
	 * @param args Ignored
	 */
	public static void main(String[] args) {

		for (String in : args) {
			System.out.print(in);
			
			try {
				File f = new File(in);
				Collection<Result> results =  QRDecoder.decodeImage(new FileInputStream(f));
				for (Result r : results) {
					System.out.print("\t");
					System.out.print(r);
				}
				
			} catch (FileNotFoundException e) {
			}
			System.out.print("\n");
		}
		
		System.exit(0);
	}
}
