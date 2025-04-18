package app.ewarehouse.serviceImpl;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import app.ewarehouse.dto.CommonCaptcha;
import app.ewarehouse.dto.CommonCaptchaGenerate;
import app.ewarehouse.service.CommonCaptchaGenerateService;

@Service
public class CommonCaptchaGenerateServiceImpl implements CommonCaptchaGenerateService {

	private static final Logger logger = LoggerFactory.getLogger(CommonCaptchaGenerateServiceImpl.class);

	@Override
	public CommonCaptcha generateCaptcha() {
		try {
			logger.info("Execute generateCaptcha() Method of CommonCaptchaGenerateServiceImpl class....!!");
			SecureRandom rand = SecureRandom.getInstanceStrong();

			String captchaText = generateCaptchaText(rand);
			Integer result = extractCaptchaResult(captchaText);
			String captchaId = UUID.randomUUID().toString();
			BufferedImage image = createCaptchaImage(captchaText);
			CommonCaptchaGenerate.put(captchaId, result);
			return new CommonCaptcha(captchaId, captchaText, image, result.toString());

		} catch (NoSuchAlgorithmException | IOException e) {
			logger.error("An error occurred while generating captcha.", e);
			return null;
		}
	}

	private String generateCaptchaText(SecureRandom rand) {
		int randomIndex = rand.nextInt(8);
		int[] array = new int[3];
		int a, b, d;
		String inputNumberString = "";

		return switch (randomIndex) {
		case 0 -> { // Multiplication
			a = rand.nextInt(9) + 1;
			b = rand.nextInt(9) + 1;
			inputNumberString = "What is the output " + a + " * " + b + " = ?";
			yield inputNumberString;
		}
		case 1 -> { // Subtraction
			a = rand.nextInt(89) + 10;
			b = rand.nextInt(9) + 1;
			inputNumberString = "What is the output " + a + " - " + b + " = ?";
			yield inputNumberString;
		}

		case 2 -> // Addition
		{
			a = rand.nextInt(9) + 1;
			b = rand.nextInt(99) + 1;
			inputNumberString = "What is the output " + a + " + " + b + " = ?";
			yield inputNumberString;
		}
		case 3 -> { // Greatest Number
			getRandomArray(rand, array);
			inputNumberString = "Which is the greatest number? " + array[0] + ", " + array[1] + ", " + array[2];
			yield inputNumberString;
		}
		case 4 -> {// Smallest Number
			getRandomArray(rand, array);
			inputNumberString = "Which is the smallest number? " + array[0] + ", " + array[1] + ", " + array[2];
			yield inputNumberString;
		}
		case 5 -> { // First Number
			a = rand.nextInt(99) + 1;
			b = rand.nextInt(99) + 1;
			d = rand.nextInt(99) + 1;
			inputNumberString = "Which is the first number? " + a + ", " + b + ", " + d;
			yield inputNumberString;
		}
		case 6 -> { // Last Number
			a = rand.nextInt(99) + 1;
			b = rand.nextInt(99) + 1;
			d = rand.nextInt(99) + 1;
			inputNumberString = "Which is the last number? " + a + ", " + b + ", " + d;
			yield inputNumberString;
		}
		case 7 -> { // Middle Number
			a = rand.nextInt(99) + 1;
			b = rand.nextInt(99) + 1;
			d = rand.nextInt(99) + 1;
			inputNumberString = "Which is the middle number? " + a + ", " + b + ", " + d;
			yield inputNumberString;
		}
		default -> null;
		};
	}

	public Integer extractCaptchaResult(String captchaText) {
		if (captchaText.contains("Which is the greatest number?")) {
			int[] numbers = extractNumbersFromText(captchaText);
			return getMax(numbers);
		} else if (captchaText.contains("Which is the smallest number?")) {
			int[] numbers = extractNumbersFromText(captchaText);
			return getMin(numbers);
		} else if (captchaText.contains("+")) {
			return extractAndCalculate(captchaText, "+");
		} else if (captchaText.contains("-")) {
			return extractAndCalculate(captchaText, "-");
		} else if (captchaText.contains("*")) {
			return extractAndCalculate(captchaText, "*");
		} else if (captchaText.contains("Which is the first number?")) {
			return extractFirstNumber(captchaText);
		} else if (captchaText.contains("Which is the last number?")) {
			return extractLastNumber(captchaText);
		} else if (captchaText.contains("Which is the middle number?")) {
			return extractMiddleNumber(captchaText);
		}
		return 0;
	}

	private int[] extractNumbersFromText(String captchaText) {
		String[] parts = captchaText.split("[^0-9]+");
		int[] numbers = new int[parts.length - 1];
		int index = 0;
		for (String part : parts) {
			if (!part.isEmpty()) {
				numbers[index++] = Integer.parseInt(part);
			}
		}
		return numbers;
	}

	private int extractAndCalculate(String captchaText, String operator) {
		String[] parts = captchaText.split("[^0-9]+");
		int[] numbers = new int[2];
		int count = 0;

		for (String part : parts) {
			if (!part.isEmpty() && count < 2) {
				numbers[count++] = Integer.parseInt(part.trim());
			}
		}

		if (numbers.length < 2) {
			throw new RuntimeException("Invalid captcha text, insufficient numbers for operation.");
		}

		switch (operator) {
		case "+":
			return numbers[0] + numbers[1];
		case "-":
			return numbers[0] - numbers[1];
		case "*":
			return numbers[0] * numbers[1];
		default:
			throw new UnsupportedOperationException("Unknown operator: " + operator);
		}
	}

	private int extractFirstNumber(String captchaText) {
		String[] parts = captchaText.split("[^0-9]+");
		return Integer.parseInt(parts[1]); // First number
	}

	private int extractLastNumber(String captchaText) {
		String[] parts = captchaText.split("[^0-9]+");
		return Integer.parseInt(parts[parts.length - 1]); // Last number
	}

	private int extractMiddleNumber(String captchaText) {
		String[] parts = captchaText.split("[^0-9]+");
		return Integer.parseInt(parts[2]); // Middle number
	}

	private int getMax(int[] array) {
		int max = array[0];
		for (int num : array) {
			if (num > max) {
				max = num;
			}
		}
		return max;
	}

	private int getMin(int[] array) {
		int min = array[0];
		for (int num : array) {
			if (num < min) {
				min = num;
			}
		}
		return min;
	}

	private void getRandomArray(SecureRandom rand, int[] array) {
		for (int i = 0; i < array.length; i++) {
			array[i] = rand.nextInt(99) + 1;
		}
	}

	private BufferedImage createCaptchaImage(String captchaText) throws IOException {
	    // Font settings with increased size
	    Font font = new Font("Roboto", Font.BOLD | Font.ITALIC, 40); // Larger font size for better visibility

	    // Create a temporary image to calculate text dimensions
	    BufferedImage tempImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
	    Graphics2D tempGraphics = tempImage.createGraphics();
	    tempGraphics.setFont(font);
	    FontMetrics fontMetrics = tempGraphics.getFontMetrics();
	    int textWidth = fontMetrics.stringWidth(captchaText); // Calculate text width
	    int textHeight = fontMetrics.getHeight(); // Calculate text height
	    int textAscent = fontMetrics.getAscent();
	    tempGraphics.dispose();

	    // Add some padding around the text
	    int padding = 20; // Maintain a cleaner look with padding
	    int imageWidth = textWidth + (2 * padding);
	    int imageHeight = textHeight + (2 * padding);

	    // Create the final image with calculated dimensions
	    BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
	    Graphics2D graphics2D = image.createGraphics();

	    // Set rendering hints for better quality
	    graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	    // Set the background color to a faded light blue and fill the entire image
	    graphics2D.setColor(new Color(230, 245, 255)); // Faded light blue color
	    graphics2D.fillRect(0, 0, imageWidth, imageHeight); // Fill the entire image with the background color

	    // Set the font and draw the text
	    graphics2D.setFont(font);
	    graphics2D.setColor(Color.BLACK); // Set text color to black for better contrast

	    // Calculate the position to center the text
	    int textX = (imageWidth - textWidth) / 2; // Center horizontally
	    int textY = ((imageHeight - textHeight) / 2) + textAscent; // Center vertically

	    // Draw the text at the calculated position
	    graphics2D.drawString(captchaText, textX, textY);

	    // Dispose of the graphics object
	    graphics2D.dispose();

	    return image;
	}



}
