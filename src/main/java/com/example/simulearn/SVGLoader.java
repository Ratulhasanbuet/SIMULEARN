package com.example.simulearn;

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

    /**
     * Load SVG with TRANSPARENT background
     */
    public static ImageView loadSVG(String svgPath, double width, double height) {
        return loadSVG(svgPath, width, height, true); // transparent by default
    }

    /**
     * Load SVG with option for transparent or colored background
     */
    public static ImageView loadSVG(String svgPath, double width, double height, boolean transparent) {
        try {
            InputStream svgStream = SVGLoader.class.getResourceAsStream(svgPath);

            if (svgStream == null) {
                System.err.println("⚠ SVG file not found: " + svgPath);
                // ✅ Try loading PNG version instead
                return loadPNGFallback(svgPath, width, height);
            }

            PNGTranscoder transcoder = new PNGTranscoder();

            // Set size
            transcoder.addTranscodingHint(PNGTranscoder.KEY_WIDTH, (float) width);
            transcoder.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, (float) height);

            // ✅ KEY CHANGE: Transparent background
            if (transparent) {
                // Don't set background color - makes it transparent
                transcoder.addTranscodingHint(
                        PNGTranscoder.KEY_BACKGROUND_COLOR,
                        new java.awt.Color(0, 0, 0, 0) // Fully transparent
                );
            } else {
                transcoder.addTranscodingHint(
                        PNGTranscoder.KEY_BACKGROUND_COLOR,
                        java.awt.Color.WHITE
                );
            }

            TranscoderInput input = new TranscoderInput(svgStream);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            TranscoderOutput output = new TranscoderOutput(outputStream);

            transcoder.transcode(input, output);
            outputStream.flush();

            // Convert to JavaFX Image with alpha channel
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
            // ✅ Try loading PNG version instead
            return loadPNGFallback(svgPath, width, height);
        }
    }

    /**
     * Try to load PNG version when SVG fails
     * Converts ".../you.svg" to ".../you.png"
     */
    private static ImageView loadPNGFallback(String svgPath, double width, double height) {
        try {
            // Convert .svg to .png
            String pngPath = svgPath.replaceFirst("\\.svg$", ".png");

            System.out.println("🔄 Trying PNG fallback: " + pngPath);

            InputStream pngStream = SVGLoader.class.getResourceAsStream(pngPath);

            if (pngStream == null) {
                System.err.println("❌ PNG fallback not found: " + pngPath);
                return createFallbackImage(width, height);
            }

            // Load PNG image
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
            // Create transparent fallback
            BufferedImage bufferedImage = new BufferedImage(
                    (int) width,
                    (int) height,
                    BufferedImage.TYPE_INT_ARGB // ✅ With alpha channel
            );

            java.awt.Graphics2D g2d = bufferedImage.createGraphics();

            // Draw with transparency
            g2d.setColor(new java.awt.Color(200, 200, 200, 100)); // Semi-transparent
            g2d.fillRect(0, 0, (int) width, (int) height);

            g2d.setColor(java.awt.Color.DARK_GRAY);
            g2d.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
            java.awt.FontMetrics fm = g2d.getFontMetrics();
            String text = "SVG";
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getHeight();
            g2d.drawString(text,
                    ((int) width - textWidth) / 2,
                    ((int) height + textHeight) / 2 - 4
            );

            g2d.dispose();

            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            return new ImageView(image);

        } catch (Exception e) {
            return new ImageView();
        }
    }
}