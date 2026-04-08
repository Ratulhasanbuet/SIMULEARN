package com.example.simulearn.SimuLearn;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class SVGLoader {


    public static ImageView loadSVG(String svgPath, double width, double height) {
        return loadSVG(svgPath, width, height, true);
    }


    public static ImageView loadSVG(String svgPath, double width, double height, boolean transparent) {
        try {
            InputStream svgStream = SVGLoader.class.getResourceAsStream(svgPath);

            if (svgStream == null) {
                System.err.println("⚠ SVG file not found: " + svgPath);

                return loadPNGFallback(svgPath, width, height);
            }

            PNGTranscoder transcoder = new PNGTranscoder();


            transcoder.addTranscodingHint(PNGTranscoder.KEY_WIDTH, (float) width);
            transcoder.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, (float) height);


            if (transparent) {

                transcoder.addTranscodingHint(PNGTranscoder.KEY_BACKGROUND_COLOR, new java.awt.Color(0, 0, 0, 0));
            } else {
                transcoder.addTranscodingHint(PNGTranscoder.KEY_BACKGROUND_COLOR, java.awt.Color.WHITE);
            }

            TranscoderInput input = new TranscoderInput(svgStream);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            TranscoderOutput output = new TranscoderOutput(outputStream);

            transcoder.transcode(input, output);
            outputStream.flush();


            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);

            ImageView imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            imageView.setCache(true);

            svgStream.close();
            outputStream.close();
            inputStream.close();

            System.out.println("✅ Successfully loaded SVG (transparent): " + svgPath);
            return imageView;

        } catch (Exception e) {
            System.err.println("⚠ Error loading SVG: " + svgPath);
            e.printStackTrace();

            return loadPNGFallback(svgPath, width, height);
        }
    }


    private static ImageView loadPNGFallback(String svgPath, double width, double height) {
        try {

            String pngPath = svgPath.replaceFirst("\\.svg$", ".png");

            System.out.println("🔄 Trying PNG fallback: " + pngPath);

            InputStream pngStream = SVGLoader.class.getResourceAsStream(pngPath);

            if (pngStream == null) {
                System.err.println("❌ PNG fallback not found: " + pngPath);
                return createFallbackImage(width, height);
            }


            Image image = new Image(pngStream, width, height, true, true);

            ImageView imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            imageView.setCache(true);

            pngStream.close();

            System.out.println("✅ Successfully loaded PNG fallback: " + pngPath);
            return imageView;

        } catch (Exception e) {
            System.err.println("❌ Error loading PNG fallback");
            e.printStackTrace();
            return createFallbackImage(width, height);
        }
    }

    private static ImageView createFallbackImage(double width, double height) {
        try {

            BufferedImage bufferedImage = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB);

            java.awt.Graphics2D g2d = bufferedImage.createGraphics();


            g2d.setColor(new java.awt.Color(200, 200, 200, 100));
            g2d.fillRect(0, 0, (int) width, (int) height);

            g2d.setColor(java.awt.Color.DARK_GRAY);
            g2d.setFont(new java.awt.Font("DM Sans Medium", java.awt.Font.BOLD, 14));
            java.awt.FontMetrics fm = g2d.getFontMetrics();
            String text = "SVG";
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getHeight();
            g2d.drawString(text, ((int) width - textWidth) / 2, ((int) height + textHeight) / 2 - 4);

            g2d.dispose();

            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            return new ImageView(image);

        } catch (Exception e) {
            return new ImageView();
        }
    }
}