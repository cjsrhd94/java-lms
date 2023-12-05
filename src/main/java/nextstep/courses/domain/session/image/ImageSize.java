package nextstep.courses.domain.session.image;

public class ImageSize {
	private static final int WIDTH_MIN = 300;
	private static final int HEIGHT_MIN = 200;
	private static final double IMAGE_RATIO = 1.5d;

	private final int width;
	private final int height;

	public ImageSize(int width, int height) {
		this.width = width;
		this.height = height;
		validateImageSize();
	}

	private void validateImageSize() {
		validateWidthOverMin();
		validateHeightOverMin();
		validateRatioConstant();
	}

	private void validateWidthOverMin() {
		if (width < WIDTH_MIN) {
			throw new IllegalArgumentException("이미지의 가로 길이가 최소값 이하입니다.");
		}
	}

	private void validateHeightOverMin() {
		if (height < HEIGHT_MIN) {
			throw new IllegalArgumentException("이미지의 세로 길이가 최소값 이하입니다.");
		}
	}

	private void validateRatioConstant() {
		if ((double) width/height != IMAGE_RATIO) {
			throw new IllegalArgumentException("이미지의 가로 세로 길이 비율이 알맞지 않습니다.");
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
